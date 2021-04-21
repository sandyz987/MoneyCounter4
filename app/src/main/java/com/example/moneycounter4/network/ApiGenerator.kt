package com.example.moneycounter4.network

import android.util.Log
import android.widget.Toast
import com.example.moneycounter4.BuildConfig
import com.example.moneycounter4.beannew.ApiWrapper
import com.example.moneycounter4.beannew.InfoWrapper
import com.example.moneycounter4.beannew.Token
import com.example.moneycounter4.beannew.isSuccessful
import com.example.moneycounter4.model.Config
import com.example.moneycounter4.model.networkcache.NetworkCache
import com.example.moneycounter4.viewmodel.MainApplication
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import okio.GzipSource
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
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

fun Observable<InfoWrapper>.checkError(): Observable<InfoWrapper> = map {
    if (it.isSuccessful) {
        it
    } else {
        throw ApiException(it.info, it.status)
    }
}

fun <T> Observable<ApiWrapper<T>>.checkApiError(): Observable<T> = map {
    if (it.isSuccessful) {
        it.data ?: throw ApiException(it.info, it.status)
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
    private const val retryNum = 1

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
                connectTimeout(5, TimeUnit.SECONDS)
                interceptors().add(object : Interceptor { // 通过添加拦截器来实现自动化token管理
                    override fun intercept(chain: Interceptor.Chain): Response {
                        var retryCount = 0
                        var request =
                            chain.request().newBuilder().header("token", token.toString()).build()
                        var response = retry(request, chain)
                        if (response?.isSuccessful == true) {
                            saveCache(request, response)
                            Log.e("sandyzhang====", response.body?.contentType().toString() + "   6")

                            return response
                        }
                        // 如果失败，获取一次token
                        token = try {
                            getCommonApiService(LoginApi::class.java).getToken(
                                Config.userId,
                                Config.password
                            ).execute().body()?.data?.token ?: 0
                        } catch (e: Exception) {
                            0
                        }

                        response?.close()
                        // 获取完token再看看能不能请求成功
                        request =
                            chain.request().newBuilder().header("token", token.toString()).build()
                        response = retry(request, chain)
                        // 如果失败则重试3次
                        while (response?.isSuccessful != true && retryCount < retryNum) {
                            retryCount++
                            response?.close()
                            response = retry(request, chain)
                        }
                        if (response?.isSuccessful != true) {
//                            Toast.makeText(
//                                MainApplication.context,
//                                "请求失败，使用缓存数据！",
//                                Toast.LENGTH_SHORT
//                            ).show()
                            response = Response.Builder().code(200)
                                .message("OK")
                                .protocol(Protocol.HTTP_1_1)
                                .body(getCache(request)?.toResponseBody(response?.body?.contentType()))
                                .request(Request.Builder().url("http://localhost/").build())
                                .build()
                        }
                        saveCache(request, response)

                        return response
                    }
                })
            }.build())
        }.build()
        retrofitCommon = Retrofit.Builder().apply {
            baseUrl(Config.MainUrl)
            client(OkHttpClient().newBuilder().apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(logging)
                }
                addConverterFactory(GsonConverterFactory.create())
                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                connectTimeout(5, TimeUnit.SECONDS)
            }.build())
        }.build()
    }

    fun retry(request: Request, chain: Interceptor.Chain): Response? {
        return try {
            chain.proceed(request)
        } catch (e: Exception) {
            null
        }
    }

    fun getCache(request: Request): String? {
        val buffer2 = Buffer()
        request.body?.writeTo(buffer2)
        val contentType2 = request.body?.contentType()
        val charset2: Charset =
            contentType2?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
        val args = buffer2.readString(charset2)
        return NetworkCache.getCache("${request.url}?${args}")

    }

    fun saveCache(request: Request, response: Response) {
        val source = response.body?.source() ?: return
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer
        val contentType = response.body?.contentType()
        val charset: Charset =
            contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8


        val buffer2 = Buffer()
        request.body?.writeTo(buffer2)
        val contentType2 = request.body?.contentType()
        val charset2: Charset =
            contentType2?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
        val args = buffer2.readString(charset2)

        NetworkCache.addCache("${request.url}?${args}", buffer.clone().readString(charset))
        Log.e(
            "sandyzhang6",
            "${request.url}?${args}" + "*" + buffer.clone().readString(charset) + " ==${buffer.size}"
        )
    }

    fun <T> getCommonApiService(clazz: Class<T>) = retrofitCommon.create(clazz)

    fun <T> getApiService(clazz: Class<T>) = retrofit.create(clazz)


}