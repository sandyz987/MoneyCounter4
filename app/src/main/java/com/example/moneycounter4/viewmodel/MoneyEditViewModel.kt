package com.example.moneycounter4.viewmodel

import com.example.moneycounter4.base.BaseViewModel
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.widgets.SingleLiveEvent

/**
 *@author zhangzhe
 *@date 2021/4/2
 *@description
 */

class MoneyEditViewModel : BaseViewModel() {

    val willBeAddedItem = SingleLiveEvent<CounterDataItem>()

}