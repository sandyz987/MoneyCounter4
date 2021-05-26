package com.example.moneycounter4.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
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
import com.example.moneycounter4.view.costom.DataItem
import com.example.moneycounter4.viewmodel.MainViewModel
import com.google.gson.Gson
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*
import kotlin.math.abs


class FragmentMain : BaseViewModelFragment<MainViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        iv_welfare.setOnClickListener {
            findNavController().navigate(R.id.action_global_fragmentWelfare)
        }

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
            graph_view_main.title = it.title
            graph_view_main.data = it.data
            graph_view_main.setOnClickListener {
                val week = CalendarUtil.getWeek()
                val option = DataReader.OPTION_EXPEND
                val list = DataReader.db?.counterDao()
                    ?.getByDuration(
                        CalendarUtil.getFirstDayOfWeek(week),
                        0L,
                        86400000L * 7,
                        option
                    )

                findNavController().navigate(R.id.action_global_fragmentDistribution, Bundle().apply {
                    putString("data", Gson().toJson(list))
                    putString("label", "支出分布图")
                })
            }
        }

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
            tv_money_last.text = "￥${String.format("%.2f", it[0])}"
            tv_money_out_month.text = "本月支出：${String.format("%.2f", it[1])}"
            tv_money_in_month.text = "本月收入：${String.format("%.2f", it[2])}"

        }

        val b = Observable.create<List<CounterDataItem>> {
            it.onNext(
                BudgetCounter.getList(
                    SettingUtil.settingData!!.budgetPeriod,
                    SettingUtil.settingData!!.budgetStartDate
                )
            )
        }.setSchedulers().subscribe {
            tv_money_pocket.text =
                "￥${String.format("%.2f", SettingUtil.settingData!!.budgetMoney)}"
            val expend = DataReader.count(it, DataReader.OPTION_EXPEND)
            tv_money_out.text = "已支出：${String.format("%.2f", expend)}"
            tv_money_rest.text =
                "剩余：${String.format("%.2f", SettingUtil.settingData!!.budgetMoney - expend)}"
            counter_pb_main.progressInt =
                ((SettingUtil.settingData!!.budgetMoney - expend) / SettingUtil.settingData!!.budgetMoney * 100f).toInt()

        }

        view3.setOnClickListener {
            findNavController().navigate(R.id.action_global_fragmentCounterSetting)
        }

        tv_budget_setting.text =
            "结算日:每${SettingUtil.settingData!!.budgetPeriod}${FragmentCounterSetting.getDescribe()}"

    }

}