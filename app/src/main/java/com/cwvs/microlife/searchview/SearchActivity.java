package com.cwvs.microlife.searchview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.appbook.book.AppAdapter.FindBookAppAdapter;
import com.appbook.book.AppAdapter.HotSearchAppAdapter;
import com.appbook.book.BookDetailActivity;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.HttpUtils.SendRequest;
import com.appbook.book.MyRunApplication;
import com.appbook.book.R;
import com.appbook.book.ViewUtils.LoadingDialog;
import com.appbook.book.entity.mBookInfo;
import com.appbook.book.utils.HttpUtils;
import com.appbook.book.utils.InputTools;
import com.appbook.book.widget.ListViewForScrollView;
import com.appbook.book.widget.PicassoUtlis;
import com.appbook.book.widget.VRefresh;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * 搜索
 * Created by Explorer on 2016/4/29.
 */
public class SearchActivity extends AppCompatActivity implements NetworkCallBack {
    public FindBookAppAdapter findBookAppAdapter;
	public EditText etSearchContent;
	private TextView tvSearch;
    public int number;
    public GridView gardViews;
	public ListView mListViewHistory;
    public LinearLayout ll_search_results;
    public  HotSearchAppAdapter hotSearchAppAdapter;
    public ImageView search_clear_content_iv;
    private ImageView search_iv_back;
    public VRefresh swipeRefreshLayout;
	public LoadingDialog loadingDialog = null;
    public void setNumber(int number){
        this.number = number;
    }
    public int getNumber(){
        return this.number;
    }
    private ScrollView scrollView;
    private ListViewForScrollView Listview;
    private CategoryListppAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
        initViews();
    }
	public void initViews() {
        loadingDialog = new LoadingDialog(SearchActivity.this,"正在搜索•••");
        gardViews = (GridView) findViewById(R.id.grid);
        scrollView = (ScrollView) findViewById(R.id.scrollViews);
        Listview = (ListViewForScrollView) findViewById(R.id.Listview);
        search_clear_content_iv = (ImageView) findViewById(R.id.search_clear_content_iv);
        ll_search_results = (LinearLayout) findViewById(R.id.ll_search_results);
		etSearchContent = (EditText) findViewById(R.id.et_search_content);
		tvSearch = (TextView) findViewById(R.id.textView6);
		mListViewHistory = (ListView) findViewById(R.id.lv_search_history);
        search_iv_back = (ImageView) findViewById(R.id.search_iv_back);
        swipeRefreshLayout = (VRefresh) findViewById(R.id.srlayout);
        swipeRefreshLayout.setView(this,mListViewHistory);
        search_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.gc();
                finish();
            }
        });
        search_clear_content_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseError();
                swipeRefreshLayout.setVisibility(View.GONE);
                ll_search_results.setVisibility(View.VISIBLE);
                etSearchContent.setText("");
            }
        });

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etSearchContent.getText().toString().length()<2){
                    Toast.makeText(SearchActivity.this,"最少输入2个关键字！",0).show();
                }else{
                    loadingDialog.show();
                    if(InputTools.KeyBoard(etSearchContent)){
						InputTools.HideKeyboard(etSearchContent);
					}
					swipeRefreshLayout.setMoreData(true);
                    SendRequest.Unmber = 0;
                    SendRequest.SendFindByBook(SearchActivity.this,SearchActivity.this,etSearchContent.getText().toString(),System.currentTimeMillis()-MyRunApplication.getTTME(),false,"");
                }
            }
        });
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

        swipeRefreshLayout.setOnLoadListener(new VRefresh.OnLoadListener() {
            @Override
            public void onLoadMore() {
                if(mListViewHistory.getCount()-1>=getNumber()){
                    swipeRefreshLayout.setLoading(false);
                    swipeRefreshLayout.setMoreData(false);
                    return;
                }
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SendRequest.Unmber += 10;
                        SendRequest.SendFindByBook(SearchActivity.this,SearchActivity.this,etSearchContent.getText().toString(),11000l,true,"");
                    }
                }, 0);
            }
        });
        SendRequest.SendHotBook(SearchActivity.this,SearchActivity.this);
	}
    @Override
    protected void onDestroy() {
        HttpUtils.Cancelrequest(this);
        super.onDestroy();
    }
    public void OpenError(){
        ll_search_results.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
        SendRequest.SendSearchError(getApplicationContext(),this);
        scrollView.smoothScrollTo(0,0);
    }
    public void CloseError(){
        scrollView.setVisibility(View.GONE);
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

    static class CategoryListppAdapter extends BaseAdapter {
        private SearchActivity activity;
        Context context;
        public ArrayList<mBookInfo> dataList= new ArrayList<>();
        private LayoutInflater v ;
        private View view;
        public CategoryListppAdapter(SearchActivity activity, Context context, ArrayList<mBookInfo> inputDataList)
        {
            this.activity = new WeakReference<SearchActivity>(activity).get();
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
