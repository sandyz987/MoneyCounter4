package com.example.moneycounter4.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.example.moneycounter4.R

class SingleSelectTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    private val noSelectColor: Int
    private val selectedColor: Int

    var isSelectedItem = false
        set(value) {
            field = value
            if (value) {
                setBackgroundColor(selectedColor)
            } else {
                setBackgroundColor(noSelectColor)
            }
            invalidate()
        }

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.SingleSelectItem)

        noSelectColor =
            typedArray.getColor(R.styleable.SingleSelectItem_no_select_color, Color.TRANSPARENT)
        selectedColor =
            typedArray.getColor(R.styleable.SingleSelectItem_selected_color, Color.TRANSPARENT)

        typedArray.recycle()
    }


}