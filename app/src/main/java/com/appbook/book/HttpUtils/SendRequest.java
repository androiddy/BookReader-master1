package com.appbook.book.HttpUtils;

import android.content.Context;
import android.widget.Toast;

import com.appbook.book.Activity;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.CallBackImpl.BookSessionCallBackImpl;
import com.appbook.book.CallBackImpl.FindBookCallBackImpl;
import com.appbook.book.CallBackImpl.HotNetworkCallBackImpl;
import com.appbook.book.CallBackImpl.NetworkCallBackImpl;
import com.appbook.book.CommentsActvivty;
import com.appbook.book.MyRunApplication;
import com.appbook.book.SplashActivity;
import com.appbook.book.TotalMainActivity;
import com.appbook.book.entity.NnumberAll;
import com.appbook.book.fragment.UserFragmen;
import com.appbook.book.fragment.main.CategoryListActivity;
import com.appbook.book.fragment.main.MainActivity;
import com.appbook.book.fragment.main.Mains1Activity;
import com.appbook.book.fragment.main2.DowListActivity;
import com.appbook.book.fragment.main2.Find_hotRecommendFragment;
import com.appbook.book.fragment.main2.MessageIndexctivity;
import com.appbook.book.fragment.main2.MessageListctivity;
import com.cwvs.microlife.searchview.SearchActivity;
import com.justwayward.reader.utils.FileUtils;
import com.loopj.android.http.RequestParams;

/**
 * Created by zhangzhongping on 16/11/4.
 */

public class SendRequest {
    public static HttpUtils httpUtils;
    public static int Unmber = 0;
    private static RequestParams requestParams = null;
    private static String URLSendRandomBook = "/Book/Book/RandomByid.do";
    private static String URLSendHotBook = "/Book/Book/findAllByHot.do";
    private static String URLSendFindByBook = "/Book/Book/FindByBook.do";
    private static String SendDowListBook = "/Book/project/findAllByPro.do";
    private static String SendSendFindByPro = "/Book/Book/findByPro.do";
    private static String SendMesList = "/Book/message/findAllMesType.do";
    private static String SendAddClick = "/Book/message/AddMesClickCount.do";
    private static String SendMesBanner = "/Book/Banner/findByMesBanner.do";
    private static String AddMes = "/Book/Comments/addmsg.do";
    private static String SendSearchError = "/Book/Book/findBySearch.do";
    public static void SendRandomBook(Context context,Mains1Activity activity,int i){
        httpUtils = new HttpUtils(MyRunApplication.getURL(URLSendRandomBook),context,new NetworkCallBackImpl(activity),null);
        httpUtils.NetworkRequest(null,false,i);
    }

    public static void SendHotBook(Context context,SearchActivity activity){
        httpUtils = new HttpUtils(MyRunApplication.getURL(URLSendHotBook),context,new HotNetworkCallBackImpl(activity),null);
        httpUtils.HotNetworkRequest(false);
    }
    public static void SendFindByBook(Context context,SearchActivity activity,String name,long tima,boolean str,String id){
        requestParams = new RequestParams();
        requestParams.put("name",name);
        requestParams.put("time",tima);
        requestParams.put("currentPage",Unmber);
        requestParams.put("id",id);
        httpUtils = new HttpUtils(MyRunApplication.getURL(URLSendFindByBook),context,new FindBookCallBackImpl(activity),null);
        httpUtils.NetworkRequest(requestParams,str,0);
    }
    public static void SendDowListBook(DowListActivity context){
        httpUtils = new HttpUtils(MyRunApplication.getURL(SendDowListBook),context.getContext(),context, null);
        httpUtils.DowNetworkRequest(false);
    }
    public static void SendFindByPro(Context context, CategoryListActivity activity, int id){
        requestParams = new RequestParams();
        requestParams.put("id",id);
        httpUtils = new HttpUtils(MyRunApplication.getURL(SendSendFindByPro),context,activity,null);
        httpUtils.NetworkRequest(requestParams,true,1);
    }
    public static void SendMesList(MessageListctivity context){
        httpUtils = new HttpUtils(MyRunApplication.getURL(SendMesList),context.getContext(),context, null);
        httpUtils.MesListNetworkRequest(false);
    }
    public static void SendAddClick(Context context, MessageIndexctivity activity, int id){
        requestParams = new RequestParams();
        requestParams.put("id",id);
        httpUtils = new HttpUtils(MyRunApplication.getURL(SendAddClick),context,null,null);
        httpUtils.NetworkRequest(requestParams,true,0);
    }
    public static void SendMesBanner(MainActivity context){
        httpUtils = new HttpUtils(MyRunApplication.getURL(SendMesBanner),context.getContext(),context, null);
        httpUtils.BannerAllNetworkRequest(false);
    }

    public static void AddMes(MessageIndexctivity context,String msg,String id,int msgid){
        requestParams = new RequestParams();
        requestParams.put("userid",id);
        requestParams.put("usermsg",msg);
        requestParams.put("mesid",msgid);
        requestParams.put("type",0);
        httpUtils = new HttpUtils(MyRunApplication.getURL(AddMes),context,context, null);
        httpUtils.AddMsgRequest(requestParams,false);
    }
    public static void AddMes(CommentsActvivty context, String msg,String id,int msgid){
        requestParams = new RequestParams();
        requestParams.put("userid",id);
        requestParams.put("usermsg",msg);
        requestParams.put("mesid",msgid);
        requestParams.put("type",0);
        httpUtils = new HttpUtils(MyRunApplication.getURL(AddMes),context,context, null);
        httpUtils.AddMsgRequest(requestParams,false);
    }

    public static void AddMes(UserFragmen context, String msg, String id, int msgid){
        requestParams = new RequestParams();
        requestParams.put("userid",id);
        requestParams.put("usermsg",msg);
        requestParams.put("mesid",msgid);
        requestParams.put("type",1);
        httpUtils = new HttpUtils(MyRunApplication.getURL(AddMes),context.getContext(),context, null);
        httpUtils.AddMsgRequest(requestParams,false);
    }

    public static void SendSearchError(Context context, SearchActivity activity){
        httpUtils = new HttpUtils(MyRunApplication.getURL(SendSearchError),context,activity,null);
        httpUtils.NetworkRequest(null,true,0);
    }
}
