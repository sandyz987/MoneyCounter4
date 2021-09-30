package com.example.moneycounter4.view.adapter.homewidget

import android.view.View
import android.view.ViewGroup
import androidx.annotation.FloatRange
import androidx.recyclerview.widget.RecyclerView
import com.example.moneycounter4.extensions.swapOrder
import com.example.moneycounter4.widgets.ItemMoveCallback


/**
 *@author zhangzhe
 *@date 2021/5/31
 *@description
 */

class WidgetAdapter(
    private val widgetList: MutableList<String>,
    private val onInnerClickAction: ((Int) -> Unit),
    private val onChange: ((List<String>) -> Unit)
) :
    RecyclerView.Adapter<WidgetAdapter.BaseViewHolder>(), ItemMoveCallback {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return WidgetItemGetter.getViewHolder(parent, viewType)
            .also { setAlphaAllView(it.itemView, 0.9f) }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        WidgetItemGetter.bindView(holder, onInnerClickAction)
    }


    override fun getItemCount() = widgetList.size

    override fun getItemViewType(position: Int): Int {
        return WidgetItemGetter.getTypeByName(widgetList[position])
    }

    open class BaseViewHolder(v: View, val viewType: Int) : RecyclerView.ViewHolder(v)

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        swapOrder(widgetList as MutableList<*>, fromPosition, toPosition)
        onChange.invoke(widgetList)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {}
    override fun onItemMoveFinish() {
        notifyDataSetChanged()
    }


    companion object {
        private fun setAlphaAllView(view: View?, @FloatRange(from = 0.0, to = 1.0) alpha: Float) {
            if (view == null) {
                return
            }
            if (view.background != null) {
                view.background.mutate().alpha = (alpha * 255).toInt()
            }
            view.alpha = alpha
            //设置子view透明度
            if (view is ViewGroup) {
                val vp = view
                for (i in 0 until vp.childCount) {
                    val viewChild = vp.getChildAt(i)
                    //调用本身（递归）
                    setAlphaAllView(viewChild, alpha)
                }
            }
        }
    }

}