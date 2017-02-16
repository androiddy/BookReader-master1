package com.appbook.book.ViewUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appbook.book.R;

/**
 * Created by zhangzhongping on 16/10/24.
 */

public class LoadingDialog extends Dialog {

    private TextView tv;
    private String msg;
    public LoadingDialog(Context context,String msg) {
        super(context, R.style.loadingDialogStyle);
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewwinxin_dialog);
        tv = (TextView)findViewById(R.id.tv);
        tv.setText(msg);
        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);
    }

}


