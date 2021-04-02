package com.example.moneycounter4.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.moneycounter4.base.BaseViewModel
import com.example.moneycounter4.bean.TranData

/**
 *@author zhangzhe
 *@date 2021/4/2
 *@description
 */

class MoneyEditViewModel : BaseViewModel() {

    val tranData = MutableLiveData<TranData>()

}