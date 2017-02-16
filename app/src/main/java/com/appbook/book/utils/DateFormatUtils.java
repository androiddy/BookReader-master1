package com.appbook.book.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.appbook.book.CallBack.DowPngCallBack;
import com.appbook.book.MyRunApplication;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 封装的日期计算工具类
 */
public class DateFormatUtils {
    private static DateFormat dateFormat = new SimpleDateFormat("yy年MM月dd日");
    private static  String path =   Environment.getExternalStorageDirectory()+"/AppBook/";
    private static  String paths =   Environment.getExternalStorageDirectory()+"/AppBook/image/";
    static File myCaptureFile = null;
    /*
    获取时间差
     */
    public static String getTimesToNow(long date) {
        String result;
        int minute = 1000 * 60;
        int hour = minute * 60;
        int day = hour * 24;
        int halfamonth = day * 15;
        int month = day * 30;
        long now = new Date().getTime();
        long diffValue = now - date;
        if (diffValue < 0) {
            return "刚刚";
        }
        long monthC = diffValue / month;
        long weekC = diffValue / (7 * day);
        long dayC = diffValue / day;
        long hourC = diffValue / hour;
        long minC = diffValue / minute;
         if (weekC >= 1||dayC>=3) {
            result = dateFormat.format(new Date(date));
        } else if (dayC >= 1&&dayC<3) {
            result = "" + dayC + "天前";
        } else if (hourC >= 1) {
            result = "" + hourC + "小时前";
        } else if (minC >= 1) {
            result = "" + minC + "分钟前";
        } else
            result = "刚刚";
        return result;
    }

    /**
     * Get image from newwork
     * @param path The path of image
     * @return byte[]
     * @throws Exception
     */
    private static byte[] getImage(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            return readStream(inStream);
        }
        return null;
    }
    public static void IsDowIamge(String url,DowPngCallBack dow){
        Bitmap bitm = null;
        try {
            byte[] b = getImage(url);
            if( b!=null){
                bitm = BitmapFactory.decodeByteArray(b, 0, b.length);// bitmap
                saveFile(url,bitm,isImageType(b),b);
                if(myCaptureFile.exists()){
                    dow.onSuccess("图片保存至"+paths+"文件夹");
                }else{
                    dow.onError("保存错误!");
                }
            }else{
                dow.onError("保存错误!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            dow.onError("保存错误!");
        }finally {
            if(bitm!=null){
                bitm.recycle();
            }
        }
    }
    private static int isImageType(byte[] b){
        int type = 0;
        byte b0 = b[0];
        byte b1 = b[1];
        byte b2 = b[2];
        byte b3 = b[3];
        byte b6 = b[6];
        byte b7 = b[7];
        byte b8 = b[8];
        byte b9 = b[9];
// GIF
        if (b0 == (byte) 'G' && b1 == (byte) 'I' && b2 == (byte) 'F')
            type = 1;
// PNG
        else if (b1 == (byte) 'P' && b2 == (byte) 'N' && b3 == (byte) 'G')
            type = 2;
// JPG
        else if (b6 == (byte) 'J' && b7 == (byte) 'F' && b8 == (byte) 'I' && b9 == (byte) 'F')
            type = 3;
        return type;
    }
    /**
     * Get data from stream
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    private static byte[] readStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }
    /**
     * 保存文件
     * @param bm
     * @param
     * @throws IOException
     */
    private static void saveFile(String url,Bitmap bm, int type,byte[] cs) throws Exception {

        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        dirFile = new File(paths);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        switch (type){
            case 1:
                myCaptureFile = new File(paths + HttpUtils.getMD5(url.getBytes())+".gif");
                break;
            case 2:
                myCaptureFile = new File(paths + HttpUtils.getMD5(url.getBytes())+".png");
                break;
            case 3:
                myCaptureFile = new File(paths + HttpUtils.getMD5(url.getBytes())+".jpg");
                break;
            default:
                myCaptureFile = new File(paths + HttpUtils.getMD5(url.getBytes())+ ".jpg");
                break;
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        switch (type){
            case 2:
                bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
                break;
            case 3:
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                break;
            case 1:
                readStreams(cs,myCaptureFile.getAbsolutePath());
                break;
            default:
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                break;
        }
        bos.flush();
        bos.close();
    }
    /**
     * Get data from stream
     * @param
     * @return byte[]
     * @throws Exception
     */
    private static void readStreams(byte[] b,String path) throws Exception{
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        FileOutputStream outStream = new FileOutputStream(path);
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=bais.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.flush();
        outStream.close();
        bais.close();
    }
}
