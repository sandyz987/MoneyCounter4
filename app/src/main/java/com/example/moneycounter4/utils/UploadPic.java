package com.example.moneycounter4.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.moneycounter4.widgets.LogW;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import org.json.JSONObject;


public class UploadPic {

    public static void upload(String path,HttpUtilCallback httpUtilCallback){
        Configuration cfg = new Configuration(Region.region0());
        UploadManager uploadManager = new UploadManager(cfg);
        String accessKey = "Ih70dD_u2apnm5HV9MThEM0345aU1RPb8_KQr7o2";
        String secretKey = "7YUQbGq4T0pCVyVzSvhlzT7-xfKMFfcAGMI54b8x";
        String bucket = "sandyzhang2";
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        ThreadPool.getInstance().execute(() -> {
            try {
                Response response = uploadManager.put(path, key, upToken);
                //解析上传成功的结果
                LogW.INSTANCE.d(response.bodyString());
                try{
                    JSONObject jsonObject=new JSONObject(response.bodyString());
                    String s="http://qdnzcqgo4.bkt.clouddn.com/";
//                Message msg=Message.obtain();
//                msg.obj=s+jsonObject.getString("key");
//                handler.sendMessage(msg);
                    httpUtilCallback.doSomething(s + jsonObject.getString("key"));
                }catch (Exception e){
                    httpUtilCallback.error();
                    e.printStackTrace();
                }

            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        });
    }
}
