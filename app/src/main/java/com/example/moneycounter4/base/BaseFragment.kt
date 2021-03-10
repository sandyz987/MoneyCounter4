package com.example.moneycounter4.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 *@author zhangzhe
 *@date 2021/3/6
 *@description
 */

open class BaseFragment : Fragment() {

    fun <T> MutableLiveData<T>.observe(t: ((T) -> Unit)) {
        this.observe(viewLifecycleOwner, Observer { t.invoke(it) })
    }

    fun <T> MutableLiveData<T>.observeNotNull(t: ((T) -> Unit)) {
        this.observe(viewLifecycleOwner, Observer { it?.let { t.invoke(it) } })
    }
}