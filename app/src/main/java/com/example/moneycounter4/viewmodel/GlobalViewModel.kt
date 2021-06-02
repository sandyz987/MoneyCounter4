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

/**
 * 全局viewModel，一般把共享的东西放置在这里面。
 */


@RequiresApi(Build.VERSION_CODES.N)
class GlobalViewModel : BaseViewModel() {


    val typeListOut = mutableListOf<TypeItem>()
    val typeListIn = mutableListOf<TypeItem>()


    init {

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