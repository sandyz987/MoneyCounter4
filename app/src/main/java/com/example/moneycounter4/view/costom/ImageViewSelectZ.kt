package com.example.moneycounter4.view.costom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.res.ResourcesCompat
import com.example.moneycounter4.R
import kotlin.math.abs

//月份的自定义view

class ImageViewSelectZ : androidx.appcompat.widget.AppCompatImageView {

    private lateinit var paint : Paint
    private lateinit var anim : ValueAnimator
    private var s : String? = null
    private var endSrcId = 0
    private var startSrcId = 0
    private var trX = 0f
    private var mSelected = false
    private var startX = 0f
    private var startY = 0f

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ImageViewSelectZ)

        paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        s = typedArray.getString(R.styleable.ImageViewSelectZ_month_text)
        startSrcId = typedArray.getResourceId(R.styleable.ImageViewSelectZ_month_pic_src,0)
        typedArray.recycle()
        setImageDrawable(ResourcesCompat.getDrawable(resources, startSrcId, null))
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredHeight + 130,measuredHeight)

    }

    override fun onDraw(canvas: Canvas?) {



        canvas?.translate(trX - (measuredWidth/4),0f)
        super.onDraw(canvas)
        paint.textSize = 40f
        val textWidth = paint.measureText(s)
        val textHeight = 50f
        canvas?.translate(width*4f/5f+trX/2f,height/2f)
        s?.let { canvas?.drawText(it,-10f, 0f+textHeight/4,paint) }
    }

    private fun onClick(){
        startAnimation1()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                startX = event.rawX
                startY = event.rawX
            }
            MotionEvent.ACTION_UP -> {
                if (abs(event.rawX-startX) <50f && abs(event.rawX-startY) <50f){
                    onClick()
                }
            }
        }
        return super.onTouchEvent(event)

    }

    fun setHint(string:String){
        s = string
        invalidate()
    }


    private fun startAnimation1() {
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