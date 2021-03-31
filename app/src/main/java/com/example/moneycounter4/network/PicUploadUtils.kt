package com.example.moneycounter4.network

import com.example.moneycounter4.BuildConfig
import com.example.moneycounter4.beannew.ApiWrapper
import com.example.moneycounter4.beannew.UploadPicInfo
import com.example.moneycounter4.model.Config
import io.reactivex.Observable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.File
import java.util.concurrent.TimeUnit


/**
 *@author zhangzhe
 *@date 2021/3/31
 *@description
 */

object PicUploadUtils {

    interface Api {
        @POST("PicUpload?action=uploadPic")
        fun uploadPicture(
            @Body photo: MultipartBody
        ): Observable<ApiWrapper<UploadPicInfo>>
    }

    fun uploadPicture(
        imgPath: List<String>,
        onSuccess: (picUrls: List<String>) -> Unit,
        onFailed: () -> Unit
    ) {
        val fileList = imgPath.map { File(it) }
        val builder = MultipartBody.Builder()

        for (i in fileList.indices) {
            val file = fileList[i]
            val requestFile: RequestBody =
                RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            builder.addFormDataPart("file", file.name, requestFile)
        }

        val retrofit = Retrofit.Builder().apply {
            baseUrl(Config.PicUploadUrl)
            client(OkHttpClient().newBuilder().apply {
                addConverterFactory(GsonConverterFactory.create())
                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(logging)
                }
                connectTimeout(30, TimeUnit.SECONDS)
            }.build())
        }.build()


        retrofit.create(Api::class.java).uploadPicture(builder.build())
            .checkApiError()
            .setSchedulers()
            .doOnError {
                onFailed.invoke()
            }
            .safeSubscribeBy {
                onSuccess.invoke(it.picUrls)
            }.dispose()

    }


}