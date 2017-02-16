package com.appbook.book.CallBack;

/**
 * Created by zhangzhongping on 16/10/31.
 */

public interface DowBookCallBack {
    void onSuccess(String mag,String path,String name);
    void onFiled(String mag);
    void onProces(long sta, long end);
}
