package com.appbook.book.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.appbook.book.R;
import com.appbook.book.fragment.UserFragmen;

import java.lang.ref.WeakReference;


public class AlertDialog implements OnClickListener {
	public Context context;
    public Dialog dialog;
    public LinearLayout lyt_comment;
    public LinearLayout mLytCommentVG;
    public NoTouchLinearLayout mLytEdittextVG;
    public EditText mCommentEdittext;
    public Button mSendBut;
    public Display display;
    public Button but_comment_sendsss;
    public LinearLayout lLayout_bg;
    public UserFragmen userFragmen;

	public AlertDialog(Context context, UserFragmen userFragmen) {
		WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);
		if(contextWeakReference.get()!=null){
			this.context = contextWeakReference.get();
		}
        this.userFragmen = new WeakReference<UserFragmen>(userFragmen).get();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public AlertDialog builder() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.comm_show, null);
		lLayout_bg = (LinearLayout) view.findViewById(R.id.suggestions_list);
		lyt_comment = (LinearLayout) view.findViewById(R.id.lyt_comment);
		mLytCommentVG = (LinearLayout) view.findViewById(R.id.comment_vg_lyt);
		mLytEdittextVG = (NoTouchLinearLayout) view.findViewById(R.id.edit_vg_lyt);
		mCommentEdittext = (EditText) view.findViewById(R.id.edit_comment);
		mSendBut = (Button) view.findViewById(R.id.but_comment_send);
        mSendBut.setOnClickListener(this);
		but_comment_sendsss = (Button) view.findViewById(R.id.but_comment_sendsss);
        but_comment_sendsss .setOnClickListener(this);
        mLytEdittextVG.setOnResizeListener(new NoTouchLinearLayout.OnResizeListener() {
            @Override
            public void OnResize() {
                userFragmen.onFocusChange(false);
            }
        });
		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

		return this;
	}


	public AlertDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}


	public void show() {
		dialog.show();
        userFragmen.onFocusChange(true);
	}

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.but_comment_send){
            userFragmen.isReply = false;
            userFragmen.isLogin();
        } else if( v.getId()==R.id.but_comment_sendsss){
            userFragmen.onFocusChange(false);
            dialog.dismiss();
        }
    }
	public void dismiss(){
		this.context = null;
		this.userFragmen = null;
	}
}
