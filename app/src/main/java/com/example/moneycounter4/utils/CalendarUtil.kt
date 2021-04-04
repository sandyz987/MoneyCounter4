package com.example.moneycounter4.utils

import com.example.moneycounter4.model.dao.DateItem
import java.text.SimpleDateFormat
import java.util.*

object CalendarUtil {

    val sdf = SimpleDateFormat("yyyy-MM-dd")

    fun getCalendar(year: Int, month: Int, day: Int) = Calendar.getInstance().apply {
        set(year, month - 1, day)
    }


    fun getCalendar(): Calendar = Calendar.getInstance()

    fun getFirstDayOfWeek(week: Int): DateItem {
        val cal = Calendar.getInstance()
        cal.set(Calendar.WEEK_OF_YEAR, week)
        cal.firstDayOfWeek = Calendar.MONDAY

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONTH)
        return DateItem(cal.getYear(), cal.getMonth(), cal.getDay())
    }

    fun getEveryFirstDayOfWeek(year: Int): List<DateItem> {
        val list = mutableListOf<DateItem>()
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.firstDayOfWeek = Calendar.MONDAY
        var week = 1
        var b = true
        while (b) {
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.WEEK_OF_YEAR, week)
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            DateItem(cal.getYear(), cal.getMonth(), cal.getDay()).apply {
                if (this.year <= year) {
                    list.add(this)
                } else {
                    b = false
                }
            }
            week++
        }



        return list
    }

}

fun main() {
    CalendarUtil.getEveryFirstDayOfWeek(2021).forEach {
        println(it)
    }
}


fun Calendar.getMonth(): Int {
    return get(Calendar.MONTH) + 1
}

fun Calendar.getYear(): Int {
    return get(Calendar.YEAR)
}

fun Calendar.getDay(): Int {
    return get(Calendar.DATE)
}