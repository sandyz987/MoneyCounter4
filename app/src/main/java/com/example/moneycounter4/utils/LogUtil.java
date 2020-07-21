package com.example.moneycounter4.utils;

import android.util.Log;

public class LogUtil {
    //规定每段显示的长度
    private static int LOG_MAX_LENGTH = 2000;

    public static void e(String msg) {
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAX_LENGTH;
        for (int i = 0; i < 100; i++) {
            //剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                Log.e("sandyzhang" + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAX_LENGTH;
            } else {
                Log.e("sandyzhang", msg.substring(start, strLength));
                break;
            }
        }
    }
}