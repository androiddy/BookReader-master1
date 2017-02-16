package com.justwayward.reader.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.appbook.book.R;
import com.appbook.book.User.LoginActivity;

/**
 * @author yuyh.
 * @date 16/9/5.
 */
public class LoginPopupWindow  implements View.OnTouchListener {

    private View mContentView;
    private Activity mActivity;
    private ImageView qq;
    private ImageView weibo;
    private ImageView wechat;
    public PopupWindow pw;
    private LinearLayout linnnnnn;
    LoginTypeListener listener;

    public LoginPopupWindow( Activity activity) {
        mActivity = activity;

        mContentView = LayoutInflater.from(activity).inflate(R.layout.layout_login_popup_window, null);

        pw = new PopupWindow(mContentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pw.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        linnnnnn = (LinearLayout) mContentView.findViewById(R.id.linnnnnn);
        qq = (ImageView) mContentView.findViewById(R.id.ivQQ);
        weibo = (ImageView) mContentView.findViewById(R.id.ivWeibo);
        wechat = (ImageView) mContentView.findViewById(R.id.ivWechat);

        qq.setOnTouchListener(this);
        weibo.setOnTouchListener(this);
        wechat.setOnTouchListener(this);
        linnnnnn.setFocusableInTouchMode(true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));

        pw.setAnimationStyle(R.style.LoginPopup);

        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lighton();
                pw.dismiss();
                if(mActivity instanceof LoginActivity){
                    mActivity.finish();
                }
            }
        });

    }

    private void scale(View v, boolean isDown) {
        if (v.getId() == qq.getId() || v.getId() == weibo.getId() || v.getId() == wechat.getId()) {
            if (isDown) {
                Animation testAnim = AnimationUtils.loadAnimation(mActivity, R.anim.scale_down);
                v.startAnimation(testAnim);
            } else {
                Animation testAnim = AnimationUtils.loadAnimation(mActivity, R.anim.scale_up);
                v.startAnimation(testAnim);
            }
        }
        if (!isDown && listener!=null) {
            switch (v.getId()) {
                case R.id.ivQQ:
                    listener.onLogin(pw,qq, "QQ");
                    break;
            }
        }
    }

    private void lighton() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 1.0f;
        mActivity.getWindow().setAttributes(lp);
    }

    private void lightoff() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.3f;
        mActivity.getWindow().setAttributes(lp);
    }


    public void showAtLocation(View parent, int gravity, int x, int y) {
      //  lightoff();
        pw.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scale(v, true);
                break;
            case MotionEvent.ACTION_UP:
                scale(v, false);
                break;
        }
        return false;
    }

    public interface LoginTypeListener {

        void onLogin(PopupWindow pm,ImageView view, String type);
    }

    public void setLoginTypeListener(LoginTypeListener listener){
        this.listener = listener;
    }

}
