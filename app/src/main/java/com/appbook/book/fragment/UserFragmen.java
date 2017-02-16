package com.appbook.book.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.appbook.book.BookDetailActivity;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.CommentsActvivty;
import com.appbook.book.HttpUtils.HttpUtils;
import com.appbook.book.HttpUtils.SendRequest;
import com.appbook.book.MyRunApplication;
import com.appbook.book.R;
import com.appbook.book.User.LoginActivity;
import com.appbook.book.ViewUtils.LoadingDialog;
import com.appbook.book.ViewUtils.RefreshLayout;
import com.appbook.book.entity.BookResult;
import com.appbook.book.entity.CommentsInfo;
import com.appbook.book.entity.LogInUserInfo;
import com.appbook.book.entity.NnumberAll;
import com.appbook.book.utils.DateFormatUtils;
import com.appbook.book.utils.InputTools;
import com.appbook.book.utils.cache.LogInUtils;
import com.appbook.book.widget.AlertDialog;
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

import okhttp3.OkHttpClient;

/**
 * Created by zhangzhongping on 16/12/16.
 */

public class UserFragmen extends Fragment implements View.OnClickListener ,NetworkCallBack{
    public View view =  null;
    public ListView recyclerView;
    public com.appbook.book.HttpUtils.HttpUtils httpUtils;
    public NnumberAll nnumberAll;
    public static Tencent mTencent;
    public String comment = "";
    public VRefresh swipeRefreshLayout;
    public RequestParams params;
    public RunRankAdapter runRankAdapter;
    private LinearLayout lyt_comment;
    public boolean isReply = true;
    public String URL = "/Book/Comments/findmsg.do";
    private AlertDialog alertDialog;
    public int ids;
    private LoadingDialog loadingDialog;
    public static UserFragmen newInstance(int id) {
        UserFragmen fragment = new UserFragmen();
        fragment.ids = id;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.activity_user, container, false);
            initControls(view);
        }
        return view;
    }
    /**
     * 初始化各控件
     * @param view
     */
    private void initControls(View view) {
        mTencent = Tencent.createInstance("101373848", getContext());
        alertDialog = new WeakReference<AlertDialog>(new AlertDialog(getContext(),this).builder().setCancelable(false)).get();
        nnumberAll = new NnumberAll();
        httpUtils = new com.appbook.book.HttpUtils.HttpUtils(MyRunApplication.getURL(URL),getContext(),new MsgCallBack(this),nnumberAll);
        swipeRefreshLayout = (VRefresh) view.findViewById(R.id.srlayout);
        recyclerView = (ListView) view.findViewById(R.id.lidttt);
        swipeRefreshLayout.setView(getContext(),recyclerView);
        recyclerView.setDividerHeight(1);
        lyt_comment = (LinearLayout) view.findViewById(R.id.lyt_comment);
        lyt_comment.setOnClickListener(this);
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
                        params.put("mesid",ids);
                        params.put("type",1);
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
                        params.put("mesid",ids);
                        params.put("type",1);
                        httpUtils.FinfMsgRequest(params,true);
                    }
                }, 0);
            }
        });
        params = new RequestParams();
        params.put("id",0);
        params.put("mesid",ids);
        params.put("type",1);
        httpUtils.FinfMsgRequest(params,false);
    }
    /**
     * 显示或隐藏输入法
     */
    public void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        alertDialog.mCommentEdittext.getContext().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    alertDialog. mCommentEdittext.requestFocus();//获取焦点
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                } else {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(alertDialog.mCommentEdittext.getWindowToken(), 0);
                }
            }
        }, 100);
    }
    /**
     * 判断对话框中是否输入内容
     */
    private boolean isEditEmply() {
        comment = alertDialog.mCommentEdittext.getText().toString().trim();
        if (comment.equals("")) {
            if(loadingDialog!=null&&loadingDialog.isShowing()){
                loadingDialog.dismiss();
            }
            Toast.makeText(getContext(), "评论不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private static void initOpenidAndToken(String token,String expires,String openId) {
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                && !TextUtils.isEmpty(openId)) {
            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openId);
        }
    }
    public void isLogin(){
        loadingDialog = new LoadingDialog(UserFragmen.this.getActivity(),"评论中•••");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        String token =  (String) LogInUtils.get(getContext(),"QQToken","");
        String id = (String)LogInUtils.get(getContext(),"openid","");
        String expires = (String)LogInUtils.get(getContext(),"expires","");
        initOpenidAndToken(token,expires,id);
        UserInfo userinfo = new UserInfo(getContext(), mTencent.getQQToken());
        userinfo.getUserInfo(new BaseUIListener("get_user_info",id,token));
    }
    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.lyt_comment){
            alertDialog.show();
        }
    }
    @Override
    public void onSuccess(final ArrayList arrayList, boolean str, boolean cache) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(arrayList!=null){
                    params = new RequestParams();
                    params.put("id",0);
                    params.put("mesid",ids);
                    params.put("type",1);
                    httpUtils.FinfMsgRequest(params,false);
                    swipeRefreshLayout.setMoreData(true);
                    recyclerView.setSelection(0);
                    Toast.makeText(getContext(),((BookResult)arrayList.get(0)).getMsg(),0).show();
                }
                if(loadingDialog!=null&&loadingDialog.isShowing()){
                    loadingDialog.dismiss();
                }
                alertDialog.mCommentEdittext.setText("");
            }
        });
    }
    @Override
    public void onFiled(Throwable throwable, boolean str) {
        UserFragmen.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),"服务器错误",0).show();
                if(loadingDialog!=null&&loadingDialog.isShowing()){
                    loadingDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onError() {

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
                LogInUtils.put(getContext(),"QQName", tencetLogInInfo.getNickname());
                LogInUtils.put(getContext(),"QQLogo", url);
                params = new RequestParams();
                params.put("id",openid);
                params.put("name",java.net.URLEncoder.encode(tencetLogInInfo.getNickname()));
                params.put("logo",url);
                params.put("token",token);
                params.put("gender",java.net.URLEncoder.encode(tencetLogInInfo.getGender()));
                HttpUtils httpUtils = new HttpUtils(MyRunApplication.getURL("/Book/login/adduser.do"),getContext(),new LogIn(), null);
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
            startActivity(new Intent(getActivity(),LoginActivity.class));
        }

        @Override
        public void onCancel() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),"登录取消",0).show();
                }
            });
        }
    }
    class LogIn implements NetworkCallBack {

        @Override
        public void onSuccess(ArrayList arrayList, boolean str, boolean cache) {
            if (isEditEmply()) {        //判断用户是否输入内容
                if (!isReply) {
                    String id = (String)LogInUtils.get(getContext(),"openid","");
                    // Toast.makeText(getContext(),java.net.URLDecoder.decode(mCommentEdittext.getText().toString()),0).show();
                    SendRequest.AddMes(UserFragmen.this,java.net.URLEncoder.encode(alertDialog.mCommentEdittext.getText().toString()),id,ids);
                }
                onFocusChange(false);
                if(alertDialog.dialog!=null){
                    alertDialog.dialog.dismiss();
                }
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
        private UserFragmen actvivty;
        public MsgCallBack(UserFragmen actvivty){
            this.actvivty = new WeakReference<UserFragmen>(actvivty).get();
        }
        @Override
        public void onSuccess(final ArrayList arrayList, boolean str, boolean cache) {
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
            runRankAdapter = new RunRankAdapter(actvivty.getContext(), arrayList);
            recyclerView.setAdapter(runRankAdapter);
        }

        @Override
        public void onFiled(Throwable throwable, boolean str) {
            try{
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
