package com.appbook.book.HttpUtils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangzhongping on 16/11/3.
 */

public class DataTypeCon {
    static String reg = "[\u4e00-\u9fa5]";
    static Pattern pa = Pattern.compile(reg);
    public static int DataInt(String i){
        return Integer.parseInt(i);
    }
    public static long DataLong(String i){
        return Long.parseLong(i);
    }
    public static boolean isContains(String str){
        Matcher matcher = pa.matcher(str);
        boolean bak = false;
        if(matcher.find()){
            bak = true;
        }
        return bak;
    }

}
