package com.appbook.book;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.HttpUtils.HttpUtils;
import com.appbook.book.ViewUtils.LoadingDialog;
import com.appbook.book.ViewUtils.PagerTabLayout;
import com.appbook.book.entity.BookResult;
import com.appbook.book.entity.LogInUserInfo;
import com.appbook.book.entity.TencetLogInInfo;
import com.appbook.book.fragment.UserFragmen;
import com.appbook.book.fragment.main.*;
import com.appbook.book.fragment.main2.DowListActivity;
import com.appbook.book.fragment.main2.MessageListctivity;
import com.appbook.book.updata.Utils.Toasts;
import com.appbook.book.updata.manager.UpdateManager;
import com.appbook.book.utils.cache.LogInUtils;
import com.appbook.book.widget.CircleImageView;
import com.appbook.book.widget.NoScrollViewPager;
import com.appbook.book.widget.PicassoUtlis;
import com.appbook.book.widget.XCRoundImageView;
import com.cwvs.microlife.searchview.SearchActivity;
import com.justwayward.reader.bean.base.Base;
import com.justwayward.reader.view.LoginPopupWindow;
import com.loopj.android.http.RequestParams;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import me.majiajie.pagerbottomtabstrip.Controller;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectListener;

/**
 * Created by zhangzhongping on 16/11/2.
 */

public class TotalMainActivity extends AppCompatActivity implements NetworkCallBack,LoginPopupWindow.LoginTypeListener, OnTabItemSelectListener,ViewPager.OnPageChangeListener {
    private NoScrollViewPager mViewPager;
    private PagerTabLayout bottomTabLayout;
    private com.appbook.book.ViewUtils.CircleImageView xcRoundImageView;
    private TextView id_username;
    public static Tencent mTencent;
    public LoadingDialog loadingDialog = null;
    private RequestParams requestParams;
    public IUiListener loginListener;
    LoginPopupWindow popupWindow = null;
    private  Controller controller;
    private  Toolbar mToolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigation_view;
    public boolean strs = false;
    public Button btn_master_sh;
    private Handler handler = new Handler();
    Runnable runnable;
    private IUiListener baseUIListener = null;
    @Override
    protected void onResume() {
        super.onResume();
        String token = (String) LogInUtils.get(getApplicationContext(), "QQToken", "");
        String id = (String) LogInUtils.get(getApplicationContext(), "openid", "");
        String expires = (String) LogInUtils.get(getApplicationContext(), "expires", "");
        initOpenidAndToken(token, expires, id);
        UserInfo userinfo = new UserInfo(TotalMainActivity.this, mTencent.getQQToken());
        baseUIListener = new BaseUIListener("get_user_info", id, token);
        userinfo.getUserInfo(baseUIListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_main);
        MyRunApplication.updateManager.checkUpdate(this,false);
        mTencent = Tencent.createInstance("101373848", TotalMainActivity.this);
        InitView();
        //startActivity(new Intent(this,LoginActivity.class));
    }

    /**初始化openid和token***/
    private static void initOpenidAndToken(String token,String expires,String openId) {
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                && !TextUtils.isEmpty(openId)) {
            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openId);
        }
    }
    private void InitView(){
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mViewPager = (NoScrollViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.addOnPageChangeListener(this);
        bottomTabLayout = (PagerTabLayout) findViewById(R.id.tab);
        controller =  bottomTabLayout.builder()
                .addTabItem(R.mipmap.maintab_bookstand_icon, "书架")
                .addTabItem(R.mipmap.maintab_stack_icon, "书库")
                .addTabItem(R.mipmap.maintab_stack_zhuanti, "专题")
                .addTabItem(R.mipmap.zixun, "资讯")
                .build();
        controller.addTabItemClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawlayout);
        navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                int id = item.getItemId();
                item.setChecked(true);
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        item.setChecked(false);
                    }
                };
                handler.postDelayed(runnable,100);
                switch (id) {
                    case R.id.navigation_sub_item_2:
                        mTencent.logout(getApplicationContext());
                        Intent intent=new Intent();
                        intent.setAction("login_out");
                        sendOrderedBroadcast(intent, null, new FinalRecivey(), null, 0, null, null);
                        break;
                    case R.id.navigation_item_message:
                        Toasts.ToastInfo(getApplicationContext(),"检查更新中..");
                        MyRunApplication.updateManager.checkUpdate(TotalMainActivity.this,true);
                        break;
                    case R.id.navigation_item_discussion:
                        ScanLocalBookActivity.startActivity(TotalMainActivity.this);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        View drawview = navigation_view.inflateHeaderView(R.layout.drawer_header);
        btn_master_sh = (Button) drawview.findViewById(R.id.btn_master_shs);
        id_username = (TextView) drawview.findViewById(R.id.id_username);
        xcRoundImageView = (com.appbook.book.ViewUtils.CircleImageView) drawview.findViewById(R.id.XCRoundImageView);
        btn_master_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTencent.logout(getApplicationContext());
                if (popupWindow == null) {
                    popupWindow = new LoginPopupWindow(TotalMainActivity.this);
                    popupWindow.setLoginTypeListener(TotalMainActivity.this);
                }
                popupWindow.showAtLocation(mToolbar, Gravity.CENTER, 0, 0);
            }
        });
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.open, R.string.close);
        // 添加抽屉按钮，通过点击按钮实现打开和关闭功能; 如果不想要抽屉按钮，只允许在侧边边界拉出侧边栏，可以不写此行代码
        mDrawerToggle.syncState();
        // 设置按钮的动画效果; 如果不想要打开关闭抽屉时的箭头动画效果，可以不写此行代码
        drawerLayout.setDrawerListener(mDrawerToggle);

        setupViewPager(mViewPager);
        IntLogInViewInfo();
    }
    private void setupViewPager(ViewPager mViewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(Mains1Activity.newInstance(),"");
        adapter.addFragment(com.appbook.book.fragment.main.MainActivity.newInstance(),"");
        adapter.addFragment(DowListActivity.newInstance(),"");
        adapter.addFragment(MessageListctivity.newInstance(),"");
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onSelected(int index, Object tag) {
        switch (index){
            case 0:
                mViewPager.setCurrentItem(0);
                break;
            case 1:
                mViewPager.setCurrentItem(1);
                break;
            case 2:
                mViewPager.setCurrentItem(2);
                break;
            case 3:
                mViewPager.setCurrentItem(3);
                break;
        }
    }

    @Override
    public void onRepeatClick(int index, Object tag) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        controller.setSelect(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onLogin(final PopupWindow popupWindow, ImageView view, String type) {
        if (type.equals("QQ")) {
            if (!mTencent.isSessionValid()) {
                if (loginListener == null) {
                    loginListener = new BaseUIListener("login","","");
                }
                strs = true;
                mTencent.login(this, "all", loginListener);
            }
        }
    }

    @Override
    public void onSuccess(final ArrayList arrayList, boolean str, boolean cache) {
        if(loadingDialog!=null){
            loadingDialog.dismiss();
        }
        if(arrayList==null){
            onFiled(null,false);
            return;
        }
        final BookResult arrayList1 = ((BookResult)arrayList.get(0));
        TotalMainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(strs){
                    Toast.makeText(getApplicationContext(), arrayList1.getMsg(),0).show();
                    strs  = false;
                }

            }
        });
        if(arrayList1.getStatus()==1){
            arrayList1.getData();
            com.alibaba.fastjson.JSONObject jsonArray = JSON.parseObject( arrayList1.getData().toString());
            com.appbook.book.entity.UserInfo arrayLists = (com.appbook.book.entity.UserInfo) JSON.parseObject(jsonArray.toString(), com.appbook.book.entity.UserInfo.class);
            Intent intent=new Intent();
            intent.setAction("login_Success");
            intent.putExtra("name", java.net.URLDecoder.decode(arrayLists.getUser_name()));
            intent.putExtra("logo", arrayLists.getUser_logo());
            sendOrderedBroadcast(intent, null, new FinalRecivey(), null, 0, null, null);
        }else {
            onFiled(null,false);
        }
    }
    @Override
    public void onFiled(Throwable throwable, boolean str) {
        if(loadingDialog!=null){
            loadingDialog.dismiss();
        }
        mTencent.logout(getApplicationContext());
        LogInUtils.remove(getApplicationContext(),"QQToken");
        LogInUtils.remove(getApplicationContext(),"openid");
        LogInUtils.remove(getApplicationContext(),"expires");
        LogInUtils.remove(getApplicationContext(),"QQName");
        LogInUtils.remove(getApplicationContext(),"QQLogo");
        TotalMainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(strs){
                    Toast.makeText(getApplicationContext(), "登录失败",0).show();
                    strs  = false;
                }
            }
        });
    }

    @Override
    public void onError() {

    }

    public void IntLogInViewInfo(){
    String name = (String)LogInUtils.get(getApplicationContext(),"QQName","");
    String logo =  (String)LogInUtils.get(getApplicationContext(),"QQLogo","");
    if(TextUtils.isEmpty(name)||TextUtils.isEmpty(logo)){
        return;
    }
    btn_master_sh.setVisibility(View.GONE);
    id_username.setVisibility(View.VISIBLE);
    id_username.setText(name);
    PicassoUtlis.LoadImage(logo,xcRoundImageView);
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
                    UserInfo userinfo = new UserInfo(TotalMainActivity.this, mTencent.getQQToken());
                    userinfo.getUserInfo(new BaseUIListener("get_user_info",tencetLogInInfo.getOpenid(),tencetLogInInfo.getAccess_token()));
                }else{
                    onError(null);
                }
            }else{
                JSONObject jsonObject = (JSONObject) o;
                String json = jsonObject.toString();
                final LogInUserInfo tencetLogInInfo = JSON.parseObject(json, LogInUserInfo.class);
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
                    HttpUtils httpUtils = new HttpUtils(MyRunApplication.getURL("/Book/login/adduser.do"),TotalMainActivity.this,TotalMainActivity.this, null);
                    httpUtils.AddUserRequest(requestParams,false);
                }else{
                    onError(null);
                }
            }

        }

        @Override
        public void onError(final UiError uiError) {
            if(loadingDialog!=null){
                loadingDialog.dismiss();
            }
            TotalMainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!strs){
                        String name = (String)LogInUtils.get(getApplicationContext(),"QQName","");
                        String logo =  (String)LogInUtils.get(getApplicationContext(),"QQLogo","");
                        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(logo)){
                            strs  = false;
                            return;
                        }
                        Intent intent=new Intent();
                        intent.setAction("login_Success");
                        intent.putExtra("name", (String)LogInUtils.get(getApplicationContext(),"QQName",""));
                        intent.putExtra("logo", (String)LogInUtils.get(getApplicationContext(),"QQLogo",""));
                        sendOrderedBroadcast(intent, null, new FinalRecivey(), null, 0, null, null);
                        strs = false;
                        return;
                    }
                    if(uiError==null){
                        Toast.makeText(getApplicationContext(), "登录失败！获取用户信息失败！",0).show();
                        strs = false;
                        return;
                    }
                    strs = false;
                    Toast.makeText(getApplicationContext(), uiError.errorMessage,0).show();
                }
            });
        }

        @Override
        public void onCancel() {
            if(loadingDialog!=null){
                loadingDialog.dismiss();
            }
            strs = false;
            TotalMainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"登录取消",0).show();
                }
            });
        }
    }
    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private long firstTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(xcRoundImageView.isShown()){
                drawerLayout.closeDrawers();
                return true;
            }
            if(mViewPager.getCurrentItem()!=0){
             //   mViewPager.setCurrentItem(0);
                controller.setSelect(0);
                return true;
            }
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {                                         //如果两次按键时间间隔大于2秒，则不退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;//更新firstTime
                return true;
            } else {                                                    //两次按键小于2秒时，退出应用
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                Intent intent = new Intent(TotalMainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            if(popupWindow!=null){
                popupWindow.pw.dismiss();
            }
            loadingDialog = new LoadingDialog(TotalMainActivity.this,"登录中•••");
            loadingDialog.setCancelable(false);
            loadingDialog.show();
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
   class FinalRecivey extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getAction();
            if(msg.equals("login_Success")){
                //新页面接收数据
                Bundle bundle = intent.getExtras();
                //接收name值
                String name = bundle.getString("name");
                String logo = bundle.getString("logo");
                btn_master_sh.setVisibility(View.GONE);
                id_username.setVisibility(View.VISIBLE);
                id_username.setText(name);
                PicassoUtlis.LoadImage(logo,xcRoundImageView);
            }else  if(msg.equals("login_out")){
                LogInUtils.clear(getApplicationContext());
                TotalMainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn_master_sh.setVisibility(View.VISIBLE);
                        id_username.setVisibility(View.GONE);
                        xcRoundImageView.setImageResource(R.mipmap.icon);
                    }
                });
                Toasts.ToastInfo(getApplicationContext(),"退出成功");
            }
        }
    }
}
