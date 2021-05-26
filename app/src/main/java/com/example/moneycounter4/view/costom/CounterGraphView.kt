package com.example.moneycounter4.view.costom

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import com.example.moneycounter4.R
import com.example.moneycounter4.extensions.dp2px
import com.example.moneycounter4.extensions.sp
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.model.dao.DateItem
import com.example.moneycounter4.model.dao.getByDuration
import com.example.moneycounter4.viewmodel.MainApplication
import com.google.gson.Gson
import kotlin.math.max

/**
 *@author zhangzhe
 *@date 2021/3/31
 *@description
 */

data class DataItem(
    val text: String = "",
    var data: Double = 0.0,
    val dateItem: DateItem = DateItem(),
    val duration: Long = 0L
)

@RequiresApi(Build.VERSION_CODES.M)
class CounterGraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth = 0f
    private var mHeight = 0f

    init {
        setOnClickListener {
            if (data.isNotEmpty() && data[0].duration != 0L) {
                val list = DataReader.db?.counterDao()
                    ?.getByDuration(data[0].dateItem, 0L, data[0].duration, -1)
                findNavController().navigate(R.id.action_global_fragmentDistribution, Bundle().apply {
                    putString("data", Gson().toJson(list))
                })
            }
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        rect = RectF(0f, 0f, mWidth, mHeight)
        dataBlank = mWidth / (data.size + 1.0)
    }

    private var dataBlank = 0.0
    private val maxData: Double
        get() {
            var d = 0.0
            data.forEach { d = max(d, it.data) }
            return d
        }
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
        textSize = context.sp(14).toFloat()
    }
    private val moneyPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.BLUE
        strokeCap = Paint.Cap.ROUND
        textSize = context.sp(10).toFloat()
    }
    private val titlePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
        strokeCap = Paint.Cap.ROUND
        textSize = context.sp(18).toFloat()
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
            value.forEach { if (it.data < 0.0) it.data = 0.0 }
            field = value
            dataBlank = mWidth / (value.size + 1.0)
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

        if (data.isNotEmpty() && data[0].duration != 0L) {
            val startDateString = "周${data[0].text}：${data[0].dateItem.month}日${data[0].dateItem.day}日"
            canvas?.drawText(startDateString, (0.4 * mWidth).toFloat(), (0.2 * mHeight).toFloat(), titlePaint)
        }

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
            var textWidth = textPaint.measureText(data[i].text)
            canvas?.drawText(
                data[i].text, xOffset.toFloat() - textWidth / 2,
                (yBottom + context.dp2px(34f).toFloat()).toFloat(), textPaint
            )
            if (data[i].data != 0.0) {
                val moneyText = String.format("%.2f", data[i].data)
                textWidth = moneyPaint.measureText(moneyText)
                canvas?.drawText(
                    String.format("%.2f", data[i].data * animValue),
                    xOffset.toFloat() - textWidth / 2,
                    (yBottom.toFloat() - yOffset.toFloat() * animValue),
                    moneyPaint
                )
            }
        }
        super.onDraw(canvas)
    }


}