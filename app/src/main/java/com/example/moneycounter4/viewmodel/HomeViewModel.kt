package com.example.moneycounter4.viewmodel


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import com.example.moneycounter4.base.BaseViewModel
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.widgets.LogW
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
class HomeViewModel: BaseViewModel() {

    val year = ObservableField<Int>()
    val month = ObservableField<Int>()
    val income = ObservableField<Double>()
    val expend = ObservableField<Double>()
    var selectedYear = 0
    val list = mutableListOf<CounterDataItem>()


    init {

        LogW.d(this.toString())
        //初始化年月显示
        month.set(Calendar.getInstance().get(Calendar.MONTH) + 1)
        year.set(Calendar.getInstance().get(Calendar.YEAR))
        val calendar = java.util.Calendar.getInstance()
        selectedYear = calendar.get(java.util.Calendar.YEAR)
        //初始化记账记录列表
        val tmpList = DataReader.getCounterItems(
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH) + 1,
            Calendar.getInstance().get(Calendar.DATE),
            DataReader.OPTION_GET_BY_MONTH
        )
        tmpList.forEach { list.add(it) }

        refreshList()
    }

    fun refreshList() {
        list.clear()
        list.addAll(
            DataReader.getCounterItems(
                year.get()!!,
                month.get()!!,
                0,
                DataReader.OPTION_GET_BY_MONTH
            )
        )
        income.set(DataReader.count(list, DataReader.OPTION_INCOME))
        expend.set(DataReader.count(list, DataReader.OPTION_EXPEND))
        list.sortBy { -it.time!! }
    }

}