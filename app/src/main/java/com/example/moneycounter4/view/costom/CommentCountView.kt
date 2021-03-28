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


//评论/回复按钮，左边是可晃动的图标，右边是文字

class CommentCountView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private lateinit var paint : Paint
    private lateinit var anim : ValueAnimator
    private var s : String? = null
    private var startSrcId = 0
    private var trX = 0f
    private var mSelected = false

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommentCountView)

        paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.GRAY
        s = typedArray.getString(R.styleable.CommentCountView_reply_count_text)
        startSrcId = typedArray.getResourceId(R.styleable.CommentCountView_reply_pic_src,0)
        typedArray.recycle()
        setImageDrawable(ResourcesCompat.getDrawable(resources,startSrcId,null))
        setOnClickListener { onClick() }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredHeight + 200,measuredHeight)

    }

    override fun onDraw(canvas: Canvas?) {



        canvas?.translate(trX - (measuredWidth/4),0f)
        super.onDraw(canvas)
        paint.textSize = 40f
        val textWidth = paint.measureText(s)
        val textHeight = 50f
        canvas?.translate(width*4f/5f+trX,height/2f)
        s?.let { canvas?.drawText(it,0f, 0f+textHeight/3,paint) }
    }

    private fun onClick(){
        startAnimation1()
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