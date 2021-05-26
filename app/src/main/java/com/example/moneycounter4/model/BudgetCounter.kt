package com.example.moneycounter4.model

import android.util.Log
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.model.dao.getByDuration
import com.example.moneycounter4.utils.CalendarUtil
import com.example.moneycounter4.utils.getDayCountOfMonth
import com.example.moneycounter4.utils.mapDayOfMonth
import com.example.moneycounter4.utils.mapDayOfWeek

/**
 *@author zhangzhe
 *@date 2021/4/4
 *@description
 */

object BudgetCounter {

    fun getList(budgetPeriod: String, budgetStartDate: Int): List<CounterDataItem> {
        val list = mutableListOf<CounterDataItem>()
        when (budgetPeriod) {
            "周" -> {
                DataReader.db?.counterDao()?.getByDuration(
                    CalendarUtil.getDateItem().mapDayOfWeek(budgetStartDate),
                    0L,
                    7 * 86400000L,
                    DataReader.OPTION_EXPEND
                )?.forEach {
                    list.add(it)
                }
                return list
            }
            "月" -> {
                Log.e(
                    "sandyzhang",
                    CalendarUtil.getDateItem().mapDayOfMonth(budgetStartDate).toString()
                )
                DataReader.db?.counterDao()?.getByDuration(
                    CalendarUtil.getDateItem().mapDayOfMonth(budgetStartDate),
                    0L,
                    CalendarUtil.getDateItem().getDayCountOfMonth() * 86400000L,
                    DataReader.OPTION_EXPEND
                )?.forEach {
                    list.add(it)
                }
                return list
            }
            else -> {
                return emptyList()
            }
        }
    }

}