package com.example.moneycounter4.viewmodel

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.example.moneycounter4.base.BaseViewModel
import com.example.moneycounter4.bean.TypeItem
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.model.TypeIndex
import com.example.moneycounter4.widgets.LogW

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel : BaseViewModel() {


    val year = ObservableField<Int>()
    val month = ObservableField<Int>()
    val income = ObservableField<Double>()
    val expend = ObservableField<Double>()
    var selectedYear = 0
    val list = mutableListOf<CounterDataItem>()
    val typeListOut = mutableListOf<TypeItem>()
    val typeListIn = mutableListOf<TypeItem>()


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
            DataReader.OPTION_BY_MONTH
        )
        tmpList.forEach { list.add(it) }
        //初始化收入图标种类列表
        val tmpList2 = DataReader.getType(DataReader.OPTION_IN)
        if (tmpList2.size == 0) {
            TypeIndex.getInTypeInit().forEach { typeListIn.add(it) }
        } else {
            tmpList2.forEach { typeListIn.add(it) }
        }
        //初始化支出图标种类列表
        val tmpList3 = DataReader.getType(DataReader.OPTION_OUT)
        if (tmpList3.size == 0) {
            TypeIndex.getOutTypeInit().forEach { typeListOut.add(it) }
        } else {
            tmpList3.forEach { typeListOut.add(it) }
        }
        refreshList()
    }

    fun refreshList() {
        list.clear()
        list.addAll(
            DataReader.getCounterItems(
                year.get()!!,
                month.get()!!,
                0,
                DataReader.OPTION_BY_MONTH
            )
        )
        income.set(DataReader.count(list, DataReader.OPTION_INCOME))
        expend.set(DataReader.count(list, DataReader.OPTION_EXPEND))
        list.sortBy { -it.time!! }
    }





    fun addType(typeItem: TypeItem, option: Int) {
        when (option) {
            DataReader.OPTION_IN -> {
                typeListIn.add(typeItem)
                DataReader.saveType(typeListIn, option)
            }
            DataReader.OPTION_OUT -> {
                typeListOut.add(typeItem)
                DataReader.saveType(typeListOut, option)
            }
        }

    }

    fun delType(typeItem: TypeItem) {
        typeListOut.remove(typeItem)
        typeListIn.remove(typeItem)
        DataReader.saveType(typeListOut, DataReader.OPTION_OUT)
        DataReader.saveType(typeListIn, DataReader.OPTION_IN)
    }
    fun saveType() {
        DataReader.saveType(typeListOut, DataReader.OPTION_OUT)
        DataReader.saveType(typeListIn, DataReader.OPTION_IN)
    }


}