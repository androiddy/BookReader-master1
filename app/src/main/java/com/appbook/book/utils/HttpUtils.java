package com.appbook.book.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.appbook.book.MyRunApplication;
import com.appbook.book.R;
import com.appbook.book.utils.cache.JsonCacheHttpResponseHandler;
import com.appbook.book.utils.cache.SPUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by zhangzhongping on 16/10/28.
 */

public  class HttpUtils {
    private static final String KEY_STORE_TYPE_BKS = "bks";
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";
    private static final String KEY_STORE_PASSWORDS = "5107721";
    private static final String KEY_STORE_TRUST_PASSWORDS = "5107721";
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    static {
        asyncHttpClient.setTimeout(10000);
        asyncHttpClient.setSSLSocketFactory(getSocketFactory(MyRunApplication.getAppContext()));
    }
    public static void post(Context context,String url,String key, RequestParams requestParams, JsonCacheHttpResponseHandler asyncHttpResponseHandler){
        getPreferencesCacheData(MyRunApplication.getAppContext(),key,asyncHttpResponseHandler);
        asyncHttpClient.post( context,url,requestParams ,asyncHttpResponseHandler);
    }
    public static void post(Context context, String url,String key, JsonCacheHttpResponseHandler asyncHttpResponseHandler){
        getPreferencesCacheData(MyRunApplication.getAppContext(),key,asyncHttpResponseHandler);
        asyncHttpClient.post(context, url,null,asyncHttpResponseHandler);
    }
    public static void post(Context context,String url,RequestParams requestParams){
        asyncHttpClient.post(context,url,requestParams, new JsonCacheHttpResponseHandler() {
            @Override
            public void onCache(JSONObject file) {
                super.onCache(file);
            }
        });
    }
    public static void Cancelrequest(Context context){
        asyncHttpClient.cancelRequests(context,true);
    }
    private static SSLSocketFactory getSocketFactory(Context context)  {
        KeyStore keyStore = null;
        KeyStore trustStore = null;
        SSLSocketFactory socketFactory=null;
        try {
            // 服务器端需要验证的客户端证书
            keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
            // 客户端信任的服务器端证书
            trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);
            //.p12文件
            InputStream ksIn = context.getResources().openRawResource(R.raw.client);
            //.bks文件
            InputStream tsIn = context.getResources().openRawResource(R.raw.server);
            try {
                keyStore.load(ksIn, KEY_STORE_PASSWORDS.toCharArray());
                trustStore.load(tsIn, KEY_STORE_TRUST_PASSWORDS.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    ksIn.close();
                } catch (Exception ignore) {
                }
                try {
                    tsIn.close();
                } catch (Exception ignore) {
                }
            }
            socketFactory = new SSLSocketFactory(keyStore, KEY_STORE_PASSWORDS, trustStore) ;
            socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  socketFactory;
    }
    private static boolean getPreferencesCacheData(Context context, String key,JsonCacheHttpResponseHandler responseHandler){
        key = getMD5(key.getBytes());
        String cache = null;
        cache = String.valueOf(SPUtils.get(context, key, ""));
        if (!TextUtils.isEmpty(cache)) {
            JSONObject myJsonObject = null;
            try {
                myJsonObject = new JSONObject(cache);
                responseHandler.onCache(myJsonObject);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        else{
            return false;
        }
    }
    public static String getMD5(byte[] src) {
        StringBuffer sb = new StringBuffer();
        try {
            java.security.MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src);
            for (byte b : md.digest()) {
                sb.append(Integer.toString(b >>> 4 & 0xF, 16)).append(Integer.toString(b & 0xF, 16));
            }
        } catch (NoSuchAlgorithmException e) {
        }
        return sb.toString();
    }
    /**
     * make true current connect service is wifi
     *
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
    /**
     * 检查当前网络是否可用
     *
     * @param
     * @return
     */

    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
