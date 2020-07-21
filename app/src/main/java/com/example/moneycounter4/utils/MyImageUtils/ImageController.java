package com.example.moneycounter4.utils.MyImageUtils;

import android.util.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class ImageController {
    private static Map<String,Future<android.graphics.Bitmap>> imageData=new ConcurrentHashMap<>();
    public static void add(String url, Future<android.graphics.Bitmap> future){

        if (imageData.size() >= 40){
            imageData=new ConcurrentHashMap<>();
        }else {
            imageData.put(url,future);
            Log.d("ImageLoader","缓存bitmap数量：" + imageData.size());
        }
    }
    public static Future<android.graphics.Bitmap> get(String url){
        return imageData.get(url);
    }



}
