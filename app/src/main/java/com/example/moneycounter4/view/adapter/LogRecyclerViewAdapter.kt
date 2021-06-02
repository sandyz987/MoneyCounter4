package com.example.moneycounter4.view.adapter

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.moneycounter4.R
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.databinding.ItemCounterDataBinding
import com.example.moneycounter4.utils.TimeUtil
import com.example.moneycounter4.viewmodel.GlobalViewModel
import com.example.moneycounter4.widgets.FirstItemDecoration

//记账记录的adapter
@RequiresApi(Build.VERSION_CODES.N)
class LogRecyclerViewAdapter(
    private val fragment: Fragment, private val vm: GlobalViewModel,
    private val mList: List<CounterDataItem>, rv: RecyclerView
) :
    RecyclerView.Adapter<LogRecyclerViewAdapter.ViewHolder>() {

    private var itemDecoration: FirstItemDecoration? = null

    init {
        itemDecoration = FirstItemDecoration(rv, {
            return@FirstItemDecoration if (it <= 0) {
                true
            } else {
                if (it < mList.size) {
                    TimeUtil.monthStr(mList[it - 1].time!!) != TimeUtil.monthStr(mList[it].time!!)
                } else {
                    false
                }

            }
        }, {
            return@FirstItemDecoration if (mList.size == 0) ""
            else if (it < mList.size) {
                TimeUtil.monthStr(mList[it].time!!)
            } else {
                ""
            }
        })
        rv.addItemDecoration(itemDecoration!!)
    }


    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {

        val itemCounterDataBinding = DataBindingUtil.inflate<ItemCounterDataBinding>(
            LayoutInflater.from(container.context),
            R.layout.item_counter_data,
            container,
            false
        )
        return ViewHolder(itemCounterDataBinding.root)

    }

    override fun getItemCount(): Int {

        return mList.size

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        DataBindingUtil.getBinding<ItemCounterDataBinding>(holder.itemView)?.dataItem =
            mList[position]
        DataBindingUtil.getBinding<ItemCounterDataBinding>(holder.itemView)?.vm = vm
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("dataItem", mList[position])
            fragment.findNavController()
                .navigate(R.id.action_global_fragmentItemDetail, bundle)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}