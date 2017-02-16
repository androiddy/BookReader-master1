package com.appbook.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.appbook.book.AppAdapter.IndexAppAdapter;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.CallBackImpl.DetailsNetworkCallBackImpl;
import com.appbook.book.HttpUtils.HttpUtils;
import com.appbook.book.HttpUtils.SendRequest;
import com.appbook.book.User.LoginActivity;
import com.appbook.book.ViewUtils.LoadingDialog;
import com.appbook.book.ViewUtils.RefreshLayout;
import com.appbook.book.entity.BookResult;
import com.appbook.book.entity.CommentsInfo;
import com.appbook.book.entity.LogInUserInfo;
import com.appbook.book.entity.MessageInfoIndex;
import com.appbook.book.entity.NnumberAll;
import com.appbook.book.entity.mBookInfo;
import com.appbook.book.fragment.main2.Find_hotRecommendFragment;
import com.appbook.book.fragment.main2.MessageIndexctivity;
import com.appbook.book.utils.DateFormatUtils;
import com.appbook.book.utils.InputTools;
import com.appbook.book.utils.cache.LogInUtils;
import com.appbook.book.widget.NoTouchLinearLayout;
import com.appbook.book.widget.PicassoUtlis;
import com.appbook.book.widget.VRefresh;
import com.loopj.android.http.RequestParams;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/12/17.
 */

