package com.appbook.book.ViewUtils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.appbook.book.TotalMainActivity;
import com.appbook.book.widget.PicassoUtlis;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by zhangzhongping on 16/12/1.
 */

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        PicassoUtlis.LoadImage(String.valueOf(path),imageView);
    }
}
