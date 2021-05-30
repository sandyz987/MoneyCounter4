package com.example.moneycounter4.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneycounter4.R
import com.google.android.material.chip.Chip

class SingleSelectAdapter(private val rv: RecyclerView, private val mList: List<String>) :
    RecyclerView.Adapter<SingleSelectAdapter.ItemViewHolder>() {
    private val selectedSet = mutableSetOf<String>()

    inner class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var text: TextView = v.findViewById(R.id.counter_graph_view_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.chip_single_select_chip, parent, false)
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (position == 0) {
            /**
             * 当点击不限后，取消对所有item的选择
             */
            holder.text.text = "不限"
            (holder.itemView as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    for (i in 1 until itemCount + 1) {
                        (rv.findViewHolderForAdapterPosition(i)?.itemView as Chip).isChecked = false
                    }
                    selectedSet.clear()
                }
            }
        } else {
            bindView(holder, position - 1)
        }
    }

    private fun bindView(holder: ItemViewHolder, realPosition: Int) {
        (rv.findViewHolderForAdapterPosition(0)?.itemView as Chip).isChecked = false
        holder.text.text = mList[realPosition]
        (holder.itemView as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedSet.add(mList[realPosition])
            } else {
                selectedSet.remove(mList[realPosition])
            }
        }
    }

    override fun getItemCount() = mList.size + 1
}