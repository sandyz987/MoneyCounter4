package com.example.moneycounter4.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.moneycounter4.R


class SingleSelectFlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FlowLayout(context, attrs, defStyleAttr) {

    var singleSelect = false

    var onSelect: ((List<String>) -> Unit)? = null

    data class Pair(
        var s: String = "",
        var selected: Boolean = false
    )

    private val selectedList: MutableList<Pair> = mutableListOf()

    fun setOptions(vararg string: String) {
        selectedList.clear()
        selectedList.add(Pair("全部", true))
        string.forEach { selectedList.add(Pair(it, false)) }
        refresh()
    }

    fun getOptions(): List<String> {
        val l = mutableListOf<String>()
        selectedList.forEach {
            if (it.selected && it.s != "全部") {
                l.add(it.s)
            }
        }
        return l
    }

    private fun refresh() {
        removeAllViews()

        selectedList.forEach {
            val v = LayoutInflater.from(context).inflate(R.layout.item_single_select, this, false)
            val tv = v.findViewById<SingleSelectTextView>(R.id.select_view)
            tv.isSelectedItem = it.selected
            tv.text = it.s
            v.setOnClickListener { _ ->
                if (it.s == "全部") {
                    clearSelect()
                    onSelect?.invoke(getOptions())

                } else {
                    if (singleSelect) {
                        clearSelect()
                        reverseSelect(tv.text.toString())
                    } else {
                        reverseSelect(tv.text.toString())
                    }
                }
            }

            addView(v)

        }
    }

    private fun clearSelect() {
        val it = selectedList.iterator()
        it.next().selected = true
        while (it.hasNext()) {
            val tmp = it.next()
            tmp.selected = false
        }
        refresh()
    }

    fun reverseSelect(s: String) {
        val it = selectedList.iterator()
        it.next().selected = false
        while (it.hasNext()) {
            val tmp = it.next()
            if (tmp.s == s) {
                tmp.selected = !tmp.selected
            }
        }
        refresh()
        onSelect?.invoke(getOptions())
    }

}