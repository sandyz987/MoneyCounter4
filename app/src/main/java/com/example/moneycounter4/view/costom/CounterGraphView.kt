package com.example.moneycounter4.view.costom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import kotlin.math.max

/**
 *@author zhangzhe
 *@date 2021/3/31
 *@description
 */

data class DataItem(
    val text: String = "",
    val data: Double = 0.0
)

class CounterGraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth = 0f
    private var mHeight = 0f


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()

    }

    private var dataBlank = 0.0
    private var maxData = 0.0

    var title = ""
        set(value) {
            field = value
            invalidate()
        }

    var data = listOf<DataItem>()
        set(value) {
            field = value
            dataBlank = mWidth / (value.size + 1.0)
            value.forEach { maxData = max(maxData, it.data) }
            invalidate()
        }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }


}