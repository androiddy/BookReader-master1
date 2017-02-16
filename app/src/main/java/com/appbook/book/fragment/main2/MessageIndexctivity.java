package com.appbook.book.fragment.main2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.appbook.book.CallBack.DowPngCallBack;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.CommentsActvivty;
import com.appbook.book.HttpUtils.HttpUtils;
import com.appbook.book.HttpUtils.SendRequest;
import com.appbook.book.MyRunApplication;
import com.appbook.book.R;
import com.appbook.book.TotalMainActivity;
import com.appbook.book.User.LoginActivity;
import com.appbook.book.ViewUtils.LoadingDialog;
import com.appbook.book.entity.BookResult;
import com.appbook.book.entity.LogInUserInfo;
import com.appbook.book.entity.MessageInfoIndex;
import com.appbook.book.entity.TencetLogInInfo;
import com.appbook.book.utils.DateFormatUtils;
import com.appbook.book.utils.InputTools;
import com.appbook.book.utils.cache.LogInUtils;
import com.appbook.book.widget.ActionSheetDialog;
import com.appbook.book.widget.NoTouchLinearLayout;
import com.loopj.android.http.RequestParams;
import com.tencent.connect.UserInfo;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MessageIndexctivity extends AppCompatActivity implements NetworkCallBack {
    private RelativeLayout linearLayout;
    private WebView mWebView;
    private String url = "file:///android_asset/themes/news/news.html";
    public MessageInfoIndex messageInfoIndex;
    private LinearLayout mLytCommentVG;
    private NoTouchLinearLayout mLytEdittextVG;
    private EditText mCommentEdittext;
    private Button mSendBut;
    public static Tencent mTencent;
    private Activity mActivity = null;
    private boolean isReply = true;
    private CheckBox checkbox_anonymous;
    private String comment = "";
    private RequestParams requestParams;
    private LinearLayout lyt_comment;
    private FloatingActionButton lyt_comments;
    private  LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.messagelist);
        linearLayout = (RelativeLayout) this.findViewById(R.id.line1);
        mWebView = new WebView(getApplicationContext());
        linearLayout.addView(mWebView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        messageInfoIndex = (MessageInfoIndex) getIntent().getSerializableExtra("mesinfo");
        toolbar.setTitle(messageInfoIndex.getMessage_name());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        WebSettings settings = mWebView.getSettings();
        // 设置javaScript可用
        settings.setJavaScriptEnabled(true);
        // 绑定javaScript接口，可以实现在javaScript中调用我们的Android代码
        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(this), "obj");
        SendRequest.SendAddClick(this,null,messageInfoIndex.getId());
        mTencent = Tencent.createInstance("101373848", MessageIndexctivity.this);
        findView();
    }

    @Override
    protected void onDestroy() {
        mActivity = null;
        loadingDialog = null;
        com.appbook.book.utils.HttpUtils.Cancelrequest(this);
        mWebView.reload();
        mWebView.clearView();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
        System.gc();
        super.onDestroy();
    }
    /**
     * 隐藏或者显示输入框
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            /**
             *这堆数值是算我的下边输入区域的布局的，
             * 规避点击输入区域也会隐藏输入区域
             */

            v.getLocationInWindow(leftTop);
            int left = leftTop[0] - 50;
            int top = leftTop[1] - 50;
            int bottom = top + v.getHeight() + 300;
            int right = left + v.getWidth() + 120;
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    /**
     * 显示或隐藏输入法
     */
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        mCommentEdittext.getContext().getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    mCommentEdittext.requestFocus();//获取焦点
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                } else {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(mCommentEdittext.getWindowToken(), 0);
                    mLytCommentVG.setVisibility(View.VISIBLE);
                    mLytEdittextVG.setVisibility(View.GONE);
                    lyt_comments.setVisibility(View.VISIBLE);
                }
            }
        }, 100);
    }
    /**
     * 判断对话框中是否输入内容
     */
    private boolean isEditEmply() {
        comment = mCommentEdittext.getText().toString().trim();
        if (comment.equals("")) {
            if(loadingDialog!=null&&loadingDialog.isShowing()&&mActivity!=null&&!mActivity.isFinishing()){
                loadingDialog.dismiss();
            }
            Toast.makeText(getApplicationContext(), "评论不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    /**
     * 点击屏幕其他地方收起输入法
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                onFocusChange(false);

            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    /**
     * 事件点击监听器
     */
    private final class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.but_comment_send:        //发表评论按钮
                    loadingDialog = new LoadingDialog(MessageIndexctivity.this,"评论中•••");
                    loadingDialog.setCancelable(false);
                    loadingDialog.show();
                    isLogin();
                    break;
                case R.id.lyt_comment:        //底部评论按钮
                    lyt_comments.setVisibility(View.GONE);
                    isReply = false;
                    mLytEdittextVG.setVisibility(View.VISIBLE);
                    mLytCommentVG.setVisibility(View.GONE);
                    onFocusChange(true);
                    break;
                case R.id.lyt_comments:
                    Intent intent =new Intent(MessageIndexctivity.this,CommentsActvivty.class);
                    //用Bundle携带数据
                    Bundle bundle=new Bundle();
                    //传递name参数为tinyphp
                    bundle.putInt("mesid", messageInfoIndex.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
        }
    }
    /**初始化openid和token***/
    private static void initOpenidAndToken(String token,String expires,String openId) {
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                && !TextUtils.isEmpty(openId)) {
            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openId);
        }
    }

    public void isLogin(){
        String token =  (String) LogInUtils.get(getApplicationContext(),"QQToken","");
        String id = (String)LogInUtils.get(getApplicationContext(),"openid","");
        String expires = (String)LogInUtils.get(getApplicationContext(),"expires","");
        initOpenidAndToken(token,expires,id);
        UserInfo userinfo = new UserInfo(MessageIndexctivity.this, mTencent.getQQToken());
        userinfo.getUserInfo(new BaseUIListener("get_user_info",id,token));
    }
    public void findView() {
        checkbox_anonymous = (CheckBox) findViewById(R.id.checkbox_anonymous);
        checkbox_anonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"暂未开启",0).show();
            }
        });
        lyt_comment = (LinearLayout) findViewById(R.id.lyt_comment);
        mLytCommentVG = (LinearLayout) findViewById(R.id.comment_vg_lyt);
        mLytEdittextVG = (NoTouchLinearLayout) findViewById(R.id.edit_vg_lyt);
        mCommentEdittext = (EditText) findViewById(R.id.edit_comment);
        mSendBut = (Button) findViewById(R.id.but_comment_send);
        lyt_comments = (FloatingActionButton) findViewById(R.id.lyt_comments);
        ClickListener cl = new ClickListener();
        mSendBut.setOnClickListener(cl);
        lyt_comment.setOnClickListener(cl);
        mLytCommentVG.setOnClickListener(cl);
        lyt_comments.setOnClickListener(cl);
        mLytEdittextVG.setOnResizeListener(new NoTouchLinearLayout.OnResizeListener() {
            @Override
            public void OnResize() {
                InputTools.HideKeyboard(mLytEdittextVG);
                mLytEdittextVG.setVisibility(View.GONE);
                mLytCommentVG.setVisibility(View.VISIBLE);
                lyt_comments.setVisibility(View.VISIBLE);
            }
        });
        initWebView();

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }

            // 页面开始时调用
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            // 页面加载完成调用
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_LONG)
                        .show();
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_SHORT).show();
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue,
                        result);
            }
        });
        mWebView.loadUrl(url);
    }


    public void initWebView() {
        mWebView.setScrollbarFadingEnabled(false);
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 设置缓存模式
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(false);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(false);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断控件是否显示
        if (mLytEdittextVG.isShown()) {
            //Toast.makeText(getApplicationContext(),"111",0).show();
            onFocusChange(false);
            return true;
        }else{
            // 检查是否为返回事件，如果有网页历史记录
            if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        // 如果不是返回键或没有网页浏览历史，保持默认
        // 系统行为（可能会退出该活动）
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSuccess(final ArrayList arrayList, boolean str, boolean cache) {
        MessageIndexctivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(arrayList!=null){
                    Toast.makeText(getApplicationContext(),((BookResult)arrayList.get(0)).getMsg(),0).show();
                }
                mCommentEdittext.setText("");
                if(loadingDialog!=null&&loadingDialog.isShowing()&&mActivity!=null&&!mActivity.isFinishing()){
                    loadingDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onFiled(Throwable throwable, boolean str) {
        MessageIndexctivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),"评论失败",0).show();
                if(loadingDialog!=null&&loadingDialog.isShowing()&&mActivity!=null&&!mActivity.isFinishing()){
                    loadingDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onError() {

    }

    final class DemoJavaScriptInterface {
        private  ActionSheetDialog actionSheetDialog = null;
        private String URL = "http://www.gameqj.xyz:8080/Book/messages/findAllByMesId.do";
        public  MessageIndexctivity messageIndexctivity;
        DemoJavaScriptInterface(MessageIndexctivity messageIndexctivity) {
            this.messageIndexctivity = messageIndexctivity;
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public String getQueryStr(){
            int name = messageIndexctivity.messageInfoIndex.getId();
            return URL+"?id="+name;
        }
        @JavascriptInterface
        public String getExtrastitle(){
            return messageIndexctivity.messageInfoIndex.getMessage_name();
        }
        @JavascriptInterface
        public String getExtrastag(){
            return messageIndexctivity.messageInfoIndex.getMessage_typename();
        }
        @JavascriptInterface
        public void openImage(final String pngurl, String srcurl){
            actionSheetDialog = new ActionSheetDialog(MessageIndexctivity.this);
            actionSheetDialog.builder().setCancelable(true)
                    .setCanceledOnTouchOutside(true).addSheetItem("保存图片",
                    ActionSheetDialog.SheetItemColor.Blue, new OnSheetItemClickListeners(MessageIndexctivity.this,MessageIndexctivity.this,pngurl)).show();
        }
    }
    private static class  OnSheetItemClickListeners implements ActionSheetDialog.OnSheetItemClickListener
    {
        public LoadingDialog loadingDialog = null;
        private Toast toast =  null;
        private Context mContetxt;
        private MessageIndexctivity activity;
        private String pngurl = null;
        public OnSheetItemClickListeners(Context mContetxt,MessageIndexctivity activity,String pngurl){
            WeakReference<Context> contextWeakReference = new WeakReference<Context>(mContetxt);
            WeakReference<MessageIndexctivity> activityWeakReference = new WeakReference<MessageIndexctivity>(activity);
            if(contextWeakReference.get()!=null){
                this.mContetxt = contextWeakReference.get();
            }else{
                this.mContetxt = null;
            }
            if(activityWeakReference.get()!=null){
                this.activity = activityWeakReference.get();
            }else{
                this.activity = null;
            }
            this.pngurl = pngurl;
        }
        @Override
        public void onClick(int which) {
            switch (which){
                case 1:
                    loadingDialog = new LoadingDialog(mContetxt,"保存中•••");
                    loadingDialog.setCancelable(true);
                    loadingDialog.show();
                    new Thread(){
                        @Override
                        public void run() {
                            DateFormatUtils.IsDowIamge(pngurl,new DowPngCallBack(){
                                @Override
                                public void onSuccess(final String msg) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(loadingDialog!=null&&loadingDialog.isShowing()&&activity.mActivity!=null&&!activity.mActivity.isFinishing()){
                                                loadingDialog.dismiss();
                                            }
                                            toast = Toast.makeText(activity,msg,0);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }
                                    });
                                }
                                @Override
                                public void onError(final String msg) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(loadingDialog!=null&&loadingDialog.isShowing()&&activity.mActivity!=null&&!activity.mActivity.isFinishing()){
                                                loadingDialog.dismiss();
                                            }
                                            toast = Toast.makeText(activity,msg,0);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }
                                    });
                                }
                            });
                        }
                    }.start();
                    break;
            }
        }
    }
    public class BaseUIListener implements IUiListener {
        private String scope;
        private String openid;
        private String token;

        public BaseUIListener(String scope,String openid,String token) {
            this.scope = scope;
            this.openid = openid;
            this.token = token;

        }
        @Override
        public void onComplete(final Object o) {
                JSONObject jsonObject = (JSONObject) o;
                String json = jsonObject.toString();
                LogInUserInfo tencetLogInInfo = JSON.parseObject(json, LogInUserInfo.class);
                if (tencetLogInInfo!=null&&tencetLogInInfo.getRet()== 0) {

                    String url = tencetLogInInfo.getFigureurl_qq_2();
                    if("".equals(url.trim())){
                        url = tencetLogInInfo.getFigureurl_qq_1();
                    }
                    LogInUtils.put(getApplicationContext(),"QQName", tencetLogInInfo.getNickname());
                    LogInUtils.put(getApplicationContext(),"QQLogo", url);
                    requestParams = new RequestParams();
                    requestParams.put("id",openid);
                    requestParams.put("name",java.net.URLEncoder.encode(tencetLogInInfo.getNickname()));
                    requestParams.put("logo",url);
                    requestParams.put("token",token);
                    requestParams.put("gender",java.net.URLEncoder.encode(tencetLogInInfo.getGender()));
                    HttpUtils httpUtils = new HttpUtils(MyRunApplication.getURL("/Book/login/adduser.do"),MessageIndexctivity.this,new LogIn(), null);
                    httpUtils.AddUserRequest(requestParams,false);
                }else{
                    if(loadingDialog!=null&&loadingDialog.isShowing()&&mActivity!=null&&!mActivity.isFinishing()){
                        loadingDialog.dismiss();
                    }
                    onError(null);
                }

        }

        @Override
        public void onError(final UiError uiError) {
            startActivity(new Intent(MessageIndexctivity.this,LoginActivity.class));
        }

        @Override
        public void onCancel() {
            MessageIndexctivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"登录取消",0).show();
                }
            });
        }
    }

    class LogIn implements NetworkCallBack{

        @Override
        public void onSuccess(ArrayList arrayList, boolean str, boolean cache) {
            if (isEditEmply()) {        //判断用户是否输入内容
                if (!isReply) {
                    String id = (String)LogInUtils.get(getApplicationContext(),"openid","");
                    // Toast.makeText(getApplicationContext(),java.net.URLDecoder.decode(mCommentEdittext.getText().toString()),0).show();
                    SendRequest.AddMes(MessageIndexctivity.this,java.net.URLEncoder.encode(mCommentEdittext.getText().toString()),id,messageInfoIndex.getId());
                }
                onFocusChange(false);
            }
        }

        @Override
        public void onFiled(Throwable throwable, boolean str) {
            MessageIndexctivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"网络错误",0).show();
                    if(loadingDialog!=null&&loadingDialog.isShowing()&&mActivity!=null&&!mActivity.isFinishing()){
                        loadingDialog.dismiss();
                    }
                }
            });
        }

        @Override
        public void onError() {

        }
    }
}
