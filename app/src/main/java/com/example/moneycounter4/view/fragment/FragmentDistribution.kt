package com.example.moneycounter4.view.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseFragment
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.view.adapter.LogRecyclerViewAdapter
import com.example.moneycounter4.viewmodel.MainViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_distribution.*
import kotlinx.android.synthetic.main.fragment_distribution.constraintInOrOut
import kotlinx.android.synthetic.main.fragment_distribution.textViewExpend
import kotlin.math.abs


class FragmentDistribution : BaseViewModelFragment<MainViewModel>() {

    override fun useActivityViewModel() = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_distribution, container, false)
    }


    var label = ""
    val arrayList = ArrayList<CounterDataItem>()
    @RequiresApi(Build.VERSION_CODES.N)
    lateinit var adapter: LogRecyclerViewAdapter

    /**
     * data: 要筛选的数据，list，序列画
     * label; 显示的标题
     */

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_title.text = arguments?.getString("label", "")
        textViewFinish3.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentDistribution_pop)
        }
        label = arguments?.getString("label", "")!!
        adapter = LogRecyclerViewAdapter(
            this,
            viewModel,
            arrayList,
            rv_list
        )


        constraintInOrOut.setOnClickListener {
            val options1Items = listOf("支出", "收入")
            val pvOptions: OptionsPickerView<String> =
                OptionsPickerBuilder(
                    requireContext()
                ) { options1, _, _, _ ->
                    val tx: String = options1Items[options1]
                    textViewExpend.text = tx
                    refresh()
                }.build()
            pvOptions.setPicker(options1Items)
            pvOptions.setSelectOptions(
                when (textViewExpend.text.toString()) {
                    "支出" -> 0
                    "收入" -> 1
                    else -> 0
                }
            )
            pvOptions.show()
        }


        if (label.isNotBlank()) {
            constraintInOrOut.visibility = View.GONE
        }



        rv_list.layoutManager = LinearLayoutManager(requireContext())
        rv_list.adapter = adapter

        refresh()




    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun refresh() {
        val listData = mutableListOf<PieEntry>()
        try {
            val map: HashMap<String, Double> = HashMap()
            var list: List<CounterDataItem> = Gson().fromJson(
                arguments?.getString("data", ""),
                object : TypeToken<List<CounterDataItem>>() {}.type
            )

            if (label.isBlank()){
                list = list.filter {
                    when (textViewExpend.text.toString()) {
                        "支出" -> it.money!! < 0
                        "收入" -> it.money!! > 0
                        else -> it.money!! < 0
                    }

                }
            }




            arrayList.clear()
            arrayList.addAll(list)
            adapter.notifyDataSetChanged()


            list.forEach {
                if (!it.type.isNullOrBlank()) {
                    map[it.type!!] = (map[it.type!!] ?: 0.0) + it.money!!
                }
            }
            val ite = map.iterator()
            while (ite.hasNext()) {
                val it = ite.next()
                listData.add(PieEntry(abs(it.value.toFloat()), "${it.key}"))
            }
            val pieDataSet = PieDataSet(listData, arguments?.getString("label", ""))
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
            description.setEnabled(false)
            pie_chart.setDescription(description)
            //设置半透明圆环的半径, 0为透明
            //设置半透明圆环的半径, 0为透明
            pie_chart.setTransparentCircleRadius(0f)

            //设置初始旋转角度

            //设置初始旋转角度
            pie_chart.setRotationAngle(-15f)

            //数据连接线距图形片内部边界的距离，为百分数

            //设置连接线的颜色
            pieDataSet.setValueLineColor(Color.GRAY)
            // 连接线在饼状图外面
            pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE)

            // 设置饼块之间的间隔
            pieDataSet.setSliceSpace(1f)
            pieDataSet.setHighlightEnabled(true)
            // 不显示图例
            val legend: Legend = pie_chart.getLegend()
            legend.isEnabled = false

            // 和四周相隔一段距离,显示数据

            pie_chart.setExtraOffsets(26f, 5f, 26f, 5f)

            // 设置pieChart图表是否可以手动旋转
            pie_chart.setRotationEnabled(true)
            // 设置piecahrt图表点击Item高亮是否可用
            pie_chart.setHighlightPerTapEnabled(true)
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