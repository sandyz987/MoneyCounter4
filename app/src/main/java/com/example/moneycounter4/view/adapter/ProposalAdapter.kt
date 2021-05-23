package com.example.moneycounter4.view.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneycounter4.R

/**
 *@author zhangzhe
 *@date 2021/4/9
 *@description
 */

class ProposalAdapter : RecyclerView.Adapter<ProposalAdapter.ProposalViewHolder>() {

    val list = ArrayList<String>()

    inner class ProposalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_proposal: TextView = itemView.findViewById(R.id.tv_proposal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProposalViewHolder {
        return ProposalViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_proposal, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProposalViewHolder, position: Int) {
        holder.tv_proposal.text = list[position].let {
            SpannableString(it).apply {
                for (i in it.indices) {
                    if ((it[i] in '0'..'9') || it[i] == '%' || it[i] == '.' || it[i] == '￥') {
                        setSpan(
                            ForegroundColorSpan(Color.BLUE),
                            i, i + 1,
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                        )
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<String>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}