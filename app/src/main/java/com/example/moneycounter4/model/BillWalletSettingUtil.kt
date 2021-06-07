package com.example.moneycounter4.model

import android.content.Context
import android.graphics.drawable.Drawable
import com.example.moneycounter4.extensions.getIfExist
import com.example.moneycounter4.viewmodel.MainApplication
import com.google.gson.Gson

/**
 *@author zhangzhe
 *@date 2021/4/4
 *@description 设置的自动获取和保存
 */

data class BillItem(
    val name: String,
    val picId: Int
)

data class WalletItem(
    val name: String,
    val picId: Int,
    val money: Double
)

data class BillWalletSettingData(
    var billList: MutableList<BillItem> = mutableListOf(
        BillItem("日常账本", 0),
        BillItem("校园账本", 2)
    ),
    var walletList: MutableList<WalletItem> = mutableListOf(
        WalletItem("微信", 5, 0.0),
        WalletItem("支付宝", 4, 0.0)
    )

)

fun BillWalletSettingData.toJson(): String = Gson().toJson(this)

object BillWalletSettingUtil {
    private const val modelStr = "bill_wallet_setting_data"
    private val sp =
        MainApplication.context.getSharedPreferences("counterData", Context.MODE_PRIVATE)
    var settingData: BillWalletSettingData? = null
        get() {
            if (field != null) {
                return field
            }
            try {
                settingData =
                    Gson().fromJson(sp.getString(modelStr, ""), BillWalletSettingData::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return field ?: BillWalletSettingData()
        }

    fun findBillPicByName(name: String): Drawable? {
        val ind = settingData?.billList?.indexOfFirst { it.name == name } ?: 0
        val id = settingData?.billList?.getIfExist(
            if (ind != -1) ind else
                0, null
        )?.picId ?: -1
        return BillPicGetter.getBillPic(id)
    }

    fun findWalletPicByName(name: String): Drawable? {
        val id =
            settingData?.walletList?.get(settingData?.walletList?.indexOfFirst { it.name == name }
                ?: 0)?.picId ?: -1
        return WalletPicGetter.getWalletPic(id)
    }

    fun save() {
        settingData?.let {
            val editor = sp.edit()
            editor.putString(modelStr, it.toJson())
            editor.apply()
        }
    }

    fun reset() {
        settingData?.let {
            val editor = sp.edit()
            editor.putString(modelStr, "")
            editor.apply()
            settingData = null
        }
    }

}