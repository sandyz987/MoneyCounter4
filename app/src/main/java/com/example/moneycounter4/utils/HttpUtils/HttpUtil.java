package com.example.moneycounter4.utils.HttpUtils;

import android.content.Context;
import android.util.Log;

import com.example.moneycounter4.utils.HttpUtilCallback;
import com.example.moneycounter4.utils.ThreadPool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpUtil {

    private HttpUtil() {
    }

    private static HttpUtil httpUtil;

    public static HttpUtil getInstance() {
        if (httpUtil == null) {
            synchronized (HttpUtil.class) {
                if (httpUtil == null)
                    httpUtil = new HttpUtil();
            }
        }
        return httpUtil;
    }

    public void httpGet(final String urlString, final HttpUtilCallback httpUtilCallback, final Context context, final String... requestVariableList) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder stringBuilder = new StringBuilder(urlString);
                    if (requestVariableList.length % 2 != 0) {
                        Log.e("http_get", "参数数量不对，应为一标签一值(偶数)");
                        return;
                    }
                    if (requestVariableList.length > 0) {
                        stringBuilder.append("?");
                        for (int i = 0; i < requestVariableList.length; i += 2) {
                            stringBuilder.append(requestVariableList[i]).append("=").append(requestVariableList[i + 1]);
                            if (i != requestVariableList.length - 2) {
                                stringBuilder.append("&");
                            }
                        }
                    }
                    Log.d("http_get", "已创建url：" + stringBuilder.toString());
                    URL url = new URL(stringBuilder.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(4000);
                    connection.setConnectTimeout(4000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder("");
                    String line;
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        httpUtilCallback.doSomething(response.toString());
                        Log.d("http_post", "已执行callback，result:" + response.toString());
                    }
                } catch (Exception e) {
                    httpUtilCallback.error();
                    e.printStackTrace();
                    Log.e("http_get", "错误！");
                }
            }
        };
        ThreadPool.getInstance().execute(task);
    }

    public void httpPost(final String urlString, final HttpUtilCallback httpUtilCallback, final Context context, final String... requestVariableList) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Accept-Charset", "unicode");

                    StringBuilder stringBuilder = new StringBuilder();
                    if (requestVariableList.length % 2 != 0) {
                        Log.e("http_post", "参数数量不对，应为一标签一值(偶数)");
                        return;
                    }
                    if (requestVariableList.length > 0) {
                        for (int i = 0; i < requestVariableList.length; i += 2) {
                            stringBuilder.append(requestVariableList[i]).append("=").append(requestVariableList[i + 1]);
                            if (i != requestVariableList.length - 2) {
                                stringBuilder.append("&");
                            }
                        }
                        Log.d("http_post", "已创建参数列表：" + stringBuilder.toString());
                        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                        OutputStreamWriter o = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                        o.write(stringBuilder.toString());
                        out.writeBytes(new String(stringBuilder.toString().getBytes(),0,stringBuilder.toString().getBytes().length,StandardCharsets.ISO_8859_1));
                    }


                    connection.setReadTimeout(4000);
                    connection.setConnectTimeout(4000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        httpUtilCallback.doSomething(response.toString());
                        Log.d("http_post", "已执行callback，result:" + response);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    httpUtilCallback.error();
                    Log.e("http_post", "错误！");
                }
            }
        };
        ThreadPool.getInstance().execute(task);
    }


}
