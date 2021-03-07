package com.example.moneycounter4.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.moneycounter4.base.BaseViewModel
import com.example.moneycounter4.beannew.DynamicItem
import com.example.moneycounter4.model.Config
import com.example.moneycounter4.network.*

class CommunityViewModel: BaseViewModel() {

    val dynamicList: MutableLiveData<ArrayList<DynamicItem>> = MutableLiveData(arrayListOf())

    fun getAllDynamic(pos: Int, size: Int, topic: String) {
        val list = ArrayList<DynamicItem>()
        ApiGenerator.getApiService(Api::class.java).getAllDynamic(pos, size, topic)
            .checkApiError()
            .setSchedulers()
            .doOnError {
                toastEvent.value = "请求失败！"
            }.doFinally {
//                progressDialogEvent.value = ""
            }
            .safeSubscribeBy {
                dynamicList.postValue(it as ArrayList<DynamicItem>?)
            }.lifeCycle()
    }

}