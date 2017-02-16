package com.appbook.book.HttpUtils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.entity.BannerInfo;
import com.appbook.book.entity.BookResult;
import com.appbook.book.entity.CommentsInfo;
import com.appbook.book.entity.HotBookInfo;
import com.appbook.book.entity.MessageInfoIndex;
import com.appbook.book.entity.MessageListInfo;
import com.appbook.book.entity.MessageResultInfo;
import com.appbook.book.entity.NnumberAll;
import com.appbook.book.entity.ProjectInfo;
import com.appbook.book.entity.TypeAllInfo;
import com.appbook.book.entity.mBookInfo;
import com.appbook.book.utils.cache.JsonCacheHttpResponseHandler;
import com.appbook.book.utils.cache.SPUtils;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import okhttp3.OkHttpClient;

/**
 * Created by zhangzhongping on 16/10/29.
 */

public class HttpUtils {
    private String url;
    private Context context;
    private NetworkCallBack networkCallBack;
    private NnumberAll objects;

    public HttpUtils(String url, Context context, NetworkCallBack networkCallBack, NnumberAll objects) {
        this.url = url;
        this.context = context;
        this.networkCallBack = networkCallBack;
        this.objects = objects;
    }

    public void NetworkRequest(final RequestParams requestParams, final boolean str, final int i) {
        if (requestParams != null) {
            com.appbook.book.utils.HttpUtils.post(context,url, url + "?" + requestParams.toString(), requestParams, new JsonCacheHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (networkCallBack != null) {
                        try {
                            ArrayList arrayList = InitData(response, str);
                            if (i == 1) {
                                String key = com.appbook.book.utils.HttpUtils.getMD5((url + "?" + requestParams.toString()).getBytes());
                                if (SPUtils.contains(context,key)) {
                                    SPUtils.remove(context, key);
                                }
                                SPUtils.put(context, key, response);
                                networkCallBack.onSuccess(arrayList, str, false);
                            } else {
                                networkCallBack.onSuccess(arrayList, str, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    if (networkCallBack != null) {
                        boolean boo = false;
                        String key = com.appbook.book.utils.HttpUtils.getMD5((url + "?" + requestParams.toString()).getBytes());
                        if (SPUtils.contains(context,key)) {
                            boo = true;
                        }
                        networkCallBack.onFiled(throwable, boo);
                    }
                }

                @Override
                public void onCache(JSONObject file) {
                    if (networkCallBack != null && i == 1) {
                        try {
                            networkCallBack.onSuccess(InitData(file, str), str, true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            com.appbook.book.utils.HttpUtils.post(context,url, url, new JsonCacheHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (networkCallBack != null) {
                        try {
                            ArrayList arrayList = InitData(response, str);
                            if (i == 1) {
                                String key = com.appbook.book.utils.HttpUtils.getMD5(url.getBytes());
                                if (SPUtils.contains(context,key)) {
                                    SPUtils.remove(context, key);
                                    SPUtils.put(context, key, response);
                                }else {
                                    SPUtils.put(context, key, response);
                                    networkCallBack.onSuccess(arrayList, str, false);
                                }
                            } else {
                                networkCallBack.onSuccess(arrayList, str, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    if (networkCallBack != null) {
                        boolean boo = false;
                        String key = com.appbook.book.utils.HttpUtils.getMD5(url.getBytes());
                        if (SPUtils.contains(context,key)) {
                            boo = true;
                        }
                        networkCallBack.onFiled(throwable, boo);
                    }
                }

                @Override
                public void onCache(JSONObject file) {
                    if (networkCallBack != null && i == 1) {
                        try {
                            networkCallBack.onSuccess(InitData(file, str), str, true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void TypeNetworkRequest(final boolean str, final int i) {
        com.appbook.book.utils.HttpUtils.post(context,url, url, new JsonCacheHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (networkCallBack != null) {
                    try {
                        ArrayList arrayList = TypeInitData(response);
                        networkCallBack.onSuccess(arrayList, str, false);
                        if (i == 1) {
                            String key = com.appbook.book.utils.HttpUtils.getMD5(url.getBytes());
                            if (SPUtils.contains(context,key)) {
                                SPUtils.remove(context, key);
                            }
                            SPUtils.put(context, key, response);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (networkCallBack != null) {
                    boolean boo = false;
                    String key = com.appbook.book.utils.HttpUtils.getMD5(url.getBytes());
                    if (SPUtils.contains(context,key)) {
                        boo = true;
                    }
                    networkCallBack.onFiled(throwable, boo);
                }
            }

            @Override
            public void onCache(JSONObject file) {
                if (networkCallBack != null && i == 1) {
                    try {
                        networkCallBack.onSuccess(TypeInitData(file), str, true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void HotNetworkRequest(final boolean str) {
        com.appbook.book.utils.HttpUtils.post(context,url, url, new JsonCacheHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (networkCallBack != null) {
                    try {
                        networkCallBack.onSuccess(HotInitData(response), str, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (networkCallBack != null) {
                    networkCallBack.onFiled(throwable, false);
                }
            }

            @Override
            public void onCache(JSONObject file) {

            }
        });
    }

    public void DowNetworkRequest(final boolean str) {
        com.appbook.book.utils.HttpUtils.post(context,url, url, new JsonCacheHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (networkCallBack != null) {
                    try {
                        networkCallBack.onSuccess(DowInitData(response), str, false);
                        String key = com.appbook.book.utils.HttpUtils.getMD5(url.getBytes());
                        if (SPUtils.contains(context,key)) {
                            SPUtils.remove(context, key);
                        }
                        SPUtils.put(context, key, response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (networkCallBack != null) {
                    boolean boo = false;
                    String key = com.appbook.book.utils.HttpUtils.getMD5(url.getBytes());
                    if (SPUtils.contains(context,key)) {
                        boo = true;
                    }
                    networkCallBack.onFiled(throwable, boo);
                }
            }

            @Override
            public void onCache(JSONObject file) {
                if (networkCallBack != null) {
                    try {
                        networkCallBack.onSuccess(DowInitData(file), str, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void MesListNetworkRequest(final boolean str) {
        com.appbook.book.utils.HttpUtils.post(context,url, url, new JsonCacheHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (networkCallBack != null) {
                    try {
                        networkCallBack.onSuccess(MesListInitData(response), str, false);
                        String key = com.appbook.book.utils.HttpUtils.getMD5(url.getBytes());
                        if (SPUtils.contains(context,key)) {
                            SPUtils.remove(context, key);
                        }
                        SPUtils.put(context, key, response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (networkCallBack != null) {
                    boolean boo = false;
                    String key = com.appbook.book.utils.HttpUtils.getMD5(url.getBytes());
                    if (SPUtils.contains(context,key)) {
                        boo = true;
                    }
                    networkCallBack.onFiled(throwable, boo);
                }
            }

            @Override
            public void onCache(JSONObject file) {
                if (networkCallBack != null) {
                    try {
                        networkCallBack.onSuccess(MesListInitData(file), str, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void MesAllNetworkRequest(final RequestParams requestParams, final boolean str) {
        com.appbook.book.utils.HttpUtils.post(context,url, url + "?" + requestParams.toString(), requestParams, new JsonCacheHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (networkCallBack != null) {
                    try {
                        if (requestParams.toString().indexOf("mesid=0") >= 0) {
                            String key = com.appbook.book.utils.HttpUtils.getMD5((url + "?" + requestParams.toString()).getBytes());
                            if (SPUtils.contains(context,key)) {
                                SPUtils.remove(context, key);
                            }
                            SPUtils.put(context, key, response);
                        }
                        networkCallBack.onSuccess(MesAllInitData(response), str, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (networkCallBack != null) {
                    boolean boo = false;
                    if (requestParams.toString().indexOf("mesid=0") >= 0) {
                        String key = com.appbook.book.utils.HttpUtils.getMD5((url + "?" + requestParams.toString()).getBytes());
                        if (SPUtils.contains(context,key)) {
                            boo = true;
                        }
                    }
                    networkCallBack.onFiled(throwable, boo);
                }
            }

            @Override
            public void onCache(JSONObject file) {
                if (requestParams.toString().indexOf("mesid=0") >= 0) {
                    try {
                        networkCallBack.onSuccess(MesAllInitData(file), str, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void BannerAllNetworkRequest(final boolean str) {
        com.appbook.book.utils.HttpUtils.post(context,url, url, new JsonCacheHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (networkCallBack != null) {
                    try {
                        String key = com.appbook.book.utils.HttpUtils.getMD5((url).getBytes());
                        if (SPUtils.contains(context,key)) {
                            SPUtils.remove(context, key);
                        }
                        SPUtils.put(context, key, response);
                        networkCallBack.onSuccess(BannerAllInitData(response), str, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (networkCallBack != null) {
                    boolean boo = false;
                    String key = com.appbook.book.utils.HttpUtils.getMD5(url.getBytes());
                    if (SPUtils.contains(context,key)) {
                        boo = true;
                    }
                    networkCallBack.onFiled(throwable, boo);
                }
            }

            @Override
            public void onCache(JSONObject file) {
                try {
                    networkCallBack.onSuccess(BannerAllInitData(file), str, false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void AddMsgRequest(final RequestParams requestParams,final boolean str) {
        com.appbook.book.utils.HttpUtils.post(context,url, url,requestParams, new JsonCacheHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                BookResult bookResult = JSON.parseObject(response.toString(), BookResult.class);
                ArrayList<BookResult> arrayList = new ArrayList<BookResult>();
                arrayList.add(bookResult);
                networkCallBack.onSuccess(arrayList,false,false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }

            @Override
            public void onCache(JSONObject file) {

            }
        });
    }

    public void FinfMsgRequest(final RequestParams requestParams,final boolean str) {
        com.appbook.book.utils.HttpUtils.post(context,url, url,requestParams, new JsonCacheHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (networkCallBack != null) {
                    try {
                        networkCallBack.onSuccess(FindData(response,false),str,false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (networkCallBack != null) {
                    networkCallBack.onFiled(throwable,false);
                }
            }

            @Override
            public void onCache(JSONObject file) {

            }
        });
    }
    public void AddUserRequest(final RequestParams requestParams,final boolean str) {
        com.appbook.book.utils.HttpUtils.post(context,url, url,requestParams, new JsonCacheHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                BookResult bookResult = JSON.parseObject(response.toString(), BookResult.class);
                ArrayList<BookResult> arrayList = new ArrayList<BookResult>();
                arrayList.add(bookResult);
                networkCallBack.onSuccess(arrayList,false,false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                networkCallBack.onFiled(throwable,false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                networkCallBack.onFiled(throwable,false);
            }

            @Override
            public void onCache(JSONObject file) {

            }
        });
    }

    private ArrayList<CommentsInfo> FindData(JSONObject jsonObject, boolean str) throws JSONException {
        BookResult bookResult = JSON.parseObject(jsonObject.toString(), BookResult.class);
        ArrayList<CommentsInfo> arrayList = null;
        if (bookResult.getStatus() == 1) {
            com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(bookResult.getData().toString());
            arrayList = (ArrayList<CommentsInfo>) JSON.parseArray(jsonArray.toString(), CommentsInfo.class);
            if (objects != null) {
                objects.setCurrentPage(arrayList.get(arrayList.size() - 1).getId());
            }
            return arrayList;
        } else if (bookResult.getStatus() != 0) {
            Toast.makeText(context, bookResult.getMsg(), 0).show();
        }
        return arrayList;
    }
    private ArrayList<mBookInfo> InitData(JSONObject jsonObject, boolean str) throws JSONException {
        BookResult bookResult = JSON.parseObject(jsonObject.toString(), BookResult.class);
        if (objects != null && !str) {
            objects.setNUMBER(bookResult.getNumber());
        }
        ArrayList<mBookInfo> arrayList = null;
        if (bookResult.getStatus() == 1) {
            com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(bookResult.getData().toString());
            arrayList = (ArrayList<mBookInfo>) JSON.parseArray(jsonArray.toString(), mBookInfo.class);
            if(arrayList!=null&&arrayList.size()>0){
                arrayList.get(0).setNumber(bookResult.getNumber());
                if (objects != null) {
                    objects.setCurrentPage(arrayList.get(arrayList.size() - 1).getBook_id());
                }
            }
            /*for(int i = 0;i<jsonArray.size();i++){
                com.alibaba.fastjson.JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                mBookInfo mBookInfos = new mBookInfo();
                mBookInfos.setBook_name(jsonObject1.getString("book_name"));
                mBookInfos.setBook_author(jsonObject1.getString("book_author"));
                mBookInfos.setBook_dowurl(jsonObject1.getString("book_dowurl"));
                mBookInfos.setBook_number(jsonObject1.getString("book_number"));
                mBookInfos.setBook_size(jsonObject1.getString("book_size"));
                mBookInfos.setBook_type(jsonObject1.getInteger("book_type"));
                mBookInfos.setBook_ytpename(jsonObject1.getString("book_typename"));
                mBookInfos.setBook_id(jsonObject1.getInteger("book_id"));
                mBookInfos.setLogo(jsonObject1.getString("book_logo"));
                mBookInfos.setBook_introduction(jsonObject1.getString("book_introduction"));
                mBookInfos.setBook_time(jsonObject1.getLong("book_time"));
                mBookInfos.setBook_byte(jsonObject1.getLong("book_byte"));
                mBookInfos.setNumber(bookResult.getNumber());
                arrayList.add(mBookInfos);
                if(objects!=null){
                    objects.setCurrentPage(mBookInfos.getBook_id());
                }*/
            return arrayList;
        } else if(bookResult.getStatus()==12){
            networkCallBack.onError();
        }else if (bookResult.getStatus() != 0) {
            Toast.makeText(context, bookResult.getMsg(), 0).show();
        }
        return arrayList;
    }

    private ArrayList<ProjectInfo> DowInitData(JSONObject jsonObject) throws JSONException {
        BookResult bookResult = JSON.parseObject(jsonObject.toString(), BookResult.class);
        ArrayList<ProjectInfo> arrayList = null;
        if (bookResult.getStatus() == 1) {
            com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(bookResult.getData().toString());
            arrayList = (ArrayList<ProjectInfo>) JSON.parseArray(jsonArray.toString(), ProjectInfo.class);
            return arrayList;
        } else {
            Toast.makeText(context, bookResult.getMsg(), 0).show();
        }
        return arrayList;
    }

    private ArrayList<MessageInfoIndex> MesAllInitData(JSONObject jsonObject) throws JSONException {
        MessageResultInfo bookResult = JSON.parseObject(jsonObject.toString(), MessageResultInfo.class);
        ArrayList<MessageInfoIndex> arrayList = null;
        if (bookResult.getStatus() == 1) {
            com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(bookResult.getData().toString());
            arrayList = (ArrayList<MessageInfoIndex>) JSON.parseArray(jsonArray.toString(), MessageInfoIndex.class);
            if (objects != null) {
                objects.setCurrentPage(arrayList.get(arrayList.size() - 1).getId());
            }
            return arrayList;
        } else {
            Toast.makeText(context, bookResult.getMsg(), 0).show();
        }
        return arrayList;
    }

    private ArrayList<BannerInfo> BannerAllInitData(JSONObject jsonObject) throws JSONException {
        MessageResultInfo bookResult = JSON.parseObject(jsonObject.toString(), MessageResultInfo.class);
        ArrayList<BannerInfo> arrayList = null;
        if (bookResult.getStatus() == 1) {
            com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(bookResult.getData().toString());
            arrayList = (ArrayList<BannerInfo>) JSON.parseArray(jsonArray.toString(), BannerInfo.class);
            if (objects != null) {
                objects.setCurrentPage(arrayList.get(arrayList.size() - 1).getId());
            }
            return arrayList;
        } else {
            Toast.makeText(context, bookResult.getMsg(), 0).show();
        }
        return arrayList;
    }

    private ArrayList<MessageListInfo> MesListInitData(JSONObject jsonObject) throws JSONException {
        BookResult bookResult = JSON.parseObject(jsonObject.toString(), BookResult.class);
        ArrayList<MessageListInfo> arrayList = null;
        if (bookResult.getStatus() == 1) {
            com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(bookResult.getData().toString());
            arrayList = (ArrayList<MessageListInfo>) JSON.parseArray(jsonArray.toString(), MessageListInfo.class);
            return arrayList;
        } else {
            Toast.makeText(context, bookResult.getMsg(), 0).show();
        }
        return arrayList;
    }

    private ArrayList<TypeAllInfo> TypeInitData(JSONObject jsonObject) throws JSONException {
        BookResult bookResult = JSON.parseObject(jsonObject.toString(), BookResult.class);
        ArrayList<TypeAllInfo> arrayList = null;
        if (bookResult.getStatus() == 1) {
            com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(bookResult.getData().toString());
            arrayList = (ArrayList<TypeAllInfo>) JSON.parseArray(jsonArray.toString(), TypeAllInfo.class);
            return arrayList;
        } else {
            Toast.makeText(context, bookResult.getMsg(), 0).show();
        }
        return arrayList;
    }

    private ArrayList<HotBookInfo> HotInitData(JSONObject jsonObject) throws JSONException {
        BookResult bookResult = JSON.parseObject(jsonObject.toString(), BookResult.class);
        ArrayList<HotBookInfo> arrayList = null;
        if (bookResult.getStatus() == 1) {
            com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(bookResult.getData().toString());
            arrayList = (ArrayList<HotBookInfo>) JSON.parseArray(jsonArray.toString(), HotBookInfo.class);
            return arrayList;
        } else {
            Toast.makeText(context, bookResult.getMsg(), 0).show();
        }
        return arrayList;
    }
}
