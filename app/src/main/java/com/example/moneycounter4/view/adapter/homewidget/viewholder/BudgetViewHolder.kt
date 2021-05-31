package com.example.moneycounter4.view.adapter.homewidget.viewholder

import android.view.View
import android.widget.TextView
import com.example.moneycounter4.R
import com.example.moneycounter4.view.adapter.homewidget.WidgetAdapter
import com.example.moneycounter4.view.costom.CounterProgressBar

/**
 *@author zhangzhe
 *@date 2021/5/31
 *@description
 */

class BudgetViewHolder(v: View, t: Int) : WidgetAdapter.BaseViewHolder(v, t) {
    val tv_money_pocket = v.findViewById<TextView>(R.id.tv_money_pocket)
    val tv_money_out = v.findViewById<TextView>(R.id.tv_money_out)
    val tv_money_rest = v.findViewById<TextView>(R.id.tv_money_rest)
    val counter_pb_main = v.findViewById<CounterProgressBar>(R.id.counter_pb_main)
    val tv_budget_setting = v.findViewById<TextView>(R.id.tv_budget_setting)

}