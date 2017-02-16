package com.appbook.book.CallBackImpl;

import com.appbook.book.AppAdapter.HotSearchAppAdapter;
import com.appbook.book.CallBack.NetworkCallBack;
import com.cwvs.microlife.searchview.SearchActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/11/4.
 */

public class HotNetworkCallBackImpl implements NetworkCallBack {
    private SearchActivity activity;
    public HotNetworkCallBackImpl(SearchActivity activity){
        this.activity = new WeakReference<SearchActivity>(activity).get();
    }
    @Override
    public void onSuccess(ArrayList arrayList, boolean str,boolean cache) {
        if(arrayList==null){
            return;
        }
        activity.hotSearchAppAdapter = new HotSearchAppAdapter(activity,activity,arrayList);
        activity.gardViews.setAdapter(activity.hotSearchAppAdapter);
    }

    @Override
    public void onFiled(Throwable throwable,boolean str) {

    }

    @Override
    public void onError() {

    }
}
