package com.appbook.book.CallBackImpl;


import android.view.View;
import com.appbook.book.AppAdapter.AppAdapter;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.MainActivity;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/10/29.
 */

public class DetailsNetworkCallBackImpl implements NetworkCallBack {
    private MainActivity activity;
    public AppAdapter appAdapter;
    public DetailsNetworkCallBackImpl(MainActivity activity){
        this.activity = new WeakReference<MainActivity>(activity).get();
    }

    @Override
    public void onSuccess(ArrayList arrayList, boolean str,boolean cache) {
        activity.refreshLayout1.setVisibility(View.GONE);
        activity.relativeLayout.setVisibility(View.GONE);
        activity.recyclerView.setVisibility(View.VISIBLE);
        activity.swipeRefreshLayout.setRefreshing(false);
        activity.swipeRefreshLayout.setLoading(false);
        if(arrayList==null){
            return;
        }
        if(appAdapter!=null){
            if(str){
                if(arrayList.size()>0){
                    appAdapter.dataList.addAll(arrayList);
                }
                appAdapter.notifyDataSetChanged();
                return;
            }
            appAdapter.dataList = arrayList;
            appAdapter.notifyDataSetChanged();
            return;
        }
        appAdapter = new AppAdapter(activity,activity,arrayList);
        activity.recyclerView.setAdapter(appAdapter);
    }
    @Override
    public void onFiled(Throwable throwable,boolean str) {
        activity.refreshLayout1.setVisibility(View.GONE);
        if( activity.recyclerView.getCount()<5){
            activity.recyclerView.setVisibility(View.GONE);
            activity.relativeLayout.setVisibility(View.VISIBLE);
    }
        activity.swipeRefreshLayout.setRefreshing(false);
        activity.swipeRefreshLayout.setLoading(false);
    }

    @Override
    public void onError() {

    }
}
