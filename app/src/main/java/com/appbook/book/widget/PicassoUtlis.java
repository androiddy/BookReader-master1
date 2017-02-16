package com.appbook.book.widget;

import android.widget.ImageView;

import com.appbook.book.MyRunApplication;
import com.appbook.book.R;

import com.squareup.picasso.Picasso;

/**
 * Created by zhangzhongping on 16/12/1.
 */

public class PicassoUtlis {
    public static void LoadImage(String url, ImageView imageView){
         Picasso.with(MyRunApplication.getAppContext()).load(url).placeholder(R.mipmap.yujiazai).fit().into(imageView);
    }
    public static void LoadImage(String[] url, ImageView... imageView){
        Picasso.with(MyRunApplication.getAppContext()).load(url[0]).placeholder(R.mipmap.yujiazai).fit().into(imageView[0]);
        Picasso.with(MyRunApplication.getAppContext()).load(url[1]).placeholder(R.mipmap.yujiazai).fit().into(imageView[1]);
        Picasso.with(MyRunApplication.getAppContext()).load(url[2]).placeholder(R.mipmap.yujiazai).fit().into(imageView[2]);
    }
    public static void LoadImages( String[] url, ImageView... imageView){
        Picasso.with(MyRunApplication.getAppContext()).load(url[0]).into(imageView[0]);
        Picasso.with(MyRunApplication.getAppContext()).load(url[1]).into(imageView[1]);
        Picasso.with(MyRunApplication.getAppContext()).load(url[2]).into(imageView[2]);
    }
}
