package com.appbook.book.CallBackImpl;

import com.appbook.book.AppAdapter.IndexAppAdapter;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.fragment.Fragments1Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/10/29.
 */

public class RecommendedNetworkCallBackImpl implements NetworkCallBack {
    private Fragments1Activity fragments1;
    public IndexAppAdapter appAdapter;
    public RecommendedNetworkCallBackImpl(Fragments1Activity fragments1){
        this.fragments1 = new WeakReference<Fragments1Activity>(fragments1).get();
    }
    @Override
    public void onSuccess(ArrayList arrayList, boolean str,boolean cache) {
        if(arrayList==null){
            return;
        }
        if(appAdapter !=null){
            if(str){
                if(arrayList.size()>0){
                    appAdapter.dataList.addAll(arrayList);
                }
                appAdapter.notifyDataSetChanged();
                return;
            }
            appAdapter.dataList = arrayList;
            appAdapter .notifyDataSetChanged();
            return;
        }
            appAdapter = new IndexAppAdapter(fragments1,fragments1.getContext(),arrayList);
            fragments1.gridView.setAdapter(appAdapter);
    }

    @Override
    public void onFiled(Throwable throwable,boolean str) {

    }

    @Override
    public void onError() {

    }
}
