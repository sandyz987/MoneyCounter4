package com.example.moneycounter4.widgets

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.min

class FirstItemDecoration(private val recyclerView: RecyclerView, private val isFirst: (pos: Int) -> Boolean, private val getText: (pos: Int) -> String) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if(isFirst(recyclerView.getChildAdapterPosition(view))){
            outRect.set(0, 100, 0, 0)
        }else{
            outRect.set(0, 0, 0, 0)
        }
    }

    @SuppressLint("ResourceType")
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val paint = Paint()
        paint.color = Color.DKGRAY
        val textPaint = TextPaint()
        textPaint.color = Color.WHITE
        textPaint.textSize = 50f

        var height = 100
        if(isFirst(parent.getChildAdapterPosition(parent.getChildAt(1)))){
            height = min(100, parent.getChildAt(1)?.top?: 200 - 100)
        }
        c.drawRect(Rect(0, height - 100, parent.width, height), paint)
        c.drawText(getText(parent.getChildAdapterPosition(parent.getChildAt(0))), 20F, height - 30F, textPaint)

        paint.color = Color.GRAY
        for (i in 1 until parent.childCount){ // 返回显示在屏幕上的数量
            // getChildAt 返回显示出来的条目中，第i个条目的view实例
            // getChildAdapterPosition 返回第i个条目的实例在原数据集的真实位置
            if(isFirst(parent.getChildAdapterPosition(parent.getChildAt(i)))){
                c.drawRect(Rect(0, parent.getChildAt(i).top - 100, parent.width, parent.getChildAt(i).top), paint)
                c.drawText(getText(parent.getChildAdapterPosition(parent.getChildAt(i))).toString(), 20F, parent.getChildAt(i).top - 30F, textPaint)
            }

        }
    }
}