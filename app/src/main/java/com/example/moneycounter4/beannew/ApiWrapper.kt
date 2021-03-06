package com.example.moneycounter4.beannew

import com.google.gson.Gson

/**
 * 当返回的接口json中有data字段时使用此接口
 */
class ApiWrapper<T>(val data: T) : InfoWrapper() {
    companion object {
        fun <T> newApi(status: Int = 200, info: String = "", data: T): String {
            return ApiWrapper(data).apply {
                this.status = status
                this.info = info
            }.parseToString()
        }
    }
}

fun ApiWrapper<*>.parseToString(): String {
    return Gson().toJson(this)
}