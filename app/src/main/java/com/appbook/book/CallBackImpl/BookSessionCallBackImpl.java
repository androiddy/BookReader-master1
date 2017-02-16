package com.appbook.book.CallBackImpl;

import android.content.Intent;

import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.TotalMainActivity;
import com.appbook.book.SplashActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/11/12.
 */

public class BookSessionCallBackImpl implements NetworkCallBack {
    public SplashActivity activity;
    public BookSessionCallBackImpl(SplashActivity activity){
        this.activity = new WeakReference<SplashActivity>(activity).get();
    }
    @Override
    public void onSuccess(ArrayList arrayList, boolean str,boolean cache) {
        Intent intent = new Intent(activity, TotalMainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onFiled(Throwable throwable,boolean str) {

    }

    @Override
    public void onError() {

    }
}
