package com.example.moneycounter4.view.adapter

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moneycounter4.R
import com.example.moneycounter4.bean.ItemAccount

//点赞横向拖动列表的adapter很简单
class LikeRecyclerViewAdapter(private val mActivity:Activity?, private var mContext: Context, private var mList: ArrayList<ItemAccount>) :
    RecyclerView.Adapter<LikeRecyclerViewAdapter.ViewHolder>() {

    private var mLayoutInflater = LayoutInflater.from(mContext)


    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {

        when(viewType){
            1->return ViewHolder(mLayoutInflater.inflate(R.layout.item_like_account, container, false))
        }
        return ViewHolder(mLayoutInflater.inflate(R.layout.item_talk_big, container, false))
    }

    override fun getItemCount(): Int {

        return mList.size

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textViewUsrName?.text = mList[position].usrName
        //ImageLoader.with(mContext).load(mList[position].picUrl).into(holder.imageViewUsrPic)
        holder.imageViewUsrPic?.let{
            if (mActivity != null) {
                Glide.with(mActivity).load(mList[position].picUrl).into(it)
            }
        }
        holder.itemView.setOnClickListener {
            val navController = mActivity?.findNavController(R.id.recyclerViewLikeAccount)
            val bundle = Bundle()
            bundle.putString("userId", mList[position].userId)
            bundle.putBoolean("isMine", false)
            navController?.navigate(R.id.action_global_fragmentMine, bundle)
        }

    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewUsrName: TextView? = itemView.findViewById<TextView>(R.id.textViewUsrName)
        val imageViewUsrPic: ImageView? = itemView.findViewById<ImageView>(R.id.imageViewUsrPic)

    }

}