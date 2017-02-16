package com.appbook.book;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.appbook.book.CallBackImpl.DetailsNetworkCallBackImpl;
import com.appbook.book.ViewUtils.RefreshLayout;
import com.appbook.book.entity.NnumberAll;
import com.appbook.book.entity.TypeAllInfo;
import com.appbook.book.utils.HttpUtils;
import com.appbook.book.widget.PicassoUtlis;
import com.appbook.book.widget.VRefresh;
import com.cwvs.microlife.searchview.SearchActivity;
import com.loopj.android.http.RequestParams;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public ListView recyclerView;
    private com.appbook.book.HttpUtils.HttpUtils httpUtils;
    private DetailsNetworkCallBackImpl detailsNetworkCallBackl;
    public VRefresh swipeRefreshLayout;
    private  RequestParams params;
    private TypeAllInfo type;
    private NnumberAll nnumberAll;
    private String URL = "/Book/Book/BookType.do";
    public RelativeLayout refreshLayout1;
    public RelativeLayout relativeLayout;
    public int number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        type = (TypeAllInfo) getIntent().getSerializableExtra("booktype");
        toolbar.setTitle(type.getType_name());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        relativeLayout = (RelativeLayout) findViewById(R.id.refreshLayout);
        relativeLayout.setOnClickListener(this);
        refreshLayout1 = (RelativeLayout) findViewById(R.id.refreshLayout1);
        recyclerView = (ListView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (VRefresh) findViewById(R.id.srlayout);
        swipeRefreshLayout.setView(this,recyclerView);
        detailsNetworkCallBackl = new DetailsNetworkCallBackImpl(this);
        nnumberAll = new NnumberAll();
        httpUtils = new com.appbook.book.HttpUtils.HttpUtils(MyRunApplication.getURL(URL),this,detailsNetworkCallBackl,nnumberAll);
        nnumberAll.setTIME_END(0);
        nnumberAll.setNUMBER(0);
        nnumberAll.setCurrentPage(0);
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                    android.R.color.holo_orange_light, android.R.color.holo_green_light);
            // 设置下拉刷新监听器
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    swipeRefreshLayout.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            nnumberAll.setCurrentPage(0);
                            params = new RequestParams();
                            params.put("type", type.getType_id());
                            params.put("id",nnumberAll.getCurrentPage());
                            httpUtils.NetworkRequest(params,false,0);
                            swipeRefreshLayout.setEnd_Loading(true);
                        }
                    }, 0);
                }
            });
            swipeRefreshLayout.setOnLoadListener(new VRefresh.OnLoadListener() {
                @Override
                public void onLoadMore() {
                    if(recyclerView.getCount()-1>=type.getType_number()){
                        swipeRefreshLayout.setLoading(false);
                        swipeRefreshLayout.setMoreData(false);
                        return;
                    }
                    swipeRefreshLayout.setMoreData(true);
                    swipeRefreshLayout.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            params = new RequestParams();
                            params.put("type", type.getType_id());
                            params.put("id",nnumberAll.getCurrentPage());
                            httpUtils.NetworkRequest(params,true,0);
                        }
                    }, 0);
                }
            });
        SendPost();
    }
    public void SendPost(){
        try {
            swipeRefreshLayout.autoRefresh();
        } catch (Exception e) {
            params = new RequestParams();
            params.put("type", type.getType_id());
            params.put("id",nnumberAll.getCurrentPage());
            httpUtils.NetworkRequest(params,false,0);
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.refreshLayout){
            relativeLayout.setVisibility(View.GONE);
            refreshLayout1.setVisibility(View.VISIBLE);
            SendPost();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        HttpUtils.Cancelrequest(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
