package com.appbook.book.updata.callback;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhangzhongping on 16/12/9.
 */

public interface CallBack {
    public void onSuccess(File file);
    public void onLoading(long progress, long total);
    public void onFailure(Call<ResponseBody> call, Throwable t);
}
