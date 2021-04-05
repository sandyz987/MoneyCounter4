package com.example.moneycounter4.model

import android.content.Context
import com.example.moneycounter4.viewmodel.MainApplication
import com.google.gson.Gson

/**
 *@author zhangzhe
 *@date 2021/4/4
 *@description 设置的自动获取和保存
 */

data class SettingData(
    var budgetPeriod: String = "周",   // 周 月
    var budgetStartDate: Int = 2,   // 当budgetPeriod为周时，范围为1-7 周日是1，否则值为1或15
    var budgetMoney: Double = 500.0   // 预算的可用金额

)

fun SettingData.toJson(): String = Gson().toJson(this)

object SettingUtil {
    private val sp =
        MainApplication.context.getSharedPreferences("counterData", Context.MODE_PRIVATE)
    var settingData: SettingData? = null
        get() {
            if (field != null) {
                return field
            }
            try {
                settingData =
                    Gson().fromJson(sp.getString("setting_data", ""), SettingData::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return field ?: SettingData()
        }

    fun save() {
        settingData?.let {
            val editor = sp.edit()
            editor.putString("setting_data", it.toJson())
            editor.apply()
        }
    }

    fun reset() {
        settingData?.let {
            val editor = sp.edit()
            editor.putString("setting_data", "")
            editor.apply()
            settingData = null
        }
    }

}