package com.example.moneycounter4.view.adapter

import android.app.Activity
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.moneycounter4.R
import com.example.moneycounter4.bean.DataItem
import com.example.moneycounter4.bean.LogItem
import com.example.moneycounter4.databinding.ItemCounterDataBinding
import com.example.moneycounter4.databinding.ItemCounterTimeBinding
import com.example.moneycounter4.viewmodel.MainViewModel
import java.lang.Exception

//记账记录的adapter
@RequiresApi(Build.VERSION_CODES.N)
class LogRecyclerViewAdapter(private val mActivity:Activity,private val vm:MainViewModel,private var mContext:Context, private var list: ArrayList<DataItem>) :
    RecyclerView.Adapter<LogRecyclerViewAdapter.ViewHolder>() {

    private var mLayoutInflater = LayoutInflater.from(mContext)
    private var mList : ArrayList<LogItem>? = null

    init {
        setList(list)
    }

    fun setList(list: ArrayList<DataItem>){
        mList = ArrayList()
        val calendar = Calendar.getInstance()
        var nowDay = calendar.get(Calendar.DATE) + 1
        for(item:DataItem in list){
            calendar.timeInMillis = item.time
            if(nowDay != calendar.get(Calendar.DATE)){
                mList!!.add(LogItem(item.time))
                nowDay = calendar.get(Calendar.DATE)
            }

            val logItem = LogItem(0L)
            logItem.time = item.time
            logItem.tips = item.tips
            logItem.money = item.money
            logItem.type = item.type
            mList!!.add(logItem)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if(mList?.get(position)?.timeS ?: 0L != 0L)1 else 0
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {

        when(viewType){
            1->{
                val itemCounterTimeBinding = DataBindingUtil.inflate<ItemCounterTimeBinding>(mLayoutInflater,R.layout.item_counter_time,container,false)
                return ViewHolder(itemCounterTimeBinding.root)
            }
            0->{
                val itemCounterDataBinding = DataBindingUtil.inflate<ItemCounterDataBinding>(mLayoutInflater,R.layout.item_counter_data,container,false)
                return ViewHolder(itemCounterDataBinding.root)
            }
        }
        return ViewHolder(mLayoutInflater.inflate(R.layout.item_counter_data,container))
    }

    override fun getItemCount(): Int {

        return mList?.size?:0

    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try{
            DataBindingUtil.getBinding<ItemCounterTimeBinding>(holder.itemView)?.dataItem = mList?.get(position)

        }catch (e:Exception){
            DataBindingUtil.getBinding<ItemCounterDataBinding>(holder.itemView)?.dataItem = mList?.get(position)
            DataBindingUtil.getBinding<ItemCounterDataBinding>(holder.itemView)?.vm = vm
            holder.itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("dataItem",mList?.get(position))
                mActivity.findNavController(R.id.fragment).navigate(R.id.action_global_fragmentItemDetail,bundle)
            }
        }
    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

}