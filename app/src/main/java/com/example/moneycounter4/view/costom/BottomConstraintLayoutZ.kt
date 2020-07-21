package com.example.moneycounter4.view.costom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.PrecomputedTextCompat
import com.example.moneycounter4.R

//自定义view
//加了两个函数 show  hide  同时动态改变大小

class BottomConstraintLayoutZ : ConstraintLayout {
    private lateinit var animY : ValueAnimator
    var mPaint: Paint = Paint()
    var mHeight = 0
    var trY = 0f
    var isHide = false
    var isNotInit = true

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(isNotInit){
            mHeight = measuredHeight
            isNotInit = false
        }
    }

    fun show(){
        isHide = false
        animY = ValueAnimator.ofFloat(trY, 0f)
        animY.repeatCount = 0
        animY.repeatMode = ValueAnimator.REVERSE
        animY.duration = 500
        animY.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            trY = value
            val params = layoutParams
            params.height = (mHeight - trY).toInt()
            layoutParams = params
        }
        animY.start()
    }
    fun hide(){
        isHide = true
        animY = ValueAnimator.ofFloat(trY, mHeight.toFloat())
        animY.repeatCount = 0
        animY.repeatMode = ValueAnimator.REVERSE
        animY.duration = 500
        animY.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            trY = value
            val params = layoutParams
            params.height = (mHeight - trY).toInt()
            layoutParams = params
        }
        animY.start()
    }



}