package com.example.moneycounter4.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.model.dao.getByDuration
import com.example.moneycounter4.network.setSchedulers
import com.example.moneycounter4.utils.CalendarUtil
import com.example.moneycounter4.utils.getYear
import com.example.moneycounter4.utils.toTimeString
import com.example.moneycounter4.view.costom.DataItem
import com.example.moneycounter4.viewmodel.GlobalViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_graph.*
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs


class FragmentGraph : BaseViewModelFragment<GlobalViewModel>() {

    companion object {
        lateinit var tfLight: Typeface
    }

    override fun useActivityViewModel() = true

    private var year = 0
    private val adapter = ChartAdapter(listOf())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        tfLight = Typeface.createFromAsset(requireContext().assets, "alibaba_light.ttf")
        year = Calendar.getInstance().getYear()
        return inflater.inflate(R.layout.fragment_graph, container, false)
    }

    fun add(wb: HSSFWorkbook, row: HSSFRow, index: Int, str: String) {
        val cell = row.createCell(index)
        cell.setCellValue(str)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textViewTimeYear.text = "${year}年"
        constraintDate.setOnClickListener {
            val pvTime =
                TimePickerBuilder(
                    requireContext()
                ) { date, _ ->
                    year = date.year + 1900
                    drag_head_view.refresh()
                }
                    .setType(booleanArrayOf(true, false, false, false, false, false))
                    .build()
            pvTime.show()
        }


        constraintInOrOut.setOnClickListener {
            val options1Items = listOf("支出", "收入", "结余")
            val pvOptions: OptionsPickerView<String> =
                OptionsPickerBuilder(
                    requireContext()
                ) { options1, _, _, _ ->
                    val tx: String = options1Items[options1]
                    textViewExpend.text = tx
                    drag_head_view.refresh()
                }.build()
            pvOptions.setPicker(options1Items)
            pvOptions.setSelectOptions(
                when (textViewExpend.text.toString()) {
                    "支出" -> 0
                    "收入" -> 1
                    "结余" -> 2
                    else -> 0
                }
            )
            pvOptions.show()
        }

        rv_graph.adapter = adapter
        rv_graph.layoutManager = LinearLayoutManager(requireContext())
        val listener = {
            refresh()
        }

        drag_head_view.onRefreshAction = listener
        drag_head_view.refresh()

        tv_export.setOnClickListener {
            try {
                val file = File(
                    context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    "总账单.xls"
                )
                file.createNewFile()
                val fos = FileOutputStream(file)
                //=====================
                val wb = HSSFWorkbook()
                val sheet: HSSFSheet = wb.createSheet("sheet0")
                val row1 = sheet.createRow(0)
                // 初始化表头
                row1.createCell(0).setCellValue("时间")
                row1.createCell(1).setCellValue("时间戳")
                row1.createCell(2).setCellValue("金额")
                row1.createCell(3).setCellValue("类型")
                row1.createCell(4).setCellValue("种类")
                row1.createCell(5).setCellValue("备注")
                row1.createCell(6).setCellValue("账户")
                row1.createCell(7).setCellValue("账本")

                // 以下为输出到excel
                var line = 1
                val list = DataReader.db?.counterDao()?.all
                list?.sortedBy { it.time }?.forEach {
                    val row = sheet.createRow(line++)
                    var colIndex = 0
                    add(wb, row, colIndex++, CalendarUtil.getCalendar(it.time ?: 0L).toTimeString())
                    add(wb, row, colIndex++, it.time.toString())
                    add(wb, row, colIndex++, it.money.toString())
                    add(wb, row, colIndex++, if (it.money ?: 0.0 > 0.0) "收入" else "支出")
                    add(wb, row, colIndex++, it.type.toString())
                    add(wb, row, colIndex++, it.tips.toString())
                    add(wb, row, colIndex++, it.accountBook.toString())
                    add(wb, row, colIndex, it.wallet.toString())
                }
                wb.write(fos)
                fos.flush()
                fos.close()

                Toast.makeText(activity, "导出excel成功！文件路径为：${file.absolutePath}", Toast.LENGTH_LONG)
                Toast.makeText(activity, "导出excel成功！文件路径为：${file.absolutePath}", Toast.LENGTH_LONG)
                    .show()
                // TODO 无效，待重写 通知文件管理器新增了文件
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val uri = Uri.fromFile(file)
                intent.data = uri
                requireActivity().sendBroadcast(intent)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun refresh() {


        textViewTimeYear.text = "${year}年"

        val o = Observable.create<ArrayList<BarData>> {
            val chara = listOf("一", "二", "三", "四", "五", "六", "日")
            var week = 1
            val option = when (textViewExpend.text.toString()) {
                "支出" -> DataReader.OPTION_EXPEND
                "收入" -> DataReader.OPTION_INCOME
                "结余" -> DataReader.OPTION_LAST
                else -> DataReader.OPTION_EXPEND
            }
            val list = ArrayList<BarData>()
            CalendarUtil.getEveryFirstDayOfWeek(year).forEach {
                val t = ArrayList<DataItem>()
                var weekMoney = 0.0


                val entries: ArrayList<BarEntry> = ArrayList()
                for (i in 1..7) {
                    var m = 0.0
                    DataReader.db?.counterDao()
                        ?.getByDuration(it, 86400000L * (i - 1), 86400000L * i, option)
                        ?.forEach { cit ->
                            cit.money?.let { money ->
                                m += money
                            }

                        }
                    if (m < 0.0 && option == DataReader.OPTION_LAST) {
                        m = 0.0
                    }
                    weekMoney += m
                    t.add(DataItem(chara[i - 1], abs(m), it, 86400000L * 7))
                    //
                    entries.add(BarEntry(i.toFloat(), abs(m).toFloat()))
                    //
                }
                if (weekMoney != 0.0) {
                    //list.add(WeekItemData("第${week}周", t))
                    val d = BarDataSet(entries, "第${week}周")
                    d.setDrawValues(true)
//                    d.valueTextSize = context?.sp(10)?.toFloat()?: 0f
                    d.colors = ColorTemplate.VORDIPLOM_COLORS.toList()
                    d.barShadowColor = Color.rgb(203, 203, 203)
                    val sets: ArrayList<IBarDataSet> = ArrayList()
                    sets.add(d)
                    val cd = BarData(sets)
                    cd.barWidth = 0.9f
                    list.add(cd)
                }
                week++
            }


            it.onNext(list)
        }.setSchedulers().subscribe {
            adapter.refresh(it)
            drag_head_view?.finishRefresh()
        }


    }

    private class ChartAdapter(var list: List<BarData>) :
        RecyclerView.Adapter<ChartAdapter.ChartViewHolder>() {

        inner class ChartViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var chart: BarChart? = null
            var textViewTag: TextView? = null
            init {
                chart = v.findViewById(R.id.chart)
                textViewTag = v.findViewById(R.id.textViewTag)
            }
        }

        fun refresh(list: List<BarData>) {
            this.list = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_list_barchart, parent, false)
            return ChartViewHolder(v)
        }

        override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
            holder.chart?.setTouchEnabled(false)

            // apply styling

            list[position].setValueTypeface(tfLight)
            list[position].setValueTextColor(Color.BLACK)

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

            list[position].setValueTextSize(14f)
            holder.chart?.data = list[position]
            holder.chart?.setFitBars(true)
            holder.chart?.animateY(700)


        }

        override fun getItemCount() = list.size

    }


    //========
