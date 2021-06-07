package com.example.moneycounter4.view.adapter

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.moneycounter4.R
import com.example.moneycounter4.bean.TypeItem
import com.example.moneycounter4.databinding.ItemTypeRemoveBinding
import com.example.moneycounter4.extensions.swapOrder
import com.example.moneycounter4.view.activity.ActivityMain
import com.example.moneycounter4.viewmodel.GlobalViewModel
import com.example.moneycounter4.widgets.ItemMoveCallback
import com.example.moneycounter4.widgets.LogW

//记账记录的adapter
@RequiresApi(Build.VERSION_CODES.N)
class TypeEditRecyclerViewAdapter(
    private val mActivity: Activity,
    private val vm: GlobalViewModel,
    private var mList: MutableList<TypeItem>?
) :
    RecyclerView.Adapter<TypeEditRecyclerViewAdapter.ViewHolder>(), ItemMoveCallback {


    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {
        val itemTypeRemoveBinding = DataBindingUtil.inflate<ItemTypeRemoveBinding>(
            LayoutInflater.from(container.context),
            R.layout.item_type_remove,
            container,
            false
        )
        return ViewHolder(itemTypeRemoveBinding.root)
    }

    override fun getItemCount(): Int {
        return (mList?.size ?: 0) + 1
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemTypeRemoveBinding>(holder.itemView)

        if (position == mList?.size) {
            binding?.typeItem = TypeItem("添加类目", R.drawable.tallytype_set)
            holder.imageViewDel.visibility = View.INVISIBLE
            holder.itemView.setOnClickListener {
                //======add type
                val navController = mActivity.findNavController(R.id.fragment)
                navController.popBackStack()
                navController.navigate(R.id.action_global_fragmentAddType)
            }
            return
        }
        holder.imageViewDel.visibility = View.VISIBLE
        holder.itemView.setOnClickListener {}
        holder.imageViewDel.setOnClickListener {
            //======del type
            mList?.get(position)?.let { it -> (mActivity as ActivityMain).viewModel.delType(it) }
            LogW.d("del")
            notifyDataSetChanged()
        }

        binding?.typeItem = mList?.get(position)
        binding?.executePendingBindings();
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewDel: ImageView = itemView.findViewById(R.id.imageViewDel)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < itemCount - 1 && toPosition < itemCount - 1) {
            //交换位置
            swapOrder(mList as MutableList<*>, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
            vm.saveType()
        } else {
            notifyDataSetChanged()
        }
    }

    override fun onItemDismiss(position: Int) {
        if (position < itemCount - 1) {
            //移除数据
            mList?.removeAt(position)
            notifyItemRemoved(position)
            vm.saveType()
        } else {
            notifyDataSetChanged()
        }

    }

    override fun onItemMoveFinish(recyclerView: RecyclerView) {
        recyclerView.post { notifyDataSetChanged() }
    }


}