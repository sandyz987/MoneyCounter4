package com.example.moneycounter4.view.costom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

/**
 *@author zhangzhe
 *@date 2021/3/31
 *@description
 */

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


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

}