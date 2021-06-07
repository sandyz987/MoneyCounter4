package com.example.moneycounter4.extensions

/**
 *@author zhangzhe
 *@date 2021/6/7
 *@description
 */

fun <T> List<T>?.getIfExist(position: Int, default: T): T {
    if (isNullOrEmpty()) {
        return default
    }
    return if (position in 0 until size) {
        get(position)
    } else {
        default
    }
}