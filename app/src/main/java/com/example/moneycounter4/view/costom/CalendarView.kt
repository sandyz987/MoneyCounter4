package com.example.moneycounter4.view.costom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.moneycounter4.R
import com.example.moneycounter4.extensions.dp2px
import kotlinx.android.synthetic.main.layout_calendar.view.*
import java.util.*

/**
 *@author zhangzhe
 *@date 2021/4/4
 *@description
 */

class CalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        addView(LayoutInflater.from(context).inflate(R.layout.layout_calendar, this, false))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 设置固定大小
        setMeasuredDimension(context.dp2px(150f), context.dp2px(180f))
    }

    @SuppressLint("SetTextI18n")
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val cal = Calendar.getInstance()
        tv_year_month.text = "${cal.get(Calendar.YEAR)}.${cal.get(Calendar.MONTH) + 1}"
        tv_day.text = "${cal.get(Calendar.DATE)}"
        val weekdayString =
            listOf(
                "",
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday"
            )
        tv_weekday.text = weekdayString[cal.get(Calendar.DAY_OF_WEEK)]
    }

}