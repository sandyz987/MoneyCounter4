package com.example.moneycounter4.view.adapter

import android.app.IntentService
import android.os.HandlerThread
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneycounter4.R
import com.google.android.material.chip.Chip

class SingleSelectAdapter(private val mList: List<String>) :
    RecyclerView.Adapter<SingleSelectAdapter.ItemViewHolder>() {
    private val selectedSet = mutableSetOf<String>()
    private val selectedMap = hashMapOf<Int, Boolean>()
    private var noItemSelected = true

    inner class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var text: Chip = v.findViewById(R.id.chip_single_select_chip)
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
                if (isChecked) {
                    selectedSet.clear()
                    val it = selectedMap.iterator()
                    while (it.hasNext()) {
                        if (it.next().key != 0) {
                            it.next().setValue(false)
                        }
                    }
                }
            }
            (holder.itemView as Chip?)?.isChecked = selectedMap[0] ?: true
        } else {
            bindView(holder, position - 1)
        }
    }

    private fun bindView(holder: ItemViewHolder, realPosition: Int) {


        (holder.itemView as Chip?)?.isChecked = selectedMap[realPosition + 1].let {
            if (it == null) {
                selectedMap[realPosition + 1] = false
                false
            } else {
                it
            }
        }

        holder.text.text = mList[realPosition]
        (holder.itemView as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedSet.add(mList[realPosition])
                selectedMap[realPosition + 1] = true
                selectedMap[0] = false

            } else {
                selectedSet.remove(mList[realPosition])
                selectedMap[realPosition + 1] = false

            }
            Log.e("sandyzhang", "=======" + selectedSet.toString())
        }

    }

    override fun getItemCount() = mList.size + 1
}