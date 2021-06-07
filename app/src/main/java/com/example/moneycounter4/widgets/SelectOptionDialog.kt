package com.example.moneycounter4.widgets

import android.content.Context
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import kotlin.math.max

/**
 *@author zhangzhe
 *@date 2021/6/7
 *@description
 */

class SelectOptionDialog(
    private val context: Context,
    private val option: List<String>,
    private val defaultSelect: String,
    private val onOptionSelected: ((String) -> Unit)
) {


    fun show() {
        val pvOptions: OptionsPickerView<String> =
            OptionsPickerBuilder(
                context
            ) { options1, _, _, _ ->
                onOptionSelected.invoke(option[options1])
            }.build()
        pvOptions.setPicker(option)
        val index = max(0, option.indexOf(defaultSelect))
        pvOptions.setSelectOptions(index)
        pvOptions.show()
    }
}