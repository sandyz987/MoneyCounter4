package com.example.moneycounter4.viewmodel

import android.app.Activity
import android.content.Context
import com.example.moneycounter4.base.BaseViewModel
import com.example.moneycounter4.model.Config
import com.example.moneycounter4.network.*

/**
 *@author zhangzhe
 *@date 2021/3/3
 *@description
 */

class LoginViewModel : BaseViewModel() {
    fun saveUserInfo(userId: String?, password: String?) {
        //账号密码存入sp
        val sharedPreferences =
            MainApplication.app.getSharedPreferences("usrInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_id", userId)
        editor.putString("password", password)
        editor.apply()
    }

    fun getUserInfo(): Pair<String, String> {
        var userId: String = ""
        var password: String = ""
        //从sp中读取账号密码
        val sharedPreferences =
            MainApplication.app.getSharedPreferences("usrInfo", Context.MODE_PRIVATE)
        sharedPreferences.getString("user_id", "")?.let { userId = it }
        sharedPreferences.getString("password", "")?.let { password = it }
        return Pair(userId, password)
    }

    fun checkLogin(activity: Activity, onSuccess: () -> Unit, onFailed: () -> Unit) {
        Thread {
            val t = try {
                ApiGenerator.getCommonApiService(LoginApi::class.java).getToken(
                    Config.userId,
                    Config.password
                ).execute().body()?.data?.token ?: 0
            } catch (e: Exception) {
                0
            }

            // callback
            activity.runOnUiThread {
                if (t == 0) {
                    onFailed.invoke()
                } else {
                    saveUserInfo(Config.userId, Config.password)
                    ApiGenerator.token = t
                    onSuccess.invoke()
                }
            }

        }.start()
    }

    fun register(userId: String, password: String, onSuccess: () -> Unit) {
        progressDialogEvent.value = "正在注册中，请稍后"
        ApiGenerator.getCommonApiService(Api::class.java).register(userId, password)
            .checkError()
            .setSchedulers()
            .doOnError {
                toastEvent.value = "注册失败，服务器拒绝"
            }.doFinally {
                progressDialogEvent.value = ""
            }
            .safeSubscribeBy {
                toastEvent.value = "注册成功！"
                Config.userId = userId
                Config.password = password
                saveUserInfo(Config.userId, Config.password)
                onSuccess.invoke()
            }.lifeCycle()
    }
}