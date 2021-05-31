package com.example.moneycounter4.view.adapter.homewidget

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneycounter4.widgets.ItemMoveCallback
import java.util.*

/**
 *@author zhangzhe
 *@date 2021/5/31
 *@description
 */

class WidgetAdapter(private val widgetList: MutableList<String>) :
    RecyclerView.Adapter<WidgetAdapter.BaseViewHolder>(), ItemMoveCallback {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return WidgetItemGetter.getViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        WidgetItemGetter.bindView(holder)
    }


    override fun getItemCount() = widgetList.size

    override fun getItemViewType(position: Int): Int {
        return WidgetItemGetter.getTypeByName(widgetList[position])
    }

    open class BaseViewHolder(v: View, val viewType: Int) : RecyclerView.ViewHolder(v)

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(widgetList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {}


}