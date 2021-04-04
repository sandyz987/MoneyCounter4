package com.example.moneycounter4.view.costom

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.example.moneycounter4.R
import com.example.moneycounter4.extensions.dp2px
import com.example.moneycounter4.extensions.sp
import com.example.moneycounter4.viewmodel.MainApplication
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

@RequiresApi(Build.VERSION_CODES.M)
class CounterGraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth = 0f
    private var mHeight = 0f


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        rect = RectF(0f, 0f, mWidth, mHeight)
        dataBlank = mWidth / (data.size + 1.0)
    }

    private var dataBlank = 0.0
    private var maxData = 0.0
    private var rect: RectF? = null

    private val dataPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = context.resources.getColor(R.color.graph_data_color, null)
        strokeCap = Paint.Cap.ROUND
        strokeWidth = MainApplication.app.dp2px(16f).toFloat()
    }
    private val textPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
        strokeCap = Paint.Cap.ROUND
        textSize = context.sp(18).toFloat()
    }
    private val titlePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
        strokeCap = Paint.Cap.ROUND
        textSize = context.sp(24).toFloat()
    }
    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = context.resources.getColor(R.color.graph_background_color, null)
        strokeCap = Paint.Cap.ROUND
    }

    var title = ""
        set(value) {
            field = value
            invalidate()
        }

    var animValue = 0f

    var data = listOf<DataItem>()
        set(value) {
            field = value
            dataBlank = mWidth / (value.size + 1.0)
            value.forEach { maxData = max(maxData, it.data) }
            startAnim()
            invalidate()
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


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {

        val round = context.dp2px(20f).toFloat()
        canvas?.clipPath(Path().apply {
            addRoundRect(
                rect ?: RectF(0f, 0f, mWidth, mHeight),
                floatArrayOf(round, round, round, round, round, round, round, round),
                Path.Direction.CCW
            )
        })

        rect?.let { canvas?.drawRect(it, backgroundPaint) }

        canvas?.drawText(title, context.dp2px(20f).toFloat(), (0.2 * mHeight).toFloat(), titlePaint)

        for (i in data.indices) {
            val xOffset = (i + 1) * dataBlank
            val yBottom = mHeight * 0.7
            val yTop = mHeight * 0.3
            val dy = yBottom - yTop
            val yOffset = dy * (data[i].data / maxData) // 百分比
            canvas?.drawLine(
                xOffset.toFloat(),
                yBottom.toFloat(),
                xOffset.toFloat(),
                yBottom.toFloat() - yOffset.toFloat() * animValue,
                dataPaint
            )
            val textWidth = textPaint.measureText(data[i].text)
            canvas?.drawText(
                data[i].text, xOffset.toFloat() - textWidth / 2,
                (yBottom + context.dp2px(34f).toFloat()).toFloat(), textPaint
            )
        }
        super.onDraw(canvas)
    }


}