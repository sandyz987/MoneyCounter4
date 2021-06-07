package com.example.moneycounter4.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moneycounter4.R
import com.example.moneycounter4.extensions.swapOrder
import com.example.moneycounter4.model.BillItem
import com.example.moneycounter4.model.BillWalletSettingUtil
import com.example.moneycounter4.widgets.ItemMoveCallback
import com.mredrock.cyxbs.qa.ui.widget.OptionalDialog
import kotlinx.android.synthetic.main.item_single_bill.view.*

/**
 *@author zhangzhe
 *@date 2021/6/7
 *@description 单选的带一个图片一个标题的Adapter,账本
 */


class SingleSelectBillAdapter2 : RecyclerView.Adapter<SingleSelectBillAdapter2.ViewHolder>(),
    ItemMoveCallback {

    private var sourceList: MutableList<BillItem> =
        BillWalletSettingUtil.settingData?.billList ?: mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private val selectedMap = HashMap<String, Boolean>()
    var currentSelect = ""

    init {
        if (!sourceList.isNullOrEmpty()) {
            setSelect(sourceList[0].name)
        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val iv_item_bill_pic: ImageView = v.iv_item_bill_pic
        val tv_item_bill_name: TextView = v.tv_item_bill_name
        val iv_item_selected: ImageView = v.iv_item_selected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_bill_select, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(recyclerView.context)
            .load(BillWalletSettingUtil.findBillPicByName(sourceList[position].name))
            .into(holder.iv_item_bill_pic)
        holder.tv_item_bill_name.text = sourceList[position].name
        holder.iv_item_selected.visibility =
            if (isSelect(sourceList[position].name)) View.VISIBLE else View.INVISIBLE
        holder.itemView.setOnClickListener {
            Log.e("sandyzhang", sourceList[position].name)
            if (!isSelect(sourceList[position].name)) {
                setSelect(sourceList[position].name)
            }
            notifyDataSetChanged()
        }
    }

    private fun isSelect(item: String): Boolean =
        if (selectedMap[item] != null) {
            selectedMap[item] == true
        } else {
            selectedMap[item] = false
            false
        }

    private fun setSelect(item: String) {
        currentSelect = item
        val it = selectedMap.iterator()
        while (it.hasNext()) {
            it.next().setValue(false)
        }
        selectedMap[item] = true
    }


    override fun getItemCount(): Int = sourceList.size


    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        swapOrder(sourceList as MutableList<*>, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        BillWalletSettingUtil.save()
    }

    override fun onItemDismiss(position: Int) {
        OptionalDialog.show(
            recyclerView.context,
            "真的要删除吗？不会删除已经记录的条目，可以通过添加同名账单恢复~",
            { notifyItemChanged(position) }) {
            sourceList.removeAt(position)
            notifyItemRemoved(position)
            BillWalletSettingUtil.save()
        }
    }

    override fun onItemMoveFinish(recyclerView: RecyclerView) {
        recyclerView.post { notifyDataSetChanged() }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }
}