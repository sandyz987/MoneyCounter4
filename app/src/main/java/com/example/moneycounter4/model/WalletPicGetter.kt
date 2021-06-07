package com.example.moneycounter4.model

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.drawable.Drawable
import com.example.moneycounter4.R
import com.example.moneycounter4.viewmodel.MainApplication

/**
 * 获取账单的图片
 */

object WalletPicGetter {
    @SuppressLint("UseCompatLoadingForDrawables")
    fun getWalletPic(id: Int): Drawable? =
        when (id) {
            0 -> {
                MainApplication.context.getDrawable(R.drawable.ic_wallet_card)
            }
            1 -> {
                MainApplication.context.getDrawable(R.drawable.ic_wallet_my)
            }
            2 -> {
                MainApplication.context.getDrawable(R.drawable.ic_wallet_packet)
            }
            3 -> {
                MainApplication.context.getDrawable(R.drawable.ic_wallet_qq)
            }
            4 -> {
                MainApplication.context.getDrawable(R.drawable.ic_wallet_zfb)
            }
            5 -> {
                MainApplication.context.getDrawable(R.drawable.ic_wallet_wx)
            }
            else -> MainApplication.context.getDrawable(R.drawable.ic_wallet_packet)
        }


}