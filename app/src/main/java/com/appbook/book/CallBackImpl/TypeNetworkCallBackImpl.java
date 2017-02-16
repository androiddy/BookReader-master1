package com.appbook.book.CallBackImpl;

import android.view.View;

import com.appbook.book.AppAdapter.TypeAppAdapter;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.fragment.main.MainActivity;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/10/29.
 */

public class TypeNetworkCallBackImpl implements NetworkCallBack {
    private MainActivity activity;
    public  TypeAppAdapter appAdapter;
    public TypeNetworkCallBackImpl(MainActivity fragments1){
        this.activity = new WeakReference<MainActivity>(fragments1).get();
    }
    @Override
    public void onSuccess(ArrayList arrayList, boolean str,boolean cache) {
        activity.relativeLayout.setVisibility(View.GONE);
        activity.refreshLayout1.setVisibility(View.GONE);
        activity.listView.setVisibility(View.VISIBLE);
        activity.swipeRefreshLayout.setRefreshing(false);
        activity.swipeRefreshLayout.setLoading(false);
        activity.global_search_action_bar_rl.setVisibility(View.VISIBLE);
        if(arrayList==null){
            return;
        }
        if(appAdapter!=null){
            appAdapter.dataList = arrayList;
            appAdapter.notifyDataSetChanged();
            return;
        }
        appAdapter = new TypeAppAdapter(activity,activity.getContext(),arrayList);
        activity.listView.setAdapter(appAdapter);
    }

    @Override
    public void onFiled(Throwable throwable,boolean str) {
        if(!str){
            activity.global_search_action_bar_rl.setVisibility(View.GONE);
            activity.listView.setVisibility(View.GONE);
            activity.refreshLayout1.setVisibility(View.GONE);
            activity.relativeLayout.setVisibility(View.VISIBLE);
            activity.swipeRefreshLayout.setRefreshing(false);
            activity.swipeRefreshLayout.setLoading(false);
        }
    }

    @Override
    public void onError() {

    }
}
