package com.example.moneycounter4.utils

import com.example.moneycounter4.model.dao.DateItem
import java.text.SimpleDateFormat
import java.util.*

object CalendarUtil {

    val sdf = SimpleDateFormat("yyyy-MM-dd")

    fun getCalendar(year: Int, month: Int, day: Int) = getCalendar().apply {
        set(year, month - 1, day)
        setDefaultTime()
    }

    fun getDateItem(): DateItem {
        val cal = getCalendar()
        return DateItem(cal.getYear(), cal.getMonth(), cal.getDay())
    }


    fun getCalendar(dateItem: DateItem): Calendar =
        getCalendar(dateItem.year, dateItem.month, dateItem.day)

    fun getCalendar(): Calendar = Calendar.getInstance().apply {
        firstDayOfWeek = Calendar.MONDAY
    }

    fun getFirstDayOfWeek(week: Int): DateItem {
        val cal = getCalendar()
        cal.set(Calendar.WEEK_OF_YEAR, week)

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        return DateItem(cal.getYear(), cal.getMonth(), cal.getDay())
    }

    fun getWeek(): Int {
        val cal = getCalendar()
        cal.firstDayOfWeek = Calendar.MONDAY
        return cal.get(Calendar.WEEK_OF_YEAR)
    }

    fun getWeek(dateItem: DateItem): Int {
        val cal = getCalendar(dateItem)
        cal.firstDayOfWeek = Calendar.MONDAY
        return cal.get(Calendar.WEEK_OF_YEAR)
    }

    fun getEveryFirstDayOfWeek(year: Int): List<DateItem> {
        val list = mutableListOf<DateItem>()
        val cal = getCalendar()
        cal.set(Calendar.YEAR, year)
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

/**
 * 到本周的星期几
 * @param destination 星期几 （1-7）  星期天是1  星期一是2
 */
fun DateItem.mapDayOfWeek(destination: Int): DateItem {
    val cal = CalendarUtil.getCalendar(this)

    while (cal.get(Calendar.DAY_OF_WEEK) != destination) {
        cal.add(Calendar.DATE, -1)
    }

    return this.apply {
        year = cal.getYear()
        month = cal.getMonth()
        day = cal.getDay()
    }
}

/**
 * 到本月的几号
 * @param destination 几号
 */
fun DateItem.mapDayOfMonth(destination: Int): DateItem {
    val cal = CalendarUtil.getCalendar(this)


    cal.set(Calendar.DATE, destination)

    if (day < destination) {
        cal.add(Calendar.MONTH, -1)
    }

    return this.apply {
        year = cal.getYear()
        month = cal.getMonth()
        day = cal.getDay()
    }
}

/**
 * 本月的天数
 */
fun DateItem.getDayCountOfMonth(): Int {
    return CalendarUtil.getCalendar(this).getActualMaximum(Calendar.DAY_OF_MONTH)
}

fun Calendar.toDateItem(): DateItem {
    return DateItem(getYear(), getMonth(), getDay())
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

fun Calendar.setDefaultTime() {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
}