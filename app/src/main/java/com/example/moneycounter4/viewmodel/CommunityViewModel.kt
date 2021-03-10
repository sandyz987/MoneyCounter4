package com.example.moneycounter4.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.moneycounter4.base.BaseViewModel
import com.example.moneycounter4.beannew.DynamicItem
import com.example.moneycounter4.beannew.ReplyInfo
import com.example.moneycounter4.model.Config
import com.example.moneycounter4.network.*
import com.example.moneycounter4.widgets.SingleLiveEvent

class CommunityViewModel : BaseViewModel() {

    val dynamicList: MutableLiveData<ArrayList<DynamicItem>> = MutableLiveData(arrayListOf())

    val releaseDynamicStatus: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val replyStatus: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val replyInfo: MutableLiveData<ReplyInfo> = MutableLiveData()


    fun refreshDynamic() {
        getAllDynamic(0, 50, "广场")
    }

    fun getAllDynamic(pos: Int, size: Int, topic: String) {
        ApiGenerator.getApiService(Api::class.java).getAllDynamic(pos, size, topic)
            .checkApiError()
            .setSchedulers()
            .doOnError {
                toastEvent.value = "请求失败！"
            }.safeSubscribeBy {
                dynamicList.value = (it as ArrayList<DynamicItem>?)
            }.lifeCycle()
    }


    fun releaseDynamic(text: String, topic: String) {
        progressDialogEvent.value = "正在上传中..."
        ApiGenerator.getApiService(Api::class.java).releaseDynamic(text, topic)
            .checkError()
            .setSchedulers()
            .doOnError {
                toastEvent.value = "发送帖子失败！"
                toastEvent.value = "发送成功！"
            }.doFinally {
                progressDialogEvent.value = ""
            }.safeSubscribeBy {
                releaseDynamicStatus.postValue(true)
            }.lifeCycle()
    }

    /**
     * @param which 0为直接回复帖子 1为回复帖子下面的评论 2为回复二级评论
     */
    private fun reply(text: String, replyId: Int, which: Int) {
        progressDialogEvent.value = "正在上传中..."
        ApiGenerator.getApiService(Api::class.java).reply(text, replyId, which)
            .checkError()
            .setSchedulers()
            .doOnError {
                toastEvent.value = "回复失败！"
            }.doFinally {
                progressDialogEvent.value = ""
            }.safeSubscribeBy {
                toastEvent.value = "回复成功！"
                replyStatus.postValue(true)
            }.lifeCycle()
    }

    fun reply(text: String) {
        toastEvent.value = text
        replyInfo.value?.let {
            if (it.replyId != -1 && it.which != -1 && text.isNotBlank()) {
                reply(text, it.replyId, it.which)
            } else {
                toastEvent.value = "回复参数不合法"
            }
        }
    }

}