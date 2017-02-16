package com.appbook.book.updata.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.appbook.book.MyRunApplication;
import com.appbook.book.entity.BookResult;
import com.appbook.book.entity.UpdataInfo;
import com.appbook.book.updata.DialogView.ActionSheetDialog;
import com.appbook.book.updata.Crash.CrashHandler;
import com.appbook.book.updata.Utils.DeviceUtils;
import com.appbook.book.utils.HttpUtils;
import com.appbook.book.utils.cache.JsonCacheHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by zs on 2016/7/7.
 */
public class UpdateManager extends JsonCacheHttpResponseHandler {

    private int DELYED= 500;
    private Context mContext;
    private boolean isToast;
    private ActionSheetDialog actionSheetDialog;
    private final String URL = MyRunApplication.getURL("/Book/updata/updata.do");
    Handler handlers = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            HttpUtils.post(mContext,URL, URL, UpdateManager.this);
        }
    };
    private Handler hand = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (isToast) {
                        Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 0:
                    InitUpdate((UpdataInfo)msg.obj);
                    break;
            }
        }
    };
    public UpdateManager(Context context) {
        CrashHandler.getInstance().init(context);
    }
    /**
     * 检测软件更新
     */
    public void checkUpdate(Context mContext,final boolean isToast) {
        this.isToast = isToast;
        this.mContext = mContext;
        if(isToast){
            handlers.postDelayed(runnable, DELYED); //每隔500ms执行
        }else{
            handlers.postDelayed(runnable, 10); //每隔10ms执行
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
            Message mess = new Message();
            mess.what = 0;
            mess.obj = InitData(response);
            hand.sendMessage(mess);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        Message mess = new Message();
        mess.what = 1;
        hand.sendMessage(mess);
    }

    @Override
    public void onCache(JSONObject file) {
        super.onCache(file);
    }
    private UpdataInfo InitData(JSONObject jsonObject)  {
        BookResult bookResult = JSON.parseObject(jsonObject.toString(), BookResult.class);
        UpdataInfo arrayList = null;
        if (bookResult.getStatus() == 1) {
            com.alibaba.fastjson.JSONObject jsonArray = JSON.parseObject(bookResult.getData().toString());
            arrayList = (UpdataInfo) JSON.parseObject(jsonArray.toString(), UpdataInfo.class);
            return arrayList;
        }
        return arrayList;
    }
    public void InitUpdate( UpdataInfo info){
        if(info==null){
            if (isToast) {
                Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        int mVersion_code = DeviceUtils.getVersionCode(mContext);// 当前的版本号
        int nVersion_code = info.getAppvar();
        if (mVersion_code < nVersion_code) {
            actionSheetDialog = new ActionSheetDialog(mContext,info).builder();
            actionSheetDialog.show();
        } else {
            if (isToast) {
                Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
