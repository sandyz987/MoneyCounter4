package com.example.moneycounter4.view.adapter.homewidget

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.moneycounter4.R
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.model.BudgetCounter
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.model.SettingUtil
import com.example.moneycounter4.model.dao.getByDuration
import com.example.moneycounter4.model.dao.getByTime
import com.example.moneycounter4.network.setSchedulers
import com.example.moneycounter4.utils.CalendarUtil
import com.example.moneycounter4.utils.getMonth
import com.example.moneycounter4.utils.getYear
import com.example.moneycounter4.view.adapter.WeekItemData
import com.example.moneycounter4.view.adapter.homewidget.viewholder.BudgetViewHolder
import com.example.moneycounter4.view.adapter.homewidget.viewholder.CalendarViewHolder
import com.example.moneycounter4.view.adapter.homewidget.viewholder.MonthCarryOverViewHolder
import com.example.moneycounter4.view.adapter.homewidget.viewholder.WeekGraphViewHolder
import com.example.moneycounter4.view.costom.DataItem
import com.example.moneycounter4.view.fragment.FragmentCounterSetting
import io.reactivex.Observable
import java.util.*
import kotlin.math.abs

/**
 *@author zhangzhe
 *@date 2021/5/31
 *@description
 *
 * 添加新控件请在{@link getTypeByName}写一个名字和id，
 * 并且在bindView中处理响应的绑定逻辑（记得在viewholder包中添加新的ViewHolder）
 *
 */
class WidgetItemGetter {

    companion object {

        // 视图所占的宽度
        fun getTypeByName(name: String) = when (name) {
            "日历" -> 1
            "月结余" -> 2
            "本周支出" -> 3
            "全局预算" -> 4
            else -> 1
        }

        // 控件所占的宽度
        fun getSpanSizeByType(type: Int) = when (type) {
            1 -> 2
            2 -> 2
            3 -> 4
            4 -> 2
            else -> 1
        }

        fun getViewHolder(parent: ViewGroup, viewType: Int): WidgetAdapter.BaseViewHolder =
            when (viewType) {
                1 -> CalendarViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.widget_calendar, parent, false), viewType
                )
                2 -> MonthCarryOverViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.widget_month_carrry_over, parent, false), viewType
                )
                3 -> WeekGraphViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.widget_week_graph, parent, false), viewType
                )
                4 -> BudgetViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.widget_budget, parent, false), viewType
                )

                else -> CalendarViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.widget_calendar, parent, false), viewType
                )
            }

        fun bindView(holder: WidgetAdapter.BaseViewHolder) {
            if (holder is CalendarViewHolder) {
                // 自动显示日期，无需操作
            }
            if (holder is MonthCarryOverViewHolder) {
                val c = Observable.create<List<Double>> {
                    val list = mutableListOf<Double>()
                    val cal = Calendar.getInstance()
                    var sum = 0.0
                    DataReader.db?.counterDao()?.getByTime(cal.getYear(), cal.getMonth(), -1)
                        ?.forEach { item ->
                            sum += item.money ?: 0.0
                        }
                    list.add(sum)

                    sum = 0.0
                    DataReader.db?.counterDao()
                        ?.getByTime(cal.getYear(), cal.getMonth(), DataReader.OPTION_EXPEND)
                        ?.forEach { item ->
                            sum -= item.money ?: 0.0
                        }
                    list.add(sum)

                    sum = 0.0
                    DataReader.db?.counterDao()
                        ?.getByTime(cal.getYear(), cal.getMonth(), DataReader.OPTION_INCOME)
                        ?.forEach { item ->
                            sum += item.money ?: 0.0
                        }
                    list.add(sum)

                    it.onNext(list)
                }.setSchedulers().subscribe {
                    holder.tv_money_last.text = "￥${String.format("%.2f", it[0])}"
                    holder.tv_money_out_month.text = "本月支出：${String.format("%.2f", it[1])}"
                    holder.tv_money_in_month.text = "本月收入：${String.format("%.2f", it[2])}"

                }
            }
            if (holder is WeekGraphViewHolder) {
                val o = Observable.create<WeekItemData> {
                    val chara = listOf("一", "二", "三", "四", "五", "六", "日")
                    val week = CalendarUtil.getWeek()
                    val option = DataReader.OPTION_EXPEND

                    val t = ArrayList<DataItem>()

                    for (i in 1..7) {
                        var m = 0.0
                        DataReader.db?.counterDao()
                            ?.getByDuration(
                                CalendarUtil.getFirstDayOfWeek(week),
                                86400000L * (i - 1),
                                86400000L * i,
                                option
                            )
                            ?.forEach { cit ->
                                cit.money?.let { money ->
                                    m += money

                                }

                            }
                        t.add(DataItem(chara[i - 1], abs(m)))
                    }

                    it.onNext(WeekItemData("本周支出", t))
                }.setSchedulers().subscribe {
                    holder.graph_view.title = it.title
                    holder.graph_view.data = it.data
                    holder.graph_view.setOnClickListener {
//                        findNavController().navigate(R.id.action_global_fragmentDistribution, Bundle().apply {
//                            putBoolean("label", true)
//                        })
                    }
                }
            }
            if (holder is BudgetViewHolder) {
                val b = Observable.create<List<CounterDataItem>> {
                    it.onNext(
                        BudgetCounter.getList(
                            SettingUtil.settingData!!.budgetPeriod,
                            SettingUtil.settingData!!.budgetStartDate
                        )
                    )
                }.setSchedulers().subscribe {
                    holder.tv_money_pocket.text =
                        "￥${String.format("%.2f", SettingUtil.settingData!!.budgetMoney)}"
                    val expend = DataReader.count(it, DataReader.OPTION_EXPEND)
                    holder.tv_money_out.text = "已支出：${String.format("%.2f", expend)}"
                    holder.tv_money_rest.text =
                        "剩余：${
                            String.format(
                                "%.2f",
                                SettingUtil.settingData!!.budgetMoney - expend
                            )
                        }"
                    holder.counter_pb_main.progressInt =
                        ((SettingUtil.settingData!!.budgetMoney - expend) / SettingUtil.settingData!!.budgetMoney * 100f).toInt()
//                    view3.setOnClickListener {
//                        findNavController().navigate(R.id.action_global_fragmentCounterSetting)
//                    }
                    holder.tv_budget_setting.text =
                        "结算日:每${SettingUtil.settingData!!.budgetPeriod}${FragmentCounterSetting.getDescribe()}"

                }
            }

        }
    }
}