package com.appbook.book.Thread;

import android.os.Environment;
import android.os.Looper;
import com.appbook.book.BookDetailActivity;
import com.appbook.book.CallBack.DowBookCallBack;
import com.appbook.book.utils.HttpUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by zhangzhongping on 16/10/31.
 */

public class DowThread extends Thread {
    private BookDetailActivity activity;
    private DowBookCallBack dowBookCallBack = null;
    private String url;
    private String name;
    private  long index;
    private int id;
    public DowThread(int id,BookDetailActivity activity, DowBookCallBack dowBookCallBack, String url, String name, long index){
        this.activity = new WeakReference<BookDetailActivity>(activity).get();
        this.dowBookCallBack = dowBookCallBack;
        this.url = url;
        this.name = name;
        this.index = index;
        this.id = id;
    }

    @Override
    public void run() {
        Looper.prepare();
        downLoadFile(url, Environment.getExternalStorageDirectory()+"/AppBook/book/",name+"-dy-"+ HttpUtils.getMD5((id+"").getBytes())+".txt");
        Looper.loop();
    }

    //下载apk程序代码
    public  void downLoadFile(String httpUrl, String path, String name) {
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        file = new File(path+name);
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            dowBookCallBack.onFiled("创建文件失败！");
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        InputStream is = null;
        FileOutputStream fos =null;
        try {
                URL url = new URL(httpUrl);
                conn = (HttpURLConnection) url.openConnection();
                is = conn.getInputStream();
                fos = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                conn.connect();
                double count = 0;
                if (conn.getResponseCode() >= 400) {
                    dowBookCallBack.onFiled("下载失败！");
                } else {
                    while (count <= 100) {
                        if (is != null) {
                            int numRead = is.read(buf);
                            if (numRead <= 0) {
                                break;
                            } else {
                                dowBookCallBack.onProces(file.length(),index);
                                fos.write(buf, 0, numRead);
                            }
                        } else {
                            break;
                        }
                    }
                    dowBookCallBack.onSuccess("下载完成！",file.getAbsolutePath(),name.replace(".txt",""));
                }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            dowBookCallBack.onFiled("下载失败！");
            e.printStackTrace();
        }finally {
            try{
                if(conn!=null){
                    conn.disconnect();
                }
                if(fos!=null){
                    fos.close();
                }
                if(is!=null){
                    is.close();
                }
            }catch (Exception e){
                dowBookCallBack.onFiled("下载失败！");
            }
        }


    }
}
