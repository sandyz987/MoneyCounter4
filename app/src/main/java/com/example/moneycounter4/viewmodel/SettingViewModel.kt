package com.example.moneycounter4.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.moneycounter4.base.BaseViewModel
import com.example.moneycounter4.beannew.User
import com.example.moneycounter4.network.*

class SettingViewModel : BaseViewModel() {

    val user = MutableLiveData<User>()

    fun getUser(userId: String) {
        ApiGenerator.getApiService(Api::class.java).getUserInfo(userId)
            .checkApiError()
            .setSchedulers()
            .doOnError {
                toastEvent.value = "获取用户信息失败！"
            }.safeSubscribeBy {
                user.postValue(it)
            }.lifeCycle()
    }

    fun changeUserInfo(userInfo: String) {
        ApiGenerator.getApiService(Api::class.java).changeUserInfo(userInfo)
            .checkError()
            .setSchedulers()
            .doOnError {
                toastEvent.value = "修改用户信息失败！"
            }.safeSubscribeBy {
                toastEvent.value = "修改用户信息成功！"
            }.lifeCycle()
    }
}