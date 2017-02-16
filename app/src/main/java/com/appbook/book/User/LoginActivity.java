package com.appbook.book.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.HttpUtils.HttpUtils;
import com.appbook.book.MyRunApplication;
import com.appbook.book.R;
import com.appbook.book.TotalMainActivity;
import com.appbook.book.ViewUtils.LoadingDialog;
import com.appbook.book.entity.BookResult;
import com.appbook.book.entity.LogInUserInfo;
import com.appbook.book.entity.TencetLogInInfo;
import com.appbook.book.fragment.main2.MessageIndexctivity;
import com.appbook.book.utils.cache.LogInUtils;
import com.justwayward.reader.view.LoginPopupWindow;
import com.loopj.android.http.RequestParams;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/11/2.
 */

public class LoginActivity extends Activity implements LoginPopupWindow.LoginTypeListener,NetworkCallBack{
    public static Tencent mTencent;
    public IUiListener loginListener;
    LoginPopupWindow popupWindow = null;
    public LoadingDialog loadingDialog = null;
    private RequestParams requestParams;
    private Activity mActivity = null;
    private final String msg = "当前功能需要登录！是否进行登录？";
    private final String msgs = "登录已过期!请重新登录!";
    private String index = null;
    /** 初始化openid和token***/
    private static void initOpenidAndToken(String token,String expires,String openId) {
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                && !TextUtils.isEmpty(openId)) {
            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openId);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            System.gc();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ccccccc);
        mActivity = this;
        mTencent = Tencent.createInstance("101373848", LoginActivity.this);
        //initOpenidAndToken();
        if(isLogin()){
            index = msgs;
        }else{
            index = msg;
        }
        AlertDialog  actionSheetDialog = new AlertDialog(this);
        actionSheetDialog.builder().setCancelable(false).setTitle("提示信息").setMsg(index).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.gc();
                finish();
            }
        }).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow == null) {
                    popupWindow = new LoginPopupWindow(LoginActivity.this);
                    popupWindow.setLoginTypeListener(LoginActivity.this);
                }
                popupWindow.showAtLocation(findViewById(R.id.liii), Gravity.CENTER, 0, 0);
            }
        }).show();
    }
    public boolean isLogin(){
        String token =  (String)LogInUtils.get(getApplicationContext(),"QQToken","");
        String id = (String)LogInUtils.get(getApplicationContext(),"openid","");
        String expires = (String)LogInUtils.get(getApplicationContext(),"expires","");
        String name = (String)LogInUtils.get(getApplicationContext(),"QQName","");
        String logo =  (String)LogInUtils.get(getApplicationContext(),"QQLogo","");
        if(TextUtils.isEmpty(token)||TextUtils.isEmpty(id)||TextUtils.isEmpty(expires)||TextUtils.isEmpty(name)||TextUtils.isEmpty(logo)){
            return false;
        }
        return true;
    }
    @Override
    public void onLogin(PopupWindow popupWindow, ImageView view, String type) {
        if (type.equals("QQ")) {
            if (!mTencent.isSessionValid()) {
                if (loginListener == null) loginListener = new BaseUIListener("login","","");
                mTencent.login(this, "all", loginListener);
            }
        }
    }

    @Override
    public void onSuccess(ArrayList arrayList, boolean str, boolean cache) {
        if(loadingDialog!=null&&loadingDialog.isShowing()&&mActivity!=null&&!mActivity.isFinishing()){
            loadingDialog.dismiss();
        }
        final BookResult arrayList1 = ((BookResult)arrayList.get(0));
        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), arrayList1.getMsg(),0).show();
            }
        });
        System.gc();
        finish();
    }

    @Override
    public void onFiled(Throwable throwable, boolean str) {
        if(loadingDialog!=null&&loadingDialog.isShowing()&&mActivity!=null&&!mActivity.isFinishing()){
            loadingDialog.dismiss();
        }
        mTencent.logout(getApplicationContext());
        LogInUtils.remove(getApplicationContext(),"QQToken");
        LogInUtils.remove(getApplicationContext(),"openid");
        LogInUtils.remove(getApplicationContext(),"expires");
        LogInUtils.remove(getApplicationContext(),"QQName");
        LogInUtils.remove(getApplicationContext(),"QQLogo");
        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "登录失败",0).show();
            }
        });
        System.gc();
        finish();
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
            if(scope.equals("login")){
                JSONObject jsonObject = (JSONObject) o;
                String json = jsonObject.toString();
                TencetLogInInfo tencetLogInInfo = JSON.parseObject(json, TencetLogInInfo.class);
                if (tencetLogInInfo!=null&&tencetLogInInfo.getRet()== 0) {
                    if(LogInUtils.contains(getApplicationContext(),"QQToken")){
                        LogInUtils.remove(getApplicationContext(),"QQToken");
                    }
                    if(LogInUtils.contains(getApplicationContext(),"openid")){
                        LogInUtils.remove(getApplicationContext(),"openid");
                    }
                    if(LogInUtils.contains(getApplicationContext(),"expires")){
                        LogInUtils.remove(getApplicationContext(),"expires");
                    }
                    LogInUtils.put(getApplicationContext(),"QQToken", tencetLogInInfo.getAccess_token());
                    LogInUtils.put(getApplicationContext(),"expires", tencetLogInInfo.getExpires_in()+"");
                    LogInUtils.put(getApplicationContext(),"openid", tencetLogInInfo.getOpenid());
                    initOpenidAndToken(tencetLogInInfo.getAccess_token(),tencetLogInInfo.getExpires_in()+"",tencetLogInInfo.getOpenid());
                    UserInfo userinfo = new UserInfo(LoginActivity.this, mTencent.getQQToken());
                    userinfo.getUserInfo(new BaseUIListener("get_user_info",tencetLogInInfo.getOpenid(),tencetLogInInfo.getAccess_token()));
                }else{
                    onError(null);
                }
            }else{
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
                    HttpUtils httpUtils = new HttpUtils(MyRunApplication.getURL("/Book/login/adduser.do"),LoginActivity.this,LoginActivity.this, null);
                    httpUtils.AddUserRequest(requestParams,false);
                }else{
                    onError(null);
                }
            }
        }

        @Override
        public void onError(final UiError uiError) {
            if(loadingDialog!=null&&loadingDialog.isShowing()&&mActivity!=null&&!mActivity.isFinishing()){
                loadingDialog.dismiss();
            }
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(uiError==null){
                            Toast.makeText(getApplicationContext(), "登录错误!获取用户信息错误!",0).show();
                            return;
                        }
                        Toast.makeText(getApplicationContext(), uiError.errorMessage,0).show();
                    }
                });
            System.gc();
            finish();
        }

        @Override
        public void onCancel() {
            if(loadingDialog!=null&&loadingDialog.isShowing()&&mActivity!=null&&!mActivity.isFinishing()){
                loadingDialog.dismiss();
            }
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"登录取消",0).show();
                        }
                    });
            System.gc();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        mActivity = null;
        com.appbook.book.utils.HttpUtils.Cancelrequest(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            if(popupWindow!=null){
                popupWindow.pw.dismiss();
            }
            loadingDialog = new LoadingDialog(LoginActivity.this,"登录中•••");
            loadingDialog.setCancelable(false);
            loadingDialog.show();
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
