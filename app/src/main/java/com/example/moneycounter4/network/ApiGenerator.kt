package com.example.moneycounter4.network

import com.example.moneycounter4.BuildConfig
import com.example.moneycounter4.beannew.ApiWrapper
import com.example.moneycounter4.beannew.InfoWrapper
import com.example.moneycounter4.beannew.Token
import com.example.moneycounter4.beannew.isSuccessful
import com.example.moneycounter4.model.Config
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
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
    @POST("Counter4Sql?action=login")
    @FormUrlEncoded
    fun getToken(
        @Field("user_id") userId: String,
        @Field("password") password: String
    ): Call<ApiWrapper<Token>>
}

fun <T> Observable<T>.safeSubscribeBy(
    onError: (Throwable) -> Unit = { it.printStackTrace() },
    onComplete: () -> Unit = {},
    onNext: (T) -> Unit = {}
): Disposable = subscribe(onNext, onError, onComplete)

@CheckReturnValue
fun <T> Observable<T>.setSchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    unsubscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Observable<T> = subscribeOn(subscribeOn)
    .unsubscribeOn(unsubscribeOn)
    .observeOn(observeOn)

fun Observable<out InfoWrapper>.checkError(): Observable<out InfoWrapper> = map {
    if (it.isSuccessful) {
        it
    } else {
        throw ApiException(it.info, it.status)
    }
}


class ApiException(info: String, status: Int, cause: Throwable? = null) :
    RuntimeException(info, cause)

object ApiGenerator {
    private var retrofit: Retrofit
    private var retrofitCommon: Retrofit
    private var token: Int = 0
    private const val retryNum = 3

    init {
        retrofit = Retrofit.Builder().apply {
            baseUrl(Config.MainUrl)
            client(OkHttpClient().newBuilder().apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(logging)
                }
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
                        }
                        // 如果失败，获取一次token
                        token = getCommonApiService(LoginApi::class.java).getToken(
                            Config.userId,
                            Config.password
                        ).execute().body()?.data?.token ?: 0
                        response.close()
                        // 获取完token再看看能不能请求成功
                        request =
                            chain.request().newBuilder().header("token", token.toString()).build()
                        response = chain.proceed(request)
                        // 如果失败则重试3次
                        while (!response.isSuccessful && retryCount < retryNum) {
                            retryCount++
                            response.close()
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