package com.appbook.book.CallBackImpl;

import android.view.View;
import android.widget.Toast;

import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.entity.mBookInfo;
import com.appbook.book.fragment.main.Mains1Activity;
import com.justwayward.reader.bean.Recommend;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhongping on 16/11/4.
 */

public class NetworkCallBackImpl implements NetworkCallBack{
    public Mains1Activity activity;
    public NetworkCallBackImpl(Mains1Activity activity){
        this.activity = new WeakReference<Mains1Activity>(activity).get();
    }
    @Override
    public void onSuccess(ArrayList arrayList, boolean str,boolean cache) {
        activity.refreshLayout1.setVisibility(View.GONE);
        if(!activity.swipeRefreshLayout.isShown()){
            activity.swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
        if(arrayList==null){
            return;
        }
        activity.swipeRefreshLayout.setRefreshing(false);
        ArrayList<mBookInfo> m = arrayList;
        List<Recommend.RecommendBooks> c = new ArrayList<>();
        for (mBookInfo book : m){
            Recommend.RecommendBooks r = new Recommend.RecommendBooks();
            r.bookInfo = book;
            r.isNetwore = true;
            c.add(r);
        }
        activity.mAdapter.addAll(c);
        activity.mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFiled(Throwable throwable,boolean str) {
        if(!str){
            activity.refreshLayout1.setVisibility(View.GONE);
            if(!activity.swipeRefreshLayout.isShown()){
                activity.swipeRefreshLayout.setVisibility(View.VISIBLE);
            }
            activity.swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onError() {

    }
}
