package com.example.moneycounter4.view.costom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.example.moneycounter4.R

class RoundedImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private var leftBottomCorner = 0f
    private var leftTopCorner = 0f
    private var rightTopCorner = 0f
    private var rightBottomCorner = 0f
    private val drawFilter: PaintFlagsDrawFilter
    private var rect: RectF? = null

    var mWidth = 0f
    var mHeight = 0f


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        rect = RectF(0f, 0f, mWidth, mHeight)
    }

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView)

        leftBottomCorner =
            typedArray.getDimension(R.styleable.RoundedImageView_left_bottom_corner, 0F)
        leftTopCorner =
            typedArray.getDimension(R.styleable.RoundedImageView_left_top_corner, 0F)
        rightTopCorner =
            typedArray.getDimension(R.styleable.RoundedImageView_right_top_corner, 0F)
        rightBottomCorner =
            typedArray.getDimension(R.styleable.RoundedImageView_right_bottom_corner, 0F)

        typedArray.recycle()

        drawFilter = PaintFlagsDrawFilter(
            0,
            Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG
        )
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {

        canvas?.drawFilter = drawFilter

        canvas?.clipPath(Path().apply {
            addRoundRect(
                rect ?: RectF(0f, 0f, mWidth, mHeight),
                floatArrayOf(leftTopCorner, leftTopCorner, rightTopCorner, rightTopCorner, rightBottomCorner, rightBottomCorner, leftBottomCorner, leftBottomCorner),
                Path.Direction.CCW
            )
        })
        super.onDraw(canvas)
    }

}