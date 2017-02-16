package com.appbook.book.CallBack;

import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/10/29.
 */

public interface NetworkCallBack {
    void onSuccess(ArrayList arrayList, boolean str,boolean cache);
    void onFiled(Throwable throwable,boolean str);
    void onError();
}