//    private class ChartDataAdapter internal constructor(
//        context: Context?,
//        objects: List<BarData?>?
//    ) :
//        ArrayAdapter<BarData?>(context!!, 0, objects!!) {
//
//        @SuppressLint("InflateParams")
//        override fun getView(
//            position: Int,
//            convertView: View?,
//            parent: ViewGroup
//        ): View {
//
//
//            var convertView1 = convertView
//            val data: BarData? = getItem(position)
//            val holder: ViewHolder
//            if (convertView1 == null) {
//                holder = ViewHolder()
//                convertView1 =
//                    LayoutInflater.from(context).inflate(R.layout.item_list_barchart, null)
//                holder.chart = convertView1.findViewById(R.id.chart)
//                holder.textViewTag = convertView1.findViewById(R.id.textViewTag)
//                convertView1.tag = holder
//            } else {
//                holder = convertView1.tag as ViewHolder
//            }
//
//
//            holder.chart?.setTouchEnabled(false)
//
//            // apply styling
//            if (data != null) {
//                data.setValueTypeface(tfLight)
//                data.setValueTextColor(Color.BLACK)
//            }
//            holder.chart?.description?.isEnabled = false
//            holder.chart?.setDrawGridBackground(false)
//            val xAxis: XAxis? = holder.chart?.xAxis
//            xAxis?.position = XAxis.XAxisPosition.BOTTOM
//            xAxis?.typeface = tfLight
//            xAxis?.setDrawGridLines(false)
//            val leftAxis: YAxis? = holder.chart?.axisLeft
//            leftAxis?.typeface = tfLight
//            leftAxis?.setLabelCount(5, false)
//            leftAxis?.spaceTop = 15f
//            val rightAxis: YAxis? = holder.chart?.axisRight
//            rightAxis?.typeface = tfLight
//            rightAxis?.setLabelCount(5, false)
//            rightAxis?.spaceTop = 15f
//
//            // set data
//            holder.chart?.data = data
//            holder.chart?.setFitBars(true)
//
//            // do not forget to refresh the chart
////            holder.chart.invalidate();
//            holder.chart?.animateY(700)
//
//            return convertView1!!
//        }
//
//        private inner class ViewHolder {
//            var chart: BarChart? = null
//            var textViewTag: TextView? = null
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.N)
//    private fun getData(month: Int): Pair<BarData?, Boolean> {
//        val entries: ArrayList<BarEntry> = ArrayList()
//        var b = false
//
//        val calendar = Calendar.getInstance()
//        calendar.set(year, month, 0)
//        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
//        val option = when (textViewExpend.text.toString()) {
//            "支出" -> DataReader.OPTION_EXPEND
//            "收入" -> DataReader.OPTION_INCOME
//            "结余" -> DataReader.OPTION_LAST
//            else -> DataReader.OPTION_EXPEND
//        }
//        LogW.d("$year $month")
//        for (day in 1..dayOfMonth) {
//
//            val list = DataReader.getCounterItems(year, month, day, DataReader.OPTION_BY_DAY)
//            val money = DataReader.count(list, option)
//            if (money != 0.0) {
//                b = true
//            }
//            entries.add(BarEntry(day.toFloat(), money.toFloat()))
//        }
//        val d = BarDataSet(entries, "${month}月")
//        d.setDrawValues(false)
//        d.colors = ColorTemplate.VORDIPLOM_COLORS.toList()
//        d.barShadowColor = Color.rgb(203, 203, 203)
//        val sets: ArrayList<IBarDataSet> = ArrayList()
//        sets.add(d)
//        val cd = BarData(sets)
//        cd.barWidth = 0.9f
//        return Pair(cd, b)
//    }

}