package com.appbook.book.utils.cache;


import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/11/23.
 */

public abstract class JsonCacheHttpResponseHandler extends JsonHttpResponseHandler {

    public void onCache(JSONObject file){

    }
}
