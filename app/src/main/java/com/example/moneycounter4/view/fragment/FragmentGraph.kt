package com.example.moneycounter4.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseFragment
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.viewmodel.MainViewModel
import com.example.moneycounter4.widgets.LogW
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_graph.*
import java.util.*

class FragmentGraph : BaseFragment() {
    lateinit var viewModel: MainViewModel

    companion object {
        lateinit var tfLight: Typeface
    }

    private var year = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        tfLight = Typeface.createFromAsset(requireContext().assets, "alibaba_light.ttf")
        val calendar = Calendar.getInstance()
        year = viewModel.selectedYear
        return inflater.inflate(R.layout.fragment_graph, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textViewTimeYear.text = "${year}年"
        refresh()
        constraintDate.setOnClickListener {
            val pvTime =
                TimePickerBuilder(requireContext(),
                    OnTimeSelectListener { date, _ ->
                        year = date.year + 1900
                        viewModel.selectedYear = year
                        refresh()
                    })
                    .setType(booleanArrayOf(true, false, false, false, false, false))
                    .build()
            pvTime.show()
        }


        constraintInOrOut.setOnClickListener {
            val options1Items = listOf("支出","收入","结余")
            val pvOptions: OptionsPickerView<String> =
                OptionsPickerBuilder(requireContext(),
                    OnOptionsSelectListener { options1, _, _, _ ->
                        val tx: String = options1Items[options1]
                        textViewExpend.text = tx
                        refresh()
                    }).build()
            pvOptions.setPicker(options1Items)
            pvOptions.setSelectOptions(when(textViewExpend.text.toString()){
                "支出"->0
                "收入"->1
                "结余"->2
                else->0
            })
            pvOptions.show()
        }


    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun refresh(){
        val list = ArrayList<BarData?>()
        for (i in 1..12) {
            val d = getData(i)
            if(d.second){
                list.add(d.first)
            }
        }
        val adapter = ChartDataAdapter(requireContext(), list)
        listView.adapter = adapter
        textViewTimeYear.text = "${year}年"
    }


    //========
    private class ChartDataAdapter internal constructor(context: Context?, objects: List<BarData?>?) :
        ArrayAdapter<BarData?>(context!!, 0, objects!!) {

        @SuppressLint("InflateParams")
        override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {


            var convertView1 = convertView
            val data: BarData? = getItem(position)
            val holder: ViewHolder
            if (convertView1 == null) {
                holder = ViewHolder()
                convertView1 = LayoutInflater.from(context).inflate(R.layout.item_list_barchart, null)
                holder.chart = convertView1.findViewById(R.id.chart)
                holder.textViewTag = convertView1.findViewById(R.id.textViewTag)
                convertView1.tag = holder
            } else {
                holder = convertView1.tag as ViewHolder
            }


            holder.chart?.setTouchEnabled(false)

            // apply styling
            if (data != null) {
                data.setValueTypeface(tfLight)
                data.setValueTextColor(Color.BLACK)
            }
            holder.chart?.description?.isEnabled = false
            holder.chart?.setDrawGridBackground(false)
            val xAxis: XAxis? = holder.chart?.xAxis
            xAxis?.position = XAxis.XAxisPosition.BOTTOM
            xAxis?.typeface = tfLight
            xAxis?.setDrawGridLines(false)
            val leftAxis: YAxis? = holder.chart?.axisLeft
            leftAxis?.typeface = tfLight
            leftAxis?.setLabelCount(5, false)
            leftAxis?.spaceTop = 15f
            val rightAxis: YAxis? = holder.chart?.axisRight
            rightAxis?.typeface = tfLight
            rightAxis?.setLabelCount(5, false)
            rightAxis?.spaceTop = 15f

            // set data
            holder.chart?.data = data
            holder.chart?.setFitBars(true)

            // do not forget to refresh the chart
//            holder.chart.invalidate();
            holder.chart?.animateY(700)

            return convertView1!!
        }

        private inner class ViewHolder {
            var chart: BarChart? = null
            var textViewTag: TextView? = null
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getData(month: Int): Pair<BarData?,Boolean> {
        val entries: ArrayList<BarEntry> = ArrayList()
        var b = false

        val calendar = Calendar.getInstance()
        calendar.set(year,month,0)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val option = when(textViewExpend.text.toString()){
            "支出"->DataReader.OPTION_EXPEND
            "收入"->DataReader.OPTION_INCOME
            "结余"->DataReader.OPTION_LAST
            else->DataReader.OPTION_EXPEND
        }
        LogW.d("$year $month")
        for (day in 1..dayOfMonth) {

            val list = DataReader.getItems(year,month,day,DataReader.OPTION_BY_DAY)
            val money = DataReader.count(list,option)
            if (money!= 0.0){
                b = true
            }
            entries.add(BarEntry(day.toFloat(), money.toFloat()))
        }
        val d = BarDataSet(entries, "${month}月")
        d.setDrawValues(false)
        d.colors = ColorTemplate.VORDIPLOM_COLORS.toList()
        d.barShadowColor = Color.rgb(203, 203, 203)
        val sets: ArrayList<IBarDataSet> = ArrayList()
        sets.add(d)
        val cd = BarData(sets)
        cd.barWidth = 0.9f
        return Pair(cd,b)
    }

}