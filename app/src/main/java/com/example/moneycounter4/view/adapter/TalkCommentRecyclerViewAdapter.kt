package com.example.moneycounter4.view.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moneycounter4.R
import com.example.moneycounter4.bean.ItemAccount
import com.example.moneycounter4.bean.TalkItem
import com.example.moneycounter4.beannew.CommentItem
import com.example.moneycounter4.beannew.DynamicItem
import com.example.moneycounter4.utils.TimeUtil
import com.example.moneycounter4.view.costom.ImageViewInfoZ
import com.example.moneycounter4.view.costom.ImageViewInfoZLike


class TalkCommentRecyclerViewAdapter(
    private var mContext: Context,
    private val onItemClick: (CommentItem, View) -> Unit

) :
    RecyclerView.Adapter<TalkCommentRecyclerViewAdapter.ViewHolder>() {

    private var mLayoutInflater = LayoutInflater.from(mContext)
    private var mList: ArrayList<CommentItem> = arrayListOf()

    override fun getItemViewType(position: Int): Int {
        return if (position == mList.size) {
            1
        } else {
            0
        }
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {

        when (viewType) {
            1 -> return ViewHolder(mLayoutInflater.inflate(R.layout.item_more, container, false))
            0 -> return ViewHolder(
                mLayoutInflater.inflate(
                    R.layout.item_talk_small,
                    container,
                    false
                )
            )
        }
        return ViewHolder(mLayoutInflater.inflate(R.layout.item_talk_small, container, false))
    }

    override fun getItemCount(): Int {

        return mList.size + 1

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == mList.size) {
            return
        }
        holder.imageViewLike?.setSelect(false)
        holder.imageViewLike?.setHint("0")

        holder.textViewUsrName?.text = mList[position].nickname
        holder.textViewContent?.text = mList[position].text



        holder.textViewTime?.text = TimeUtil.getChatTimeStr(mList[position].submitTime)
        // TODO 图片显示
        holder.imageViewUsrPic?.let { Glide.with(mContext).load(mList[position].avatarUrl).into(it) }





        holder.imageViewUsrPic?.setOnClickListener {
            val navController = Navigation.findNavController(holder.itemView)
            val bundle = Bundle()
            bundle.putBoolean("isMine", false)
            bundle.putString("userId", mList[position].userId)
            navController.navigate(R.id.action_global_fragmentMine, bundle)
        }

        holder.imageViewLike?.setOnClickListener {
            // TODO 点赞
        }
        holder.itemView.isClickable = true
        holder.itemView.setOnClickListener {
            onItemClick.invoke(mList[position], it)

        }



        val likeListener = View.OnClickListener {
            holder.imageViewLike?.let {
                it.setSelect(!it.getSelect())
            }


        }
        holder.imageViewLike?.setOnClickListener(likeListener)


    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUsrName: TextView? = itemView.findViewById<TextView>(R.id.textViewUsrName)//
        val textViewTime: TextView? = itemView.findViewById<TextView>(R.id.textViewTime)//
        val textViewContent: TextView? = itemView.findViewById<TextView>(R.id.textViewContent)//
        val imageViewUsrPic: ImageView? = itemView.findViewById<ImageView>(R.id.imageViewUsrPic)//
        val imageViewLike: ImageViewInfoZLike? =
            itemView.findViewById<ImageViewInfoZLike>(R.id.imageViewLike)
        val rvCommentInner: RecyclerView? = itemView.findViewById<RecyclerView>(R.id.rv_comment_inner)

    }

    fun setList(list: List<CommentItem>) {
        mList.clear()
        mList.addAll(list)
        notifyItemRangeChanged(0, mList.size)
    }


}