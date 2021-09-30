package com.example.moneycounter4.extensions

import java.util.*

/**
 *@author zhangzhe
 *@date 2021/6/7
 *@description
 */

fun swapOrder(mList: MutableList<*>, fromPosition: Int, toPosition: Int) {
    if (fromPosition < toPosition) {
        for (i in fromPosition until toPosition) {
            Collections.swap(
                mList,
                i,
                i + 1
            )
        }
    } else {
        for (i in fromPosition downTo toPosition + 1) {

            Collections.swap(
                mList,
                i,
                i - 1
            )
        }
    }
}