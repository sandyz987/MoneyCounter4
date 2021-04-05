package com.example.moneycounter4.view.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneycounter4.R
import com.example.moneycounter4.view.costom.CounterGraphView
import com.example.moneycounter4.view.costom.DataItem

data class WeekItemData(
    val title: String,
    val data: List<DataItem>
)

class GraphAdapter: ListAdapter<WeekItemData, GraphAdapter.GraphViewHolder>(WeekDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphViewHolder {
        return GraphViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_graph, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: GraphViewHolder, position: Int) {
        val counterGraphView = holder.itemView.findViewById<CounterGraphView>(R.id.counter_graph_view_item)
        counterGraphView.title = getItem(position).title
        counterGraphView.data = getItem(position).data
    }


    inner class GraphViewHolder(view: View): RecyclerView.ViewHolder(view)
    private class WeekDiffCallback: DiffUtil.ItemCallback<WeekItemData>() {
        override fun areItemsTheSame(oldItem: WeekItemData, newItem: WeekItemData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeekItemData, newItem: WeekItemData): Boolean {
            return oldItem == newItem
        }
    }


}

