package com.example.moneycounter4.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.moneycounter4.base.BaseViewModel
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.extensions.CombineLatestMediatorLiveDataOfFour
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.model.dao.DateItem
import com.example.moneycounter4.model.dao.getByDuration

/**
 *@author zhangzhe
 *@date 2021/6/6
 *@description
 */

class DistributionViewModel : BaseViewModel() {
    var year: MutableLiveData<Int> = MutableLiveData(1970)
    var month: MutableLiveData<Int> = MutableLiveData(1)
    var day: MutableLiveData<Int> = MutableLiveData(1)
    var lastDay: MutableLiveData<Int> = MutableLiveData(1)
    var dateSource = CombineLatestMediatorLiveDataOfFour<Int, Int, Int, Int, Int>(
        year,
        month, day, lastDay
    ) { _, _, _, _ ->
        0
    }

    fun getData(): List<CounterDataItem>? {
        return DataReader.db?.counterDao()
            ?.getByDuration(
                DateItem(year.value ?: 0, month.value ?: 0, day.value ?: 0),
                0L,
                86400000L * (lastDay.value ?: 0),
                -1
            )
    }
}