public class CommentsActvivty extends AppCompatActivity implements View.OnClickListener,NetworkCallBack{
    public ListView recyclerView;
    private com.appbook.book.HttpUtils.HttpUtils httpUtils;
    private LinearLayout comment_vg_lyt;
    private LinearLayout mLytCommentVG;
    private NnumberAll nnumberAll;
    private int mesid = 0;
    private NoTouchLinearLayout mLytEdittextVG;
    private EditText mCommentEdittext;
    public static Tencent mTencent;
    private String comment = "";
    public VRefresh swipeRefreshLayout;
    private RequestParams params;
    private RunRankAdapter runRankAdapter;
    private boolean isReply = true;
    private String URL = "/Book/Comments/findmsg.do";
    public RelativeLayout refreshLayout1;
    public RelativeLayout relativeLayout;
    private LinearLayout lyt_comment;
    private Button mSendBut;
    private CheckBox checkbox_anonymous;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("评论");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        mesid = bundle.getInt("mesid");
        nnumberAll = new NnumberAll();
        httpUtils = new com.appbook.book.HttpUtils.HttpUtils(MyRunApplication.getURL(URL),this,new MsgCallBack(this),nnumberAll);
        lyt_comment = (LinearLayout) findViewById(R.id.lyt_comment);
        mLytCommentVG = (LinearLayout) findViewById(R.id.comment_vg_lyt);
        mLytEdittextVG = (NoTouchLinearLayout) findViewById(R.id.edit_vg_lyt);
        mCommentEdittext = (EditText) findViewById(R.id.edit_comment);
        mSendBut = (Button) findViewById(R.id.but_comment_send);
        swipeRefreshLayout = (VRefresh) findViewById(R.id.srlayout);
        recyclerView = (ListView) findViewById(R.id.lidttt);
        swipeRefreshLayout.setView(this,recyclerView);
        refreshLayout1 = (RelativeLayout) findViewById(R.id.refreshLayout1);
        relativeLayout = (RelativeLayout) findViewById(R.id.refreshLayout);
        comment_vg_lyt = (LinearLayout) findViewById(R.id.comment_vg_lyt);
        recyclerView.setDividerHeight(1);
        comment_vg_lyt.setOnClickListener(this);
        mSendBut.setOnClickListener(this);
        lyt_comment.setOnClickListener(this);
        mLytCommentVG.setOnClickListener(this);
        mLytEdittextVG.setOnResizeListener(new NoTouchLinearLayout.OnResizeListener() {
            @Override
            public void OnResize() {
                InputTools.HideKeyboard(mLytEdittextVG);
                mLytEdittextVG.setVisibility(View.GONE);
                mLytCommentVG.setVisibility(View.VISIBLE);
            }
        });
        checkbox_anonymous = (CheckBox) findViewById(R.id.checkbox_anonymous);
        checkbox_anonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"暂未开启",0).show();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        // 设置下拉刷新监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        params = new RequestParams();
                        params.put("id",0);
                        params.put("mesid",mesid);
                        params.put("type",0);
                        httpUtils.FinfMsgRequest(params,false);
                        swipeRefreshLayout.setMoreData(true);
                    }
                }, 0);
            }
        });
        swipeRefreshLayout.setOnLoadListener(new VRefresh.OnLoadListener() {
            @Override
            public void onLoadMore() {
                if(recyclerView.getCount()<10){
                    swipeRefreshLayout.setLoading(false);
                    return;
                }
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        params = new RequestParams();
                        params.put("id",nnumberAll.getCurrentPage());
                        params.put("mesid",mesid);
                        params.put("type",0);
                        httpUtils.FinfMsgRequest(params,true);
                    }
                }, 0);
            }
        });
        params = new RequestParams();
        params.put("id",0);
        params.put("mesid",mesid);
        params.put("type",0);
        httpUtils.FinfMsgRequest(params,false);
        mTencent = Tencent.createInstance("101373848", CommentsActvivty.this);
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
            if(loadingDialog!=null&&loadingDialog.isShowing()){
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
        UserInfo userinfo = new UserInfo(CommentsActvivty.this, mTencent.getQQToken());
        userinfo.getUserInfo(new BaseUIListener("get_user_info",id,token));
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.but_comment_send){
            loadingDialog = new LoadingDialog(CommentsActvivty.this,"评论中•••");
            loadingDialog.setCancelable(false);
            loadingDialog.show();
            isLogin();
        } else if( v.getId()==R.id.lyt_comment){
            isReply = false;
            mLytEdittextVG.setVisibility(View.VISIBLE);
            mLytCommentVG.setVisibility(View.GONE);
            onFocusChange(true);
        } else  if(v.getId()==R.id.refreshLayout){
            refreshLayout1.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            params = new RequestParams();
            params.put("id",0);
            params.put("mesid",mesid);
            params.put("type",0);
            httpUtils.FinfMsgRequest(params,false);
            swipeRefreshLayout.setMoreData(true);
        }

    }

    @Override
    public void onSuccess(final ArrayList arrayList, boolean str, boolean cache) {
        CommentsActvivty.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(arrayList!=null){
                    params = new RequestParams();
                    params.put("id",0);
                    params.put("mesid",mesid);
                    params.put("type",0);
                    httpUtils.FinfMsgRequest(params,false);
                    swipeRefreshLayout.setMoreData(true);
                    recyclerView.setSelection(0);
                    Toast.makeText(getApplicationContext(),((BookResult)arrayList.get(0)).getMsg(),0).show();
                }
                if(loadingDialog!=null&&loadingDialog.isShowing()){
                    loadingDialog.dismiss();
                }
                mCommentEdittext.setText("");
            }
        });
    }

    @Override
    public void onFiled(Throwable throwable, boolean str) {
        CommentsActvivty.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),"评论失败！",0).show();
                if(loadingDialog!=null&&loadingDialog.isShowing()){
                    loadingDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onError() {

    }

    @Override
    protected void onDestroy() {
        com.appbook.book.utils.HttpUtils.Cancelrequest(this);
        super.onDestroy();
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
                params = new RequestParams();
                params.put("id",openid);
                params.put("name",java.net.URLEncoder.encode(tencetLogInInfo.getNickname()));
                params.put("logo",url);
                params.put("token",token);
                params.put("gender",java.net.URLEncoder.encode(tencetLogInInfo.getGender()));
                HttpUtils httpUtils = new HttpUtils(MyRunApplication.getURL("/Book/login/adduser.do"),CommentsActvivty.this,new LogIn(), null);
                httpUtils.AddUserRequest(params,false);
            }else{
                if(loadingDialog!=null&&loadingDialog.isShowing()){
                    loadingDialog.dismiss();
                }
                onError(null);
            }

        }

        @Override
        public void onError(final UiError uiError) {
            startActivity(new Intent(CommentsActvivty.this,LoginActivity.class));
        }

        @Override
        public void onCancel() {
            CommentsActvivty.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"登录取消",0).show();
                }
            });
        }
    }
    class LogIn implements NetworkCallBack {

        @Override
        public void onSuccess(ArrayList arrayList, boolean str, boolean cache) {
            if (isEditEmply()) {        //判断用户是否输入内容
                if (!isReply) {
                    String id = (String)LogInUtils.get(getApplicationContext(),"openid","");
                    // Toast.makeText(getApplicationContext(),java.net.URLDecoder.decode(mCommentEdittext.getText().toString()),0).show();
                    SendRequest.AddMes(CommentsActvivty.this,java.net.URLEncoder.encode(mCommentEdittext.getText().toString()),id,mesid);
                }
                mLytCommentVG.setVisibility(View.VISIBLE);
                mLytEdittextVG.setVisibility(View.GONE);
                onFocusChange(false);
            }
        }

        @Override
        public void onFiled(Throwable throwable, boolean str) {

        }

        @Override
        public void onError() {

        }
    }
    class MsgCallBack implements NetworkCallBack{
        private CommentsActvivty actvivty;
        public MsgCallBack(CommentsActvivty actvivty){
            this.actvivty = new WeakReference<CommentsActvivty>(actvivty).get();
        }
        @Override
        public void onSuccess(final ArrayList arrayList, boolean str, boolean cache) {
            if(refreshLayout1.isShown()){
                refreshLayout1.setVisibility(View.GONE);
            }
            swipeRefreshLayout.setRefreshing(false);
            try{
                swipeRefreshLayout.setLoading(false);
            }catch (Exception e){

            }
            if (arrayList == null) {
                swipeRefreshLayout.setMoreData(false);
                return;
            }
            if(runRankAdapter!=null){
                if(str){
                    if(arrayList.size()>0){
                        if(arrayList.size()<10){
                            swipeRefreshLayout.setMoreData(false);
                        }
                        swipeRefreshLayout.setLoading(false);
                        runRankAdapter.ls.addAll(arrayList);
                    }else{
                        swipeRefreshLayout.setLoading(false);
                        swipeRefreshLayout.setMoreData(false);
                    }
                    runRankAdapter.notifyDataSetChanged();
                    return;
                }
                runRankAdapter.ls = arrayList;
                runRankAdapter.notifyDataSetChanged();
                return;
            }
            runRankAdapter = new RunRankAdapter(actvivty, arrayList);
            recyclerView.setAdapter(runRankAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFiled(Throwable throwable, boolean str) {
            try{
                if(runRankAdapter.getCount()<5){
                    recyclerView.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                }
                if(refreshLayout1.isShown()){
                    refreshLayout1.setVisibility(View.GONE);
                }
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setLoading(false);
            }catch (Exception e){

            }
        }

        @Override
        public void onError() {

        }
    }
    class RunRankAdapter extends BaseAdapter {
        ArrayList<CommentsInfo> ls;
        Context mContext;
        View view;
        private LayoutInflater v ;

        public RunRankAdapter(Context context,
                              ArrayList<CommentsInfo> list) {
            this.v = LayoutInflater.from(context);
            ls = list;
            mContext = context;
        }

        @Override
        public int getCount() {
            return ls.size();
        }

        @Override
        public Object getItem(int position) {
            return ls.get(position);
        }

        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final CommentsInfo appUnit=ls.get(position);
            ViewHolder1 holder = null;
            view = convertView;
            if(view==null){
                holder = new ViewHolder1();
                view = v.inflate(R.layout.comments_litm, null);
                holder.time1 = (ImageView) view.findViewById(R.id.imageView6);
                holder.time = (TextView) view.findViewById(R.id.textView17);
                holder.name = (TextView) view.findViewById(R.id.textView16);
                holder.msg = (TextView) view.findViewById(R.id.content);
                view.setTag(holder);
            }else{
                holder = (ViewHolder1)  view.getTag();
            }
            PicassoUtlis.LoadImage(appUnit.getUser_logo(),holder.time1);
            holder.name.setText(java.net.URLDecoder.decode(appUnit.getUser_name()));
            holder.msg.setText(java.net.URLDecoder.decode(appUnit.getUser_msg()));
            holder.time.setText(DateFormatUtils.getTimesToNow(appUnit.getTime()));
            return view;
        }

        public class ViewHolder1 {
            TextView time;
            TextView name;
            TextView msg;
            ImageView time1;
        }



    }
}
