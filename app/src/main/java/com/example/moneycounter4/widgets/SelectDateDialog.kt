package com.example.moneycounter4.widgets

import android.content.Context
import com.bigkoo.pickerview.builder.TimePickerBuilder

/**
 *@author zhangzhe
 *@date 2021/6/7
 *@description
 */

class SelectDateDialog(
    private val context: Context,
    private val scale: String,
    private val onOptionSelected: ((Int, Int, Int) -> Unit)
) {


    fun show() {
        val pvTime =
            TimePickerBuilder(
                context
            ) { date, _ ->
                onOptionSelected.invoke(date.year + 1900, date.month + 1, date.date)
            }
                .apply {
                    when (scale) {
                        "year" -> setType(booleanArrayOf(true, false, false, false, false, false))
                        "month" -> setType(booleanArrayOf(true, true, false, false, false, false))
                        "day" -> setType(booleanArrayOf(true, true, true, false, false, false))
                        else -> setType(booleanArrayOf(true, true, true, false, false, false))
                    }
                }
                .build()
        pvTime.show()

    }
}