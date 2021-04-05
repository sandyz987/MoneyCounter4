package com.example.moneycounter4.view.costom

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import com.example.moneycounter4.R
import com.example.moneycounter4.extensions.dp2px

/**
 *@author zhangzhe
 *@date 2021/4/4
 *@description
 */
@RequiresApi(Build.VERSION_CODES.M)
class CounterProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth = 0f
    private var mHeight = 0f
    private var rect: RectF? = null

    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = context.resources.getColor(R.color.progress_background_color, null)
        strokeCap = Paint.Cap.ROUND
    }


    private val foregroundPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = context.resources.getColor(R.color.progress_foreground_color, null)
        strokeCap = Paint.Cap.ROUND
    }


    var progressInt: Int = 0
        set(@IntRange(from = 0, to = 100) value) {
            field = value
            startAnim()
        }

    fun startAnim() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1000
            addUpdateListener {
                animValue = it.animatedValue as Float
                invalidate()
            }
        }.start()
    }


    private var animValue = 1f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        rect = RectF(0f, 0f, mWidth, mHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {

        val round = context.dp2px(6f).toFloat()
        canvas?.clipPath(Path().apply {
            addRoundRect(
                rect ?: RectF(0f, 0f, mWidth, mHeight),
                floatArrayOf(round, round, round, round, round, round, round, round),
                Path.Direction.CCW
            )
        })

        rect?.let {
            canvas?.drawRect(it, backgroundPaint)
        }

        Rect(0, 0, (animValue * mWidth * progressInt / 100f).toInt(), mHeight.toInt()).let {
            canvas?.drawRect(
                it,
                foregroundPaint
            )
        }


        super.onDraw(canvas)


    }

}