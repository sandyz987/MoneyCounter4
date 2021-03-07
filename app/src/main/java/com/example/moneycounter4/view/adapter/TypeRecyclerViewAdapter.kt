package com.example.moneycounter4.view.adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.moneycounter4.R
import com.example.moneycounter4.bean.TypeItem
import com.example.moneycounter4.databinding.ItemTypeBinding
import com.example.moneycounter4.view.activity.ActivityMain

//记账记录的adapter
class TypeRecyclerViewAdapter(private val mActivity:Activity,private var mContext:Context, private var mList: ObservableArrayList<TypeItem>?,private val showSettingItem:Int) :
    RecyclerView.Adapter<TypeRecyclerViewAdapter.ViewHolder>() {

    private var mLayoutInflater = LayoutInflater.from(mContext)
    private var selectedPos = 0
    private var onClickAction: ((t: TypeItem) -> Unit)? = null
    private var selectedTypeItem: TypeItem? = null

    fun setOnClick(onClick: (t: TypeItem) -> Unit) {
        this.onClickAction = onClick
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {
        val itemTypeBinding = DataBindingUtil.inflate<ItemTypeBinding>(
            mLayoutInflater,
            R.layout.item_type,
            container,
            false
        )
        return ViewHolder(itemTypeBinding.root)
    }

    override fun getItemCount(): Int {
        return (mList?.size ?: 0) + showSettingItem
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemTypeBinding>(holder.itemView)

        if(position == mList?.size){
            binding?.typeItem = TypeItem("设置类目",R.drawable.tallytype_set)
            holder.itemView.setOnClickListener {
                //======set type
                val navController = mActivity.findNavController(R.id.fragment)
                navController.popBackStack()
                val bundle = Bundle()
                bundle.putInt("position",(if((mActivity as ActivityMain).viewModel.typeListIn == mList) 1 else 0))
                navController.navigate(R.id.action_global_fragmentTypeEdit,bundle)
            }
            return
        }

        binding?.typeItem = mList?.get(position)
        if(position == selectedPos){
            binding?.width = 5
        }else{
            binding?.width = 0
        }
        holder.itemView.setOnClickListener {
            Thread {
                Thread.sleep(200)
                mActivity.runOnUiThread {
                    selectedPos = position
                    selectedTypeItem = mList?.get(position)
                    notifyDataSetChanged()
                    mList?.get(position)?.let { it1 -> onClickAction?.invoke(it1) }
                }
            }.start()
        }
        binding?.executePendingBindings();
    }


    fun setList(list: ObservableArrayList<TypeItem>?){
        this.mList = list
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}