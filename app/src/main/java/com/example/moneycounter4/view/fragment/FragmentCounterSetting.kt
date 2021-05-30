package com.example.moneycounter4.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.model.SettingUtil
import com.example.moneycounter4.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_counter_setting.*
import kotlin.math.abs


class FragmentCounterSetting : BaseViewModelFragment<MainViewModel>() {

    override fun useActivityViewModel() = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_counter_setting, container, false)
    }

    companion object {
        private val mapWeek = HashMap<Int, String>()

        init {
            mapWeek[1] = "星期日"
            mapWeek[2] = "星期一"
            mapWeek[3] = "星期二"
            mapWeek[4] = "星期三"
            mapWeek[5] = "星期四"
            mapWeek[6] = "星期五"
            mapWeek[7] = "星期六"
        }

        fun getDescribe(): String {
            return when (SettingUtil.settingData?.budgetPeriod) {
                "周" -> {
                    mapWeek[SettingUtil.settingData?.budgetStartDate] ?: "-"
                }
                "月" -> {
                    "${SettingUtil.settingData?.budgetStartDate}号"
                }
                else -> {
                    "-"
                }
            }
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textViewFinish.setOnClickListener {
            SettingUtil.save()
            Toast.makeText(requireContext(), "已保存设置", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_fragmentCounterSetting_pop)
        }

        constraintDel.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("是否恢复默认设置？")
                .setNegativeButton("否", null)
            builder.setPositiveButton(
                "是"
            ) { _, _ ->
                SettingUtil.reset()
                refresh()
            }.show()
        }

        constraint_budget_money.setOnClickListener {
            val inputServer = EditText(requireContext())
            inputServer.setText(tv_budget_money.text.toString())
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("请输入总预算：").setView(inputServer)
                .setNegativeButton("取消", null)
            builder.setPositiveButton(
                "确定"
            ) { _, _ ->
                var money = 0.0
                try {
                    money = inputServer.text.toString().toDouble()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                SettingUtil.settingData?.budgetMoney = abs(money)
                refresh()
            }.show()
        }

        constraint_budget_period.setOnClickListener {
            val options1Items = listOf("周", "月")
            val pvOptions: OptionsPickerView<String> =
                OptionsPickerBuilder(
                    requireContext()
                ) { options1, _, _, _ ->
                    val tx: String = options1Items[options1]
                    SettingUtil.settingData!!.budgetPeriod = tx
                    if (tx == "周") {
                        SettingUtil.settingData!!.budgetStartDate = 2
                    } else {
                        SettingUtil.settingData!!.budgetStartDate = 1
                    }
                    SettingUtil.save()
                    refresh()
                }.build()
            pvOptions.setPicker(options1Items)
            pvOptions.setSelectOptions(
                when (SettingUtil.settingData!!.budgetPeriod) {
                    "周" -> 0
                    "月" -> 1
                    else -> 0
                }
            )
            pvOptions.show()
        }

        constraint_budget_start.setOnClickListener {
            val options1Items = when (SettingUtil.settingData!!.budgetPeriod) {
                "周" -> listOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
                "月" -> mutableListOf<String>().apply { for (i in 1..28) {add("${i}号")} }
                else -> listOf()
            }
            val pvOptions: OptionsPickerView<String> =
                OptionsPickerBuilder(
                    requireContext()
                ) { options1, _, _, _ ->
                    SettingUtil.settingData!!.budgetStartDate =
                        when (SettingUtil.settingData!!.budgetPeriod) {
                            "周" -> options1 + 1
                            "月" -> options1 + 1
                            else -> 1
                        }
                    SettingUtil.save()
                    refresh()
                }.build()
            pvOptions.setPicker(options1Items)
            pvOptions.show()
        }

        refresh()
    }

    private fun refresh() {
        tv_budget_money.text = (SettingUtil.settingData?.budgetMoney ?: "500").toString()
        tv_budget_period.text = SettingUtil.settingData?.budgetPeriod
        tv_budget_start.text = getDescribe()
    }


}