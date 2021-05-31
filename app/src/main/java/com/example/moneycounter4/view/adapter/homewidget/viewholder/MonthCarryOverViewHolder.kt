package com.example.moneycounter4.view.adapter.homewidget.viewholder

import android.view.View
import android.widget.TextView
import com.example.moneycounter4.R
import com.example.moneycounter4.view.adapter.homewidget.WidgetAdapter

/**
 *@author zhangzhe
 *@date 2021/5/31
 *@description 月结余控件的viewholder
 */

class MonthCarryOverViewHolder(v: View, t: Int) : WidgetAdapter.BaseViewHolder(v, t) {
    val tv_money_last = v.findViewById<TextView>(R.id.tv_money_last)
    val tv_money_out_month = v.findViewById<TextView>(R.id.tv_money_out_month)
    val tv_money_in_month = v.findViewById<TextView>(R.id.tv_money_in_month)

}