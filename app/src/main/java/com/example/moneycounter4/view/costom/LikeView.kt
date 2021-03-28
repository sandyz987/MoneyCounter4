package com.example.moneycounter4.view.costom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.example.moneycounter4.R

//点赞按钮，左边是图标右边是文字，并且可以选中

open class LikeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private lateinit var paint: Paint
    private lateinit var anim: ValueAnimator
    var s: String? = null
    private var endSrcId = 0
    private var startSrcId = 0
    private var trX = 0f
    private var mSelected = false
    private var startX = 0f
    private var startY = 0f


    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.LikeView)

        paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.GRAY
        s = typedArray.getString(R.styleable.LikeView_like_count_text)
        endSrcId = typedArray.getResourceId(R.styleable.LikeView_liked_src, 0)
        startSrcId = typedArray.getResourceId(R.styleable.LikeView_no_liked_src, 0)
        typedArray.recycle()
        setImageDrawable(ResourcesCompat.getDrawable(resources, startSrcId, null))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredHeight + 200, measuredHeight)

    }

    override fun onDraw(canvas: Canvas?) {


        canvas?.translate(trX - (measuredWidth / 4), 0f)
        super.onDraw(canvas)
        paint.textSize = 40f
        val textHeight = 50f
        canvas?.translate(width * 4f / 5f + trX, height / 2f)
        s?.let {
            canvas?.drawText(
                it,
                0f,
                0f + textHeight / 3,
                paint
            )
        }
    }


    fun getSelect(): Boolean {
        return mSelected
    }

    fun setSelect(boolean: Boolean) {
        mSelected = boolean
        if (mSelected) {
            setImageDrawable(ResourcesCompat.getDrawable(resources, endSrcId, null))
        } else {
            setImageDrawable(ResourcesCompat.getDrawable(resources, startSrcId, null))
        }
        invalidate()
    }

    fun setHint(string: String) {
        s = string
        invalidate()
    }


    fun startAnimation1() {
        anim = ValueAnimator.ofFloat(0f, 10f, 0f, -10f, 0f, 10f, 0f)
        anim.repeatCount = 0
        anim.repeatMode = ValueAnimator.REVERSE
        anim.duration = 300
        anim.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Float
            trX = value
            postInvalidate()
        })
        anim.start()
    }


}