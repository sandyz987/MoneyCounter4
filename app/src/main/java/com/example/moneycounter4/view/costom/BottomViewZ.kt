package com.example.moneycounter4.view.costom

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.load.engine.Resource
import com.example.moneycounter4.R
import com.example.moneycounter4.widgets.LogW
import kotlin.math.abs

//底下导航栏的按钮 增加动画效果 左右晃动
//同时解决了onclick的判断问题

class BottomViewZ : androidx.appcompat.widget.AppCompatImageView {

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
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.attr)

        paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.GRAY
        s = typedArray.getString(R.styleable.attr_hint_a)
        endSrcId = typedArray.getResourceId(R.styleable.attr_end_src,0)
        startSrcId = typedArray.getResourceId(R.styleable.attr_start_src,0)
        typedArray.recycle()
        setImageDrawable(ResourcesCompat.getDrawable(resources,startSrcId,null))
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredHeight + 100,(measuredHeight * 1.0f).toInt())

    }

    override fun onDraw(canvas: Canvas?) {



        canvas?.translate(trX,0f)
        super.onDraw(canvas)
        paint.textSize = 30f
        val textWidth = paint.measureText(s)
        val textHeight = 40f
        canvas?.translate(width/2f,height.toFloat() - (0.25f*measuredWidth))
        //s?.let { canvas?.drawText(it,-textWidth/2f, textHeight,paint) }
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

    fun setSelect(boolean: Boolean){
        mSelected = boolean
        if (mSelected){
            setImageDrawable(ResourcesCompat.getDrawable(resources,endSrcId,null))
        }else{
            setImageDrawable(ResourcesCompat.getDrawable(resources,startSrcId,null))
        }
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