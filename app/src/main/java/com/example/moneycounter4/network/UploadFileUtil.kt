package com.example.moneycounter4.network

import android.util.Log
import com.example.moneycounter4.beannew.ApiWrapper
import com.example.moneycounter4.beannew.UploadPicInfo
import com.example.moneycounter4.model.Config
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 *@author zhangzhe
 *@date 2021/2/14
 *@description
 */

object UploadFileUtil {
    fun uploadMultiFile(path: List<String>, callback: (UploadPicInfo) -> Unit) {
        val multipartBodyBuilder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
        path.forEach {
            val file = File(it) //选择上传文件
            val fileBody = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
            multipartBodyBuilder.addFormDataPart(
                "file",
                System.currentTimeMillis().toString() + "",
                fileBody
            )
        }
        val requestBody: RequestBody = multipartBodyBuilder.build()
        val request: Request = Request.Builder()
            .url(Config.PicUploadUrl + "PicUpload?action=uploadPic")
            .post(requestBody)
            .build()
        val httpBuilder = OkHttpClient.Builder()
        val okHttpClient: OkHttpClient = httpBuilder //设置超时
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("sandyzhang", "uploadMultiFile() e=$e")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val s = response.body!!.string()
                Log.i("sandyzhang", "uploadMultiFile() response=$s")
                callback.invoke(
                    (Gson().fromJson(
                        s,
                        object : TypeToken<ApiWrapper<UploadPicInfo>>() {}.type
                    ) as ApiWrapper<UploadPicInfo>).data
                )
            }
        })
    }
}