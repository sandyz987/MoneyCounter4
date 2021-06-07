package com.example.moneycounter4.view.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.model.dao.DateItem
import com.example.moneycounter4.model.dao.getByDuration
import com.example.moneycounter4.utils.*
import com.example.moneycounter4.view.adapter.LogRecyclerViewAdapter
import com.example.moneycounter4.viewmodel.DistributionViewModel
import com.example.moneycounter4.viewmodel.GlobalViewModel
import com.example.moneycounter4.widgets.SelectDateDialog
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_distribution.*
import kotlinx.android.synthetic.main.layout_header_filter.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.N)

/**
 * putBoolean: use_week 是否使用首页星期的周期获取数据 ，为false则使用传入的以下参数
 * putInt: year month day last_day 年月日和期限（年月日后多少天）
 */

class FragmentDistribution : BaseViewModelFragment<DistributionViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_distribution, container, false)
    }


    private var label = true
    private val arrayList = ArrayList<CounterDataItem>()
    lateinit var adapter: LogRecyclerViewAdapter


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_finish.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentDistribution_pop)
        }
        label = arguments?.getBoolean("label", false) ?: false

        val dataItem = CalendarUtil.getDateItem()
        viewModel.lastDay.value = arguments?.getInt("last_day", 1) ?: 1
        viewModel.year.value = arguments?.getInt("year", dataItem.year) ?: dataItem.year
        viewModel.month.value = arguments?.getInt("month", dataItem.month) ?: dataItem.month
        viewModel.day.value = arguments?.getInt("day", dataItem.day) ?: dataItem.day



        adapter = LogRecyclerViewAdapter(
            this,
            ViewModelProviders.of(requireActivity()).get(GlobalViewModel::class.java),
            arrayList,
            rv_list
        )






        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                refreshData()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })





        rv_list.layoutManager = LinearLayoutManager(requireContext())
        rv_list.adapter = adapter





        pullRefreshLayout.setOnRefreshListener {
            refreshData()
            pullRefreshLayout.setRefreshing(false)
        }
        refreshData()

        refreshNav()

    }

    @SuppressLint("SetTextI18n")
    private fun refreshNav() {

        if (label) {
            nav_view.getHeaderView(0).cl_date_layout?.visibility = View.GONE
        } else {
            nav_view.getHeaderView(0).cl_date_layout?.visibility = View.VISIBLE
        }
        iv_filter.setOnClickListener {
            drawer_layout.open()
        }


        viewModel.dateSource.observe {
            nav_view.getHeaderView(0).tv_date_select.text = "${
                DateItem(
                    viewModel.year.value ?: 0,
                    viewModel.month.value ?: 0,
                    viewModel.day.value ?: 0
                )
            } - ${
                CalendarUtil.getCalendar(
                    DateItem(
                        viewModel.year.value ?: 0,
                        viewModel.month.value ?: 0,
                        viewModel.day.value ?: 0
                    )
                ).apply { add(Calendar.DATE, (viewModel.lastDay.value ?: 0) - 1) }.toDateItem()
            }"

            nav_view.getHeaderView(0).cl_date_select_outer.setOnClickListener {
                SelectDateDialog(
                    requireContext(), when (viewModel.lastDay.value) {
                        1 -> "day"
                        28, 29, 30, 31 -> "month"
                        365, 366 -> "year"
                        else -> "day"
                    }
                ) { y, m, d ->

                    when (viewModel.lastDay.value) {
                        1 -> {
                            viewModel.year.value = y
                            viewModel.month.value = m
                            viewModel.day.value = d
                        }
                        28, 29, 30, 31 -> {
                            viewModel.year.value = y
                            viewModel.month.value = m
                            viewModel.day.value = 1
                        }
                        365, 366 -> {
                            viewModel.year.value = y
                            viewModel.month.value = 1
                            viewModel.day.value = 1
                        }
                        else -> {
                            viewModel.year.value = y
                            viewModel.month.value = m
                            viewModel.day.value = d
                        }
                    }

                }.show()
            }

        }

        nav_view.getHeaderView(0).flow_date_select?.apply {
            setOptions("年", "月", "日")
            singleSelect = true
            reverseSelect("日")
            onSelect = {
                Log.e("sandyzhang", it.toString())
                if (it.isEmpty()) {
                    viewModel.lastDay.value = 36500
                    viewModel.year.value = 1970
                    viewModel.month.value = 1
                    viewModel.day.value = 1
                } else {
                    when (it[0]) {
                        "年" -> {
                            val dateItem = CalendarUtil.getDateItem()
                            viewModel.lastDay.value = dateItem.getDayCountOfYear()
                            viewModel.year.value = dateItem.year
                            viewModel.month.value = 1
                            viewModel.day.value = 1
                        }
                        "月" -> {
                            val dateItem = CalendarUtil.getDateItem().mapDayOfMonth(1)
                            viewModel.lastDay.value = dateItem.getDayCountOfMonth()
                            viewModel.year.value = dateItem.year
                            viewModel.month.value = dateItem.month
                            viewModel.day.value = 1
                        }
                        "日" -> {
                            val dateItem = CalendarUtil.getDateItem()
                            viewModel.lastDay.value = 1
                            viewModel.year.value = dateItem.year
                            viewModel.month.value = dateItem.month
                            viewModel.day.value = dateItem.day
                        }

                    }
                }
            }
        }

        nav_view.getHeaderView(0).flow_account_select?.apply {

        }
        nav_view.getHeaderView(0).flow_out_select?.apply {
            setOptions(
            )
            singleSelect = false
        }
        nav_view.getHeaderView(0).flow_in_select?.apply {
            setOptions(
            )
            singleSelect = false

        }
        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
                refreshData()
            }

            override fun onDrawerStateChanged(newState: Int) {
            }

        })
    }


    @SuppressLint("SetTextI18n")
    private fun refreshData() {
        val listData = mutableListOf<PieEntry>()
        try {
            val map: HashMap<String, Double> = HashMap()
            var list: List<CounterDataItem>?



            if (!label) {
                list = viewModel.getData()
                tv_date.visibility = View.VISIBLE
                tv_date.text = "日期：${
                    DateItem(
                        viewModel.year.value ?: 0,
                        viewModel.month.value ?: 0,
                        viewModel.day.value ?: 0
                    )
                } - ${
                    CalendarUtil.getCalendar(
                        DateItem(
                            viewModel.year.value ?: 0,
                            viewModel.month.value ?: 0,
                            viewModel.day.value ?: 0
                        )
                    ).apply { add(Calendar.DATE, (viewModel.lastDay.value ?: 0) - 1) }.toDateItem()
                }"
                tv_finish.visibility = View.GONE

            } else {
                /**
                 * 使用首页的星期图的周期作为筛选周期，获得记账数据
                 */
                tv_date.visibility = View.GONE

                val week = CalendarUtil.getWeek()
                val option = -1
                list = DataReader.db?.counterDao()
                    ?.getByDuration(
                        CalendarUtil.getFirstDayOfWeek(week),
                        0L,
                        86400000L * 7,
                        option
                    )
            }

            list = list!!.filter {
                when (tab_layout.selectedTabPosition) {
                    0 -> it.money!! < 0
                    1 -> it.money!! > 0
                    else -> it.money!! < 0
                }

            }




            arrayList.clear()
            arrayList.addAll(list.sortedBy {
                -it.time!!
            })
            adapter.notifyDataSetChanged()


            if (arrayList.isNullOrEmpty()) {
                tv_no_log_hint.visibility = View.VISIBLE
                constraintLayout7.visibility = View.INVISIBLE
            } else {
                tv_no_log_hint.visibility = View.INVISIBLE
                constraintLayout7.visibility = View.VISIBLE
            }


            list.forEach {
                if (!it.type.isNullOrBlank()) {
                    map[it.type!!] = (map[it.type!!] ?: 0.0) + it.money!!
                }
            }
            val ite = map.iterator()
            while (ite.hasNext()) {
                val it = ite.next()
                listData.add(PieEntry(abs(it.value.toFloat()), it.key))
            }
            val pieDataSet = PieDataSet(listData, "饼状图")
            val pieData = PieData(pieDataSet)
            pie_chart.data = pieData
            pieDataSet.colors = ColorTemplate.VORDIPLOM_COLORS.toList()
//            pieDataSet.setColors(
//
//                Color.parseColor("#F1C6E7"),
//                Color.parseColor("#8AC6D1"),
//                Color.parseColor("#C6F1D6"),
//                Color.parseColor("#E0F5B9"),
//                Color.parseColor("#E5B0EA"),
//                Color.parseColor("#FAE3D9"),
//                Color.parseColor("#FFB6B9"),
//                Color.parseColor("#BBDED6"),
//                Color.parseColor("#F9FCE1"),
//                Color.parseColor("#D3F6F3"),
//                Color.parseColor("#FBD1B7"),
//                Color.parseColor("#FEE9b2")
//
//            )

            val description = Description()
            description.isEnabled = false
            pie_chart.description = description
            //设置半透明圆环的半径, 0为透明
            //设置半透明圆环的半径, 0为透明
            pie_chart.transparentCircleRadius = 0f

            //设置初始旋转角度

            //设置初始旋转角度
            pie_chart.rotationAngle = -15f

            //数据连接线距图形片内部边界的距离，为百分数

            //设置连接线的颜色
            pieDataSet.valueLineColor = Color.GRAY
            // 连接线在饼状图外面
            pieDataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

            // 设置饼块之间的间隔
            pieDataSet.sliceSpace = 1f
            pieDataSet.isHighlightEnabled = true
            // 不显示图例
            val legend: Legend = pie_chart.legend
            legend.isEnabled = false

            // 和四周相隔一段距离,显示数据

            pie_chart.setExtraOffsets(26f, 5f, 26f, 5f)

            // 设置pieChart图表是否可以手动旋转
            pie_chart.isRotationEnabled = true
            // 设置piecahrt图表点击Item高亮是否可用
            pie_chart.isHighlightPerTapEnabled = true
            // 设置pieChart图表展示动画效果，动画运行1.4秒结束
            pie_chart.animateY(1400, Easing.EaseInOutQuad)
            //设置pieChart是否只显示饼图上百分比不显示文字
            pie_chart.setDrawEntryLabels(true)
            //是否绘制PieChart内部中心文本
            pie_chart.setDrawCenterText(false)
            pie_chart.setHoleColor(Color.TRANSPARENT)
            pie_chart.setEntryLabelColor(Color.BLACK)
            // 绘制内容value，设置字体颜色大小
            pieData.setDrawValues(true)
            pieData.setValueFormatter(PercentFormatter())
            pieData.setValueTextSize(20f)
            pieData.setValueTextColor(Color.BLACK)


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}