package com.appbook.book.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.appbook.book.*;
import com.appbook.book.AppAdapter.FindBookAppAdapter;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.HttpUtils.SendRequest;
import com.appbook.book.entity.ProjectInfo;
import com.appbook.book.entity.mBookInfo;
import com.appbook.book.utils.HttpUtils;
import com.appbook.book.widget.PicassoUtlis;
import com.cwvs.microlife.searchview.SearchActivity;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/11/24.
 */

public class CategoryListActivity extends AppCompatActivity implements NetworkCallBack {
    private static CategoryListActivity activity;
    private ImageView ivRecommendCover;
    private TextView textView10;
    private TextView textView11;
    private ListView Listview;
    private CategoryListppAdapter adapter;
    private ScrollView scrollView;
    private ProjectInfo projectInfo;

    public static CategoryListActivity newInstance() {
        activity = new CategoryListActivity();
        return activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dow_detail_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        projectInfo = (ProjectInfo) getIntent().getSerializableExtra("APP_OBTAIN");
        toolbar.setTitle(projectInfo.getProject_name());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivRecommendCover = (ImageView) findViewById(R.id.ivRecommendCover);
        textView10 = (TextView) findViewById(R.id.textView10);
        textView11 = (TextView) findViewById(R.id.textView11);
        Listview = (ListView) findViewById(R.id.Listview);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0,0);
        InitData();
    }

    private void InitData(){
        Picasso.with(this).load(projectInfo.getProject_logo()).fit().into(ivRecommendCover);
        textView10.setText(projectInfo.getProject_name());
        textView11.setText(projectInfo.getProject_index());
        SendRequest.SendFindByPro(this,this,projectInfo.getProject_id());
    }

    @Override
    protected void onDestroy() {
        HttpUtils.Cancelrequest(this);
        super.onDestroy();
    }

    @Override
    public void onSuccess(ArrayList arrayList, boolean str, boolean cache) {
        if(arrayList==null){
            return;
        }
        adapter = new CategoryListppAdapter(this,this,arrayList);
        Listview.setAdapter(adapter);
    }

    @Override
    public void onFiled(Throwable throwable, boolean str) {

    }

    @Override
    public void onError() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                Intent intent = new Intent(CategoryListActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    static class CategoryListppAdapter extends BaseAdapter {
        private CategoryListActivity activity;
        Context context;
        public ArrayList<mBookInfo> dataList= new ArrayList<>();
        private LayoutInflater v ;
        private View view;
        public CategoryListppAdapter(CategoryListActivity activity, Context context, ArrayList<mBookInfo> inputDataList)
        {
            this.activity = new WeakReference<CategoryListActivity>(activity).get();
            this.context=context;
            this.v = LayoutInflater.from(context);
            dataList.clear();

            for(int i=0;i<inputDataList.size();i++)
            {
                dataList.add(inputDataList.get(i));
            }
            inputDataList.clear();
            inputDataList = null;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return dataList.size();
        }


        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return dataList.get(position);
        }


        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(final int position, final View convertView, ViewGroup parent) {
            final mBookInfo appUnit=(mBookInfo)dataList.get(position);
            Holder holder = null;
            view = convertView;
            if(view==null){
                holder = new Holder();
                view = v.inflate(R.layout.item_findbook, null);
                holder.name = (TextView) view.findViewById(R.id.tvLatelyUpdate);
                holder.names = (TextView) view.findViewById(R.id.tvRecommendTitle);
                holder.number = (TextView) view.findViewById(R.id.tvRecommendShort);
                holder.book_logo2 = (ImageView) view.findViewById(R.id.ivRecommendCover);
                view.setTag(holder);
            }else{
                holder = (Holder)  view.getTag();
            }
            PicassoUtlis.LoadImage(appUnit.getLogo(),holder.book_logo2);
            holder.names.setText(appUnit.getBook_name());
            holder.name .setText(appUnit.getBook_author()+" | "+appUnit.getBook_ytpename()+" | "+appUnit.getBook_number());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, BookDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("book",appUnit);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            });
            return  view;
        }
        public static class Holder{
            public TextView name; //名称
            public TextView names; //名称
            public TextView number;
            public ImageView book_logo2;
        }
    }
}
