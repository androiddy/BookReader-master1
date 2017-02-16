package com.appbook.book.CallBackImpl;

import android.view.View;
import android.widget.Toast;

import com.appbook.book.AppAdapter.FindBookAppAdapter;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.HttpUtils.SendRequest;
import com.appbook.book.MyRunApplication;
import com.appbook.book.entity.mBookInfo;
import com.cwvs.microlife.searchview.SearchActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/11/4.
 */

public class FindBookCallBackImpl implements NetworkCallBack {

    private SearchActivity activity;
    public FindBookCallBackImpl(SearchActivity activity){
        this.activity = new WeakReference<SearchActivity>(activity).get();
    }
    @Override
    public void onSuccess(ArrayList arrayList, boolean str,boolean cache) {
        if(activity.loadingDialog!=null){
            activity.loadingDialog.dismiss();
        }
        if(arrayList==null){
            return;
        }
        if(arrayList.size()>0){
            activity.CloseError();
            if(str){
                activity.swipeRefreshLayout.setLoading(false);
                activity.findBookAppAdapter.dataList.addAll(arrayList);
                activity.findBookAppAdapter.notifyDataSetChanged();
                return;
            }
            MyRunApplication.setTTME(System.currentTimeMillis());
            activity.setNumber(((mBookInfo)arrayList.get(0)).getNumber());
            activity.ll_search_results.setVisibility(View.GONE);
            activity.swipeRefreshLayout.setVisibility(View.VISIBLE);
            activity.findBookAppAdapter = new FindBookAppAdapter(activity,activity,arrayList);
            activity.mListViewHistory.setAdapter(activity.findBookAppAdapter);
        }else{
            activity.OpenError();
            if(str){
                activity.swipeRefreshLayout.setLoading(false);
            }else{
                Toast.makeText(activity,"未搜索到数据！",0).show();
            }
        }
    }
    @Override
    public void onFiled(Throwable throwable,boolean str) {
        try{
            activity.loadingDialog.dismiss();
            activity.swipeRefreshLayout.setLoading(false);
        }catch (Exception e){

        }finally {
            MyRunApplication.setTTME(System.currentTimeMillis());
            Toast.makeText(activity,"网络错误！",0).show();
        }
    }

    @Override
    public void onError() {
        activity.OpenError();
    }
}
