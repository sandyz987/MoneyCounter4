package com.example.moneycounter4.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneycounter4.R
import com.example.moneycounter4.view.fragment.FragmentGraph
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter

/**
 *@author zhangzhe
 *@date 2021/6/6
 *@description
 */

class ChartAdapter(var list: List<LineData>) :
    RecyclerView.Adapter<ChartAdapter.ChartViewHolder>() {

    inner class ChartViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var chart: LineChart? = null
        var textViewTag: TextView? = null

        init {
            chart = v.findViewById(R.id.chart)
            textViewTag = v.findViewById(R.id.textViewTag)
        }
    }

    fun refresh(list: List<LineData>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_linechart, parent, false)
        return ChartViewHolder(v)
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        holder.chart?.setTouchEnabled(false)

        // apply styling

        list[position].setValueTypeface(FragmentGraph.tfLight)
        list[position].setValueTextColor(Color.BLACK)

        holder.chart?.description?.isEnabled = false
        holder.chart?.setDrawGridBackground(false)

        val xAxis: XAxis? = holder.chart?.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.typeface = FragmentGraph.tfLight
        xAxis?.setDrawGridLines(true)
        xAxis?.valueFormatter = object : ValueFormatter() {
            val weekday = listOf("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日")
            override fun getFormattedValue(value: Float): String {
                return weekday[(value + 0.5f).toInt() - 1]
            }
        }

        val leftAxis: YAxis? = holder.chart?.axisLeft
        leftAxis?.typeface = FragmentGraph.tfLight
        leftAxis?.setLabelCount(5, false)
        leftAxis?.spaceTop = 15f
        val rightAxis: YAxis? = holder.chart?.axisRight
        rightAxis?.typeface = FragmentGraph.tfLight
        rightAxis?.setLabelCount(5, false)
        rightAxis?.spaceTop = 15f

        list[position].setValueTextSize(14f)
        holder.chart?.data = list[position]


    }

    override fun getItemCount() = list.size

}