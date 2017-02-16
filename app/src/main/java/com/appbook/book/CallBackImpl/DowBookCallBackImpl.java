package com.appbook.book.CallBackImpl;

import android.os.Message;
import android.widget.Toast;

import com.appbook.book.BookDetailActivity;
import com.appbook.book.CallBack.DowBookCallBack;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.HttpUtils.HttpUtils;
import com.appbook.book.MyRunApplication;
import com.justwayward.reader.utils.FileUtils;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by zhangzhongping on 16/10/31.
 */

public class DowBookCallBackImpl implements DowBookCallBack {
    private BookDetailActivity activity;
    public DowBookCallBackImpl(BookDetailActivity activity){
        this.activity = new WeakReference<BookDetailActivity>(activity).get();
    }
    @Override
    public void onSuccess(String mag,String path,String name) {
        FileUtils.fileChannelCopy(new File(path),
                new File(FileUtils.getChapterPath(name, 1)));
        Message message = new Message();
        message.what = 2;
        activity.handler.sendMessage(message);
        Toast.makeText(activity.getApplicationContext(),mag,0).show();
        activity.CloseShowDow();
    }

    @Override
    public void onFiled(String mag) {
        activity.CloseShowDow();
        Toast.makeText(activity.getApplicationContext(),mag,0).show();
    }

    @Override
    public void onProces(final long count, final long end) {
        long i = 0;
        if(count!=0){
            i = (long) ((count/(double)end)*100);
        }
        Message message = new Message();
        message.what = 1;
        message.obj = i;
        activity.handler.sendMessage(message);
    }
}
