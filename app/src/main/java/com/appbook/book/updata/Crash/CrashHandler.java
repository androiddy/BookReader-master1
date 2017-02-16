package com.appbook.book.updata.Crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.appbook.book.entity.CrachInfo;
import com.justwayward.reader.utils.LogUtils;
import com.justwayward.reader.utils.ToastUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangzhongping on 16/12/19.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;

    private static final String PATH = Environment.getExternalStorageDirectory()+"/AppBook/Crashlog/";
    private static final String FILE_NAME = "crash";

    //log文件的后缀名
    private static final String FILE_NAME_SUFFIX = ".crash";

    private static CrashHandler sInstance = new CrashHandler();

    //系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private Context mContext;


    //构造方法私有，防止外部构造多个实例，即采用单例模式
    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    //这里主要完成初始化工作
    public void init(Context context) {
        //获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        //获取Context，方便内部使用
        mContext = context.getApplicationContext();
    }


    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            //导出异常信息到SD卡中
            dumpExceptionToSDCard(ex);
            //这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug
            uploadExceptionToServer(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    ToastUtils.showSingleToast("哎呀，程序发生异常啦...");
                    Looper.loop();
                }
            }).start();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LogUtils.e("CrashHandler.InterruptedException--->" + e.toString());
            }
            mDefaultCrashHandler.uncaughtException(thread, ex);
    }

    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        PrintWriter pw = null;
        try {
            File file = new File(PATH + FILE_NAME + System.nanoTime() + FILE_NAME_SUFFIX);
            if(!file.exists()){
                file.createNewFile();
            }
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(InitCrach(ex,false));
            pw.println();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "dump crash info failed");
        }finally {
            if(pw!=null){
                pw.flush();
                pw.close();
            }
        }
    }
    private Object InitCrach(Throwable ex,boolean str) throws PackageManager.NameNotFoundException {
        CrachInfo  crachInfo = new CrachInfo();
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.flush();
        printWriter.close();
        String result = writer.toString();
        crachInfo.setTime(time);
        crachInfo.setCrachmsg(result);
        dumpPhoneInfo(crachInfo);
        if(str){
            return crachInfo;
        }
        Object json = JSON.toJSON(crachInfo);
        return json;
    }
    private void dumpPhoneInfo(CrachInfo pw) throws PackageManager.NameNotFoundException {
        //应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.setAppvarname(pi.versionName);
        pw.setAppvar(String.valueOf(pi.versionCode));
        //android版本号
        pw.setAndroidvarname(Build.VERSION.RELEASE);
        pw.setAndroidvar(String.valueOf(Build.VERSION.SDK_INT));
        //手机制造商
        pw.setPhonebrand(Build.MANUFACTURER);
        //手机型号
        pw.setPhonemodels(Build.MODEL);
        //cpu架构
        pw.setCpuabi(Build.CPU_ABI);
    }

    private void uploadExceptionToServer(Throwable ex) {

    }

}
