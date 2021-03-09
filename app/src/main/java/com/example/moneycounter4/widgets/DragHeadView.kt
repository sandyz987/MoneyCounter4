package com.example.moneycounter4.widgets

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moneycounter4.R
import kotlinx.android.synthetic.main.layout_head_view.view.*


class DragHeadView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var rv: RecyclerView? = null
    private val headView: View
    private var headViewHeight = 0
    private var nowOffset = 0

    private val beforeRefreshText: String
    private val canRefreshText: String
    private val refreshingText: String
    private val refreshedText: String

    private var beforeIc: Drawable? = null
    private var refreshedIc: Drawable? = null


    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DragHeadView)

        val headViewId = typedArray.getResourceId(R.styleable.DragHeadView_head_view, 0)

        beforeRefreshText =
            typedArray.getString(R.styleable.DragHeadView_before_refresh_text) ?: "下拉刷新"
        canRefreshText = typedArray.getString(R.styleable.DragHeadView_can_refresh_text) ?: "释放立即刷新"
        refreshingText = typedArray.getString(R.styleable.DragHeadView_refreshing_text) ?: "正在刷新..."
        refreshedText = typedArray.getString(R.styleable.DragHeadView_refreshed_text) ?: "刷新完成"
        refreshedIc = typedArray.getDrawable(R.styleable.DragHeadView_refreshed_ic)
        beforeIc = typedArray.getDrawable(R.styleable.DragHeadView_before_ic)

        typedArray.recycle()

        headView = LayoutInflater.from(context).inflate(headViewId, this, false)

        addView(headView, 0)
        headView.post {
            headViewHeight = headView.height
            transformHeadView(headViewHeight)
            requestLayout()
        }

        head_view_progress.visibility = View.INVISIBLE


    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)



    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        try {
            rv = getChildAt(1) as RecyclerView?

        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("the first view must be recyclerView!")
        }
    }


    private fun transformHeadView(yOffset: Int) {
        headView.layoutParams = (headView.layoutParams as LayoutParams).apply {
            topMargin = -yOffset
        }
        nowOffset = yOffset
    }


    enum class STATE {
        NONE,
        DRAGGING,
        RECOVER,
        REFRESHING
    }

    private var state: STATE = STATE.NONE
    private var downY = 0f


    private fun getOffset(event: MotionEvent): Float =
        ((event.y - downY) * 0.5f)

    var arrowState = -1

    private fun doRefreshAnim() {
        if (arrowState != 0) {

            val rotate = ObjectAnimator.ofFloat(head_view_arrow, "rotation", 0f, 180f)
            rotate.duration = 200
            rotate.repeatCount = 0
            rotate.start()

            arrowState = 0
        }
    }

    private fun doCancelRefreshAnim() {
        if (arrowState != 1) {
            val rotate = ObjectAnimator.ofFloat(head_view_arrow, "rotation", 180f, 360f)
            rotate.duration = 200
            rotate.repeatCount = 0
            rotate.start()
            arrowState = 1
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                val yOffset = getOffset(event).toInt()
                if (state == STATE.DRAGGING) {
                    transformHeadView(headViewHeight - yOffset)
                    if (getOffset(event) > headViewHeight) {
                        // 超过headView的高度，则执行刷新
                        head_view_state.text = canRefreshText
                        doRefreshAnim()

                    } else {
                        // 直接恢复
                        head_view_state.text = beforeRefreshText
                        doCancelRefreshAnim()
                    }
                }
                if (state == STATE.REFRESHING) {
                    transformHeadView(-yOffset)
                }

            }
            MotionEvent.ACTION_UP -> {
                if (getOffset(event) > headViewHeight) {
                    // 超过headView的高度，则执行刷新
                    refresh()
                } else {
                    // 直接恢复
                    recover()
                }
            }

        }
        performClick()
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    fun finishRefresh() {
        head_view_state.text = refreshedText
        head_view_arrow.visibility = View.VISIBLE
        head_view_progress.visibility = View.INVISIBLE
        doCancelRefreshAnim()
        head_view_arrow.setImageDrawable(refreshedIc)
        postDelayed({
            recover()

        }, 500L)

    }

    fun refresh() {
        head_view_arrow.visibility = View.INVISIBLE
        head_view_progress.visibility = View.VISIBLE

        head_view_state.text = refreshingText

        state = STATE.REFRESHING
        val anim = ValueAnimator.ofInt(nowOffset, 0)
        anim.duration = 300L
        anim.addUpdateListener {
            val i = it.animatedValue as Int
            transformHeadView(i)
            if (i == headViewHeight) {
                state = STATE.NONE
            }
        }
        anim.start()
        onRefreshAction?.invoke()
    }

    private fun recover() {
        state = STATE.RECOVER
        head_view_arrow.setImageDrawable(beforeIc)
        head_view_progress.visibility = View.INVISIBLE
        head_view_arrow.visibility = View.VISIBLE
        head_view_state.text = beforeRefreshText

        val anim = ValueAnimator.ofInt(nowOffset, headViewHeight)
        anim.duration = 300L
        anim.addUpdateListener {
            val i = it.animatedValue as Int
            transformHeadView(i)
            if (i == headViewHeight) {
                state = STATE.NONE
            }
        }
        anim.start()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var intercept = false

        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                downY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                // 如果不能向下滚动了，说明到顶了，则进入滑动状态
                if (rv?.canScrollVertically(-1) != true && state == STATE.NONE && ev.y - downY > 10) {
                    state = STATE.DRAGGING
                    intercept = true
                } else {
                    intercept = false
                }

            }
        }
        if (state == STATE.REFRESHING) {
            intercept = true
        }
        return intercept
    }

    var onRefreshAction: (() -> Unit)? = null

}