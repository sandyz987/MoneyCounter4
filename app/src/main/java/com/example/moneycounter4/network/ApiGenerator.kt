package com.example.moneycounter4.network

import beannew.ApiWrapper
import beannew.Token
import com.example.moneycounter4.model.Config
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

/**
 *@author zhangzhe
 *@date 2021/2/12
 *@description
 */
interface LoginApi {
    @POST("?action=login")
    @FormUrlEncoded
    fun getToken(
        @Field("user_id") userId: String,
        @Field("password") password: String
    ): Call<ApiWrapper<Token>>
}

object ApiGenerator {
    private var retrofit: Retrofit
    private var retrofitCommon: Retrofit
    private var token: Int = 0
    private val retryNum = 3
    private val tokenUrl = "http://localhost:8080/Counter4/Counter4Sql?action=login"

    init {
        retrofit = Retrofit.Builder().apply {
            baseUrl(Config.MainUrl)
            client(OkHttpClient().newBuilder().apply {
                addConverterFactory(GsonConverterFactory.create())
                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                connectTimeout(10, TimeUnit.SECONDS)
                interceptors().add(object : Interceptor { // 通过添加拦截器来实现自动化token管理
                    override fun intercept(chain: Interceptor.Chain): Response {
                        var retryCount = 0
                        var request =
                            chain.request().newBuilder().header("token", token.toString()).build()
                        var response = chain.proceed(request)
                        if (response.isSuccessful) {
                            return response
                        } else {
                            // 如果失败，获取一次token
                            token = getCommonApiService(LoginApi::class.java).getToken(
                                Config.userId,
                                Config.password
                            ).execute().body()?.data?.token ?: 0
                        }

                        // 获取完token再看看能不能请求成功
                        request =
                            chain.request().newBuilder().header("token", token.toString()).build()
                        response = chain.proceed(request)
                        // 如果失败则重试3次
                        while (!response.isSuccessful && retryCount < retryNum) {
                            retryCount++
                            response = chain.proceed(request)
                        }

                        return response
                    }
                })
            }.build())
        }.build()
        retrofitCommon = Retrofit.Builder().apply {
            baseUrl(Config.MainUrl)
            client(OkHttpClient().newBuilder().apply {
                addConverterFactory(GsonConverterFactory.create())
                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                connectTimeout(10, TimeUnit.SECONDS)
            }.build())
        }.build()
    }

    fun <T> getCommonApiService(clazz: Class<T>) = retrofitCommon.create(clazz)

    fun <T> getApiService(clazz: Class<T>) = retrofit.create(clazz)


}