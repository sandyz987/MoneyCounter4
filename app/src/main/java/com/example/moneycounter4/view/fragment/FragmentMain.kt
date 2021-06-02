package com.example.moneycounter4.view.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.extensions.dp2px
import com.example.moneycounter4.extensions.getScreenHeight
import com.example.moneycounter4.view.adapter.homewidget.WidgetAdapter
import com.example.moneycounter4.view.adapter.homewidget.WidgetItemGetter
import com.example.moneycounter4.viewmodel.GlobalViewModel
import com.example.moneycounter4.widgets.ItemTouchGridCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_main.*


class FragmentMain : BaseViewModelFragment<GlobalViewModel>() {

    override fun useActivityViewModel() = true

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

        val behavior = BottomSheetBehavior.from(rv_widget)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val lp = iv_welfare?.layoutParams as CoordinatorLayout.LayoutParams?
                lp?.width = ((1 - slideOffset) * bottomSheet.context.dp2px(180f)).toInt()
                lp?.height = ((1 - slideOffset) * bottomSheet.context.dp2px(180f)).toInt()
                lp?.topMargin =
                    ((1 - slideOffset) * bottomSheet.context.getScreenHeight() * 0.65f - (lp?.height
                        ?: 0) / 2).toInt()
                iv_welfare?.requestLayout()


                realtimeblurview?.setBlurRadius((((25f * slideOffset + 0.6f) / 4).toInt() * 4).toFloat())
                realtimeblurview?.setOverlayColor(Color.argb((55 * slideOffset).toInt(), 0, 0, 0))
            }
        }.apply { onSlide(rv_widget, 0f) })

        // =======================
//        val o = Observable.create<WeekItemData> {
//            val chara = listOf("一", "二", "三", "四", "五", "六", "日")
//            val week = CalendarUtil.getWeek()
//            val option = DataReader.OPTION_EXPEND
//
//            val t = ArrayList<DataItem>()
//
//            for (i in 1..7) {
//                var m = 0.0
//                DataReader.db?.counterDao()
//                    ?.getByDuration(
//                        CalendarUtil.getFirstDayOfWeek(week),
//                        86400000L * (i - 1),
//                        86400000L * i,
//                        option
//                    )
//                    ?.forEach { cit ->
//                        cit.money?.let { money ->
//                            m += money
//
//                        }
//
//                    }
//                t.add(DataItem(chara[i - 1], abs(m)))
//            }
//
//            it.onNext(WeekItemData("本周支出", t))
//        }.setSchedulers().subscribe {
//            graph_view_main.title = it.title
//            graph_view_main.data = it.data
//            graph_view_main.setOnClickListener {
//                findNavController().navigate(R.id.action_global_fragmentDistribution, Bundle().apply {
//                    putBoolean("label", true)
//                })
//            }
//        }
//
//        val c = Observable.create<List<Double>> {
//            val list = mutableListOf<Double>()
//            val cal = Calendar.getInstance()
//            var sum = 0.0
//            DataReader.db?.counterDao()?.getByTime(cal.getYear(), cal.getMonth(), -1)
//                ?.forEach { item ->
//                    sum += item.money ?: 0.0
//                }
//            list.add(sum)
//
//            sum = 0.0
//            DataReader.db?.counterDao()
//                ?.getByTime(cal.getYear(), cal.getMonth(), DataReader.OPTION_EXPEND)
//                ?.forEach { item ->
//                    sum -= item.money ?: 0.0
//                }
//            list.add(sum)
//
//            sum = 0.0
//            DataReader.db?.counterDao()
//                ?.getByTime(cal.getYear(), cal.getMonth(), DataReader.OPTION_INCOME)
//                ?.forEach { item ->
//                    sum += item.money ?: 0.0
//                }
//            list.add(sum)
//
//            it.onNext(list)
//        }.setSchedulers().subscribe {
//            tv_money_last.text = "￥${String.format("%.2f", it[0])}"
//            tv_money_out_month.text = "本月支出：${String.format("%.2f", it[1])}"
//            tv_money_in_month.text = "本月收入：${String.format("%.2f", it[2])}"
//
//        }
//
//        val b = Observable.create<List<CounterDataItem>> {
//            it.onNext(
//                BudgetCounter.getList(
//                    SettingUtil.settingData!!.budgetPeriod,
//                    SettingUtil.settingData!!.budgetStartDate
//                )
//            )
//        }.setSchedulers().subscribe {
//            tv_money_pocket.text =
//                "￥${String.format("%.2f", SettingUtil.settingData!!.budgetMoney)}"
//            val expend = DataReader.count(it, DataReader.OPTION_EXPEND)
//            tv_money_out.text = "已支出：${String.format("%.2f", expend)}"
//            tv_money_rest.text =
//                "剩余：${String.format("%.2f", SettingUtil.settingData!!.budgetMoney - expend)}"
//            counter_pb_main.progressInt =
//                ((SettingUtil.settingData!!.budgetMoney - expend) / SettingUtil.settingData!!.budgetMoney * 100f).toInt()
//
//        }
//
//        view3.setOnClickListener {
//            findNavController().navigate(R.id.action_global_fragmentCounterSetting)
//        }
//
//        tv_budget_setting.text =
//            "结算日:每${SettingUtil.settingData!!.budgetPeriod}${FragmentCounterSetting.getDescribe()}"
        // =========================


        val widgetAdapter = WidgetAdapter(mutableListOf("日历", "月结余", "本周支出", "全局预算")) {
            when (it) {
                1 -> {
                }
                2 -> {
                }
                3 -> {
                    findNavController().navigate(
                        R.id.action_global_fragmentDistribution,
                        Bundle().apply { putBoolean("label", true) })
                }
                4 -> {
                    findNavController().navigate(R.id.action_global_fragmentCounterSetting)
                }
                else -> {
                }
            }
        }
        val ihCallback =
            ItemTouchGridCallback(widgetAdapter)
        val itemTouchHelper = ItemTouchHelper(ihCallback)
        itemTouchHelper.attachToRecyclerView(rv_widget)

        rv_widget.adapter = widgetAdapter
        rv_widget.layoutManager = GridLayoutManager(requireContext(), 4).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) =
                    WidgetItemGetter.getSpanSizeByType(widgetAdapter.getItemViewType(position))
            }
        }

    }

}