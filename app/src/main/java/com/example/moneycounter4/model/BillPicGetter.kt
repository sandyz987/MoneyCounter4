package com.example.moneycounter4.model

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import com.example.moneycounter4.R
import com.example.moneycounter4.viewmodel.MainApplication

/**
 * 获取账单的图片
 */

object BillPicGetter {
    @SuppressLint("UseCompatLoadingForDrawables")
    fun getBillPic(id: Int): Drawable? =
        when (id) {
            0 -> {
                MainApplication.context.getDrawable(R.drawable.ic_bill_daily)
            }
            1 -> {
                MainApplication.context.getDrawable(R.drawable.ic_bill_journey)
            }
            2 -> {
                MainApplication.context.getDrawable(R.drawable.ic_bill_school)
            }
            3 -> {
                MainApplication.context.getDrawable(R.drawable.ic_bill_normal)
            }
            else -> MainApplication.context.getDrawable(R.drawable.ic_bill_normal)
        }

    fun getSize() = 4


}