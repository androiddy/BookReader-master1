package com.appbook.book.fragment.main2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.HttpUtils.SendRequest;
import com.appbook.book.MainActivity;
import com.appbook.book.R;
import com.appbook.book.ViewUtils.RefreshLayout;
import com.appbook.book.entity.ProjectInfo;
import com.appbook.book.entity.mBookInfo;
import com.appbook.book.fragment.main.BaseFragment;
import com.appbook.book.fragment.main.CategoryListActivity;
import com.appbook.book.utils.HttpUtils;
import com.appbook.book.widget.PicassoUtlis;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/11/24.
 */

public class DowListActivity extends BaseFragment implements NetworkCallBack{
    private static DowListActivity activity;
    private boolean isPrepared  = false;
    private View view;
    private ListView line1;
    private DowListppAdapter adapter;
    private RelativeLayout refreshLayout;
    private RelativeLayout refreshLayout1;
    private RefreshLayout swipeRefreshLayout;
    public static DowListActivity newInstance() {
        activity = new DowListActivity();
        return activity;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.activity_dow_detail,null);
            line1 = (ListView) view.findViewById(R.id.line1);
            refreshLayout1 = (RelativeLayout) view.findViewById(R.id.refreshLayout1);
            refreshLayout = (RelativeLayout) view.findViewById(R.id.refreshLayout);
            swipeRefreshLayout = (RefreshLayout) view.findViewById(R.id.srlayout);
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                    android.R.color.holo_orange_light, android.R.color.holo_green_light);
            // 设置下拉刷新监听器
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 500);
                }
            });
            swipeRefreshLayout.setOnLoadListener(null);
            isPrepared = true;
            line1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    activity.getActivity(), view, "transitionImg");
                    Intent intent = new Intent(activity.getActivity(), CategoryListActivity.class);
                    ProjectInfo appUnit1 = (ProjectInfo) adapter.getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("APP_OBTAIN", appUnit1);
                    intent.putExtras(bundle);
                    ActivityCompat.startActivity(activity.getActivity(), intent, options.toBundle());
                }
            });
            refreshLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshLayout1.setVisibility(View.VISIBLE);
                    refreshLayout.setVisibility(View.GONE);
                    SendRequest.SendDowListBook(DowListActivity.this);
                }
            });
            lazyLoad();
        }
        return view;
    }
    @Override
    protected void lazyLoad() {
        if (!isVisible||!isPrepared) {
            return;
        }
        if(isPrepared){
            SendRequest.SendDowListBook(this);
            isPrepared = false;
        }
    }


    @Override
    public void onSuccess(ArrayList arrayList, boolean str, boolean cache) {
        if(arrayList==null||arrayList.size()<=0){
            return;
        }
        refreshLayout1.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
        adapter = new DowListppAdapter(this,this.getContext(),arrayList);
        line1.setAdapter(adapter);
    }

    @Override
    public void onFiled(Throwable throwable, boolean str) {
        if(!str){
            refreshLayout1.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError() {

    }

    static class DowListppAdapter extends BaseAdapter {
        private DowListActivity activity;
        Context context;
        public ArrayList<ProjectInfo> dataList= new ArrayList<>();
        private LayoutInflater v ;
        private View view;
        public DowListppAdapter(DowListActivity activity, Context context, ArrayList<ProjectInfo> inputDataList)
        {
            this.activity = new WeakReference<DowListActivity>(activity).get();
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
            final ProjectInfo appUnit=dataList.get(position);
            Holder holder = null;
            view = convertView;
            if(view==null){
                holder = new Holder();
                view = v.inflate(R.layout.book_panhang_mian, null);
                holder.name = (TextView) view.findViewById(R.id.textView10);
                holder.index = (TextView) view.findViewById(R.id.textView11);
                holder.book_logo = (ImageView) view.findViewById(R.id.ivRecommendCover);
                view.setTag(holder);
            }else{
                holder = (Holder)  view.getTag();
            }
            PicassoUtlis.LoadImage(appUnit.getProject_logo(),holder.book_logo);
            holder.name.setText(appUnit.getProject_name());
            holder.index.setText(appUnit.getProject_index());
            return  view;
        }
        public static class Holder{
            public TextView name; //名称
            public TextView index; //名称
            public ImageView book_logo;


        }
    }
}
