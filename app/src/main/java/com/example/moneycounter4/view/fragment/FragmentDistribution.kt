package com.example.moneycounter4.view.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.model.dao.DateItem
import com.example.moneycounter4.model.dao.getByDuration
import com.example.moneycounter4.utils.CalendarUtil
import com.example.moneycounter4.view.adapter.LogRecyclerViewAdapter
import com.example.moneycounter4.viewmodel.MainViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_distribution.*
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.N)

/**
 * putBoolean: use_week 是否使用首页星期的周期获取数据 ，为false则使用传入的以下参数
 * putInt: year month day last_day 年月日和期限（年月日后多少天）
 */

class FragmentDistribution : BaseViewModelFragment<MainViewModel>() {

    override fun useActivityViewModel() = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_distribution, container, false)
    }


    private var label = true
    private val arrayList = ArrayList<CounterDataItem>()
    lateinit var adapter: LogRecyclerViewAdapter

    private var year = 2020
    private var month = 1
    private var day = 1
    private var lastDay = 1


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textViewFinish3.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentDistribution_pop)
        }
        label = arguments?.getBoolean("label", true)!!

        year = arguments?.getInt("year", 2020) ?: 2020
        month = arguments?.getInt("month", 1) ?: 1
        day = arguments?.getInt("day", 1) ?: 1
        lastDay = arguments?.getInt("last_day", 1) ?: 1


            adapter = LogRecyclerViewAdapter(
            this,
            viewModel,
            arrayList,
            rv_list
        )



        iv_filter.setOnClickListener {
            drawer_layout.open()
        }
        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {
                refresh()
            }

            override fun onDrawerStateChanged(newState: Int) {
            }

        })


        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                refresh()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })


        pullRefreshLayout.setOnRefreshListener {
            refresh()
            pullRefreshLayout.setRefreshing(false)
        }


        rv_list.layoutManager = LinearLayoutManager(requireContext())
        rv_list.adapter = adapter





        refresh()


    }


    private fun refresh() {
        val listData = mutableListOf<PieEntry>()
        try {
            val map: HashMap<String, Double> = HashMap()
            var list: List<CounterDataItem>?



            if (!label) {
                list = DataReader.db?.counterDao()
                    ?.getByDuration(
                        DateItem(year, month, day),
                        0L,
                        86400000L * lastDay,
                        -1
                    )
            } else {
                /**
                 * 使用首页的星期图的周期作为筛选周期，获得记账数据
                 */
                val week = CalendarUtil.getWeek()
                val option = DataReader.OPTION_EXPEND
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
            pieDataSet.setColors(

                Color.parseColor("#F1C6E7"),
                Color.parseColor("#8AC6D1"),
                Color.parseColor("#C6F1D6"),
                Color.parseColor("#E0F5B9"),
                Color.parseColor("#E5B0EA"),
                Color.parseColor("#FAE3D9"),
                Color.parseColor("#FFB6B9"),
                Color.parseColor("#BBDED6"),
                Color.parseColor("#F9FCE1"),
                Color.parseColor("#D3F6F3"),
                Color.parseColor("#FBD1B7"),
                Color.parseColor("#FEE9b2")

            )

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