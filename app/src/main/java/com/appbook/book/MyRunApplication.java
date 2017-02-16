package com.appbook.book;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.appbook.book.updata.Crash.CrashHandler;
import com.appbook.book.updata.manager.UpdateManager;
import com.justwayward.reader.base.Constant;
import com.justwayward.reader.component.AppComponent;
import com.justwayward.reader.component.DaggerAppComponent;
import com.justwayward.reader.module.AppModule;
import com.justwayward.reader.module.BookApiModule;
import com.justwayward.reader.utils.AppUtils;
import com.justwayward.reader.utils.SharedPreferencesUtil;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


/**
 * Created by zhangzhongping on 16/10/31.
 */

public class MyRunApplication extends Application {
    public static Context context;
    public static long TTME = 0L;
    private static String URL = "https://www.gameqj.xyz";
    private static MyRunApplication sInstance;
    private AppComponent appComponent;
    public static UpdateManager updateManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        //在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
        updateManager = new UpdateManager(this);
        this.sInstance = this;
        initCompoent();
        AppUtils.init(this);
       // CrashHandler.getInstance().init(this);
        initPrefs();
        initNightMode();
        MyRunApplication.context = getApplicationContext();
        //initHciCloud();
       // TbsDownloader.needDownload(getApplicationContext(), true);
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                Log.e("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub

            }
        };
        QbSdk.initX5Environment(getApplicationContext(),  cb);
        File file = new File(Environment.getExternalStorageDirectory()+"/AppBook/");
        file.mkdirs();
        //copyFolder("/data/data/com.appbook.book/Crashlog",Environment.getExternalStorageDirectory()+"/AppBook");
    }
    public static String getURL(String url){
        return URL+url;
    }
    public static long getTTME (){
        return TTME;
    }
    public static void setTTME(long time){
        TTME = time;
    }
    public static Context getAppContext() {
        return MyRunApplication.context;
    }
    public static MyRunApplication getsInstance() {
        return sInstance;
    }



    private void initCompoent() {
        appComponent = DaggerAppComponent.builder()
                .bookApiModule(new BookApiModule())
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    /**
     * 初始化SharedPreference
     */
    protected void initPrefs() {
        SharedPreferencesUtil.init(getApplicationContext(), getPackageName() + "_preference", Context.MODE_MULTI_PROCESS);
    }

    protected void initNightMode() {
        boolean isNight = SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       // MultiDex.install(this);
    }

}

//43 81 16 44 45 70 4 71 21 3 82 56 77 23 76 58 57 63 65 11 22 29 80 75 79 72 78 64 73 28 34 15
///user/signin/ANDROID/2.0?cat_id=43&user_id=261755&app_version=3.5.1.57&platform=2&market_id=tool_tencent&device_code=%5Bw%5D50%3Aa7%3A2b%3A70%3A05%3Aa9-%5Bi%5D867601020331366-%5Bs%5D89860023031471435768&versioncode=169&_key=CE435F394A68BA85B1AE930305A6483905C46F894D36DA343B0699A46EE6D73DEBB5170DD8A2EE30D94080CDC13906EE3632F6AD27F2E6DC
///user/signin/ANDROID/2.0?cat_id=81&user_id=261755&app_version=3.5.1.57&platform=2&market_id=tool_tencent&device_code=%5Bw%5D50%3Aa7%3A2b%3A70%3A05%3Aa9-%5Bi%5D867601020331366-%5Bs%5D89860023031471435768&versioncode=169&_key=CE435F394A68BA85B1AE930305A6483905C46F894D36DA343B0699A46EE6D73DEBB5170DD8A2EE30D94080CDC13906EE3632F6AD27F2E6DC