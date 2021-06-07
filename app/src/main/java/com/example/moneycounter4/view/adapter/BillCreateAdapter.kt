package com.example.moneycounter4.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneycounter4.R
import com.example.moneycounter4.model.BillPicGetter
import kotlinx.android.synthetic.main.item_single_bill.view.*

/**
 *@author zhangzhe
 *@date 2021/6/7
 *@description
 */

class BillCreateAdapter(val onItemSelected: ((Int) -> Unit)) :
    RecyclerView.Adapter<BillCreateAdapter.ViewHolder>() {


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_single_bill, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.iv_item_bill_pic.setImageDrawable(BillPicGetter.getBillPic(position))
        holder.itemView.tv_item_bill_name.text = ""
        holder.itemView.setOnClickListener {
            onItemSelected.invoke(position)
        }
    }

    override fun getItemCount(): Int = BillPicGetter.getSize()

}