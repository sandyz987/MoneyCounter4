package com.example.moneycounter4.view.adapter.homewidget.viewholder

import android.view.View
import com.example.moneycounter4.R
import com.example.moneycounter4.view.adapter.homewidget.WidgetAdapter
import com.example.moneycounter4.view.costom.CounterGraphView

/**
 *@author zhangzhe
 *@date 2021/5/31
 *@description 每周支出图控件的viewholder
 */

class WeekGraphViewHolder(v: View, t: Int) : WidgetAdapter.BaseViewHolder(v, t) {
    val graph_view = v.findViewById<CounterGraphView>(R.id.graph_view)
}