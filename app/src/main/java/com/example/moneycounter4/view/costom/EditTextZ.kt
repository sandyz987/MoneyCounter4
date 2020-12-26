package com.example.moneycounter4.view.costom

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import com.example.moneycounter4.R

//输入账号密码框，仿掌上重邮的hint动态效果
class EditTextZ : androidx.appcompat.widget.AppCompatEditText {
    private lateinit var animY : ValueAnimator
    private lateinit var animSize : ValueAnimator
    private var s : String? = null
    private var mY = 130f
    private var textS = 50f
    var focused = false
    private lateinit var paint: Paint
    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet):super(context, attributeSet){
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.EditTextZ)
        paint = Paint()
        paint.isAntiAlias = true
        s = typedArray.getString(R.styleable.EditTextZ_hint_text)
        typedArray.recycle()
        focused = false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth + 50,measuredHeight + 50)

    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = (Color.GRAY)
        paint.textSize = textS
        if (!focused && text.toString() == ""){
            paint.color = (Color.GRAY)
        }else{
            paint.color = (Color.BLUE)
        }
        s?.let { canvas?.drawText(it,10f,mY,paint) }


    }
    private fun startAnimation1() {
        animY = ValueAnimator.ofFloat(130f, 70f)
        animY.repeatCount = 0
        animY.repeatMode = ValueAnimator.REVERSE
        animY.duration = 500
        animY.addUpdateListener(AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Float
            mY = value
            postInvalidate()
        })
        animY.start()


        animSize = ValueAnimator.ofFloat(50f, 30f)
        animSize.repeatCount = 0
        animSize.repeatMode = ValueAnimator.REVERSE
        animSize.duration = 500
        animSize.addUpdateListener(AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Float
            textS = value
            postInvalidate()
        })
        animSize.start()
    }

    private fun startAnimation2() {
        animY = ValueAnimator.ofFloat(70f, 130f)
        animY.repeatCount = 0
        animY.repeatMode = ValueAnimator.REVERSE
        animY.duration = 500
        animY.addUpdateListener(AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Float
            mY = value
            postInvalidate()
        })
        animY.start()


        animSize = ValueAnimator.ofFloat(30f, 50f)
        animSize.repeatCount = 0
        animSize.repeatMode = ValueAnimator.REVERSE
        animSize.duration = 500
        animSize.addUpdateListener(AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Float
            textS = value
            postInvalidate()
        })
        animSize.start()
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        this.focused = focused
        if(focused){
            if(text.toString() == ""){
                startAnimation1()
            }
        }else{
            if(text.toString() == ""){
                startAnimation2()
            }
        }
    }

}