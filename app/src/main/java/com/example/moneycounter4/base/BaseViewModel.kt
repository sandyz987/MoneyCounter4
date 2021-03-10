package com.example.moneycounter4.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneycounter4.widgets.SingleLiveEvent
import io.reactivex.disposables.Disposable

/**
 *@author zhangzhe
 *@date 2021/3/6
 *@description
 */

open class BaseViewModel : ViewModel() {
    val toastEvent = SingleLiveEvent<String>()

    private val disposableList = mutableListOf<Disposable>()

    val progressDialogEvent = SingleLiveEvent<String>()

    fun Disposable.lifeCycle(): Disposable {
        disposableList.add(this)
        return this@lifeCycle
    }

    fun toast(msg: String) {
        toastEvent.value = msg
    }

    override fun onCleared() {
        super.onCleared()
        disposeAll()
    }

    fun disposeAll() {
        disposableList.asSequence()
            .filterNot { it.isDisposed }
            .forEach { it.dispose() }
        disposableList.clear()
    }
}