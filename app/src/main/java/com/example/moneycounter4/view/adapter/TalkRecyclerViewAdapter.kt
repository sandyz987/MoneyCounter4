package com.example.moneycounter4.view.adapter

import android.annotation.SuppressLint
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
import com.example.moneycounter4.beannew.DynamicItem
import com.example.moneycounter4.beannew.findEquals
import com.example.moneycounter4.model.Config
import com.example.moneycounter4.utils.TimeUtil
import com.example.moneycounter4.view.costom.LikeView


class TalkRecyclerViewAdapter(
    private var mContext: Context,
    private val onItemClick: (DynamicItem, View) -> Unit

) :
    RecyclerView.Adapter<TalkRecyclerViewAdapter.ViewHolder>() {

    private var mLayoutInflater = LayoutInflater.from(mContext)
    private var mList: ArrayList<DynamicItem> = arrayListOf()

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
                    R.layout.item_talk_big,
                    container,
                    false
                )
            )
        }
        return ViewHolder(mLayoutInflater.inflate(R.layout.item_talk_big, container, false))
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

        val isPraise = mList[position].praise.findEquals { it.userId == Config.userId }
        holder.imageViewLikeView?.setHint((mList[position].praise.size - if (isPraise) 1 else 0).toString())
        holder.imageViewLikeView?.setSelect(isPraise)

        holder.textViewUsrName?.text = mList[position].userId
        holder.textViewContent?.text = mList[position].text.take(30)

        holder.textViewContent?.text?.length?.let {
            if (it >= 30) {
                holder.textViewContent.text = holder.textViewContent.text.toString() + "..."
                holder.textViewMore?.visibility = View.VISIBLE
            } else {
                holder.textViewMore?.visibility = View.GONE
            }
        }

        holder.textViewTime?.text = TimeUtil.getChatTimeStr(mList[position].submitTime)
        // TODO 图片显示
        holder.textViewUsrName?.text = mList[position].nickname
        holder.imageViewUsrPic?.let { Glide.with(mContext).load(mList[position].avatarUrl).into(it) }



        holder.imageViewTalkView?.setHint(mList[position].commentList.size.toString())


        holder.imageViewUsrPic?.setOnClickListener {
            val navController = Navigation.findNavController(holder.itemView)
            val bundle = Bundle()
            bundle.putBoolean("isMine", false)
            bundle.putString("userId", mList[position].userId)
            navController.navigate(R.id.action_global_fragmentMine, bundle)
        }

        holder.imageViewLikeView?.setOnClickListener {
            // TODO 点赞
        }
        holder.itemView.isClickable = true
        holder.itemView.setOnClickListener {
            onItemClick.invoke(mList[position], it)

        }
        holder.imageViewTalkView?.setOnClickListener {
            onItemClick.invoke(mList[position], it)
        }



        val likeListener = View.OnClickListener {
            holder.imageViewLikeView?.let {
                it.setSelect(!it.getSelect())
            }


        }
        holder.imageViewLikeView?.setOnClickListener(likeListener)


    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUsrName: TextView? = itemView.findViewById<TextView>(R.id.textViewUsrName)
        val textViewTime: TextView? = itemView.findViewById<TextView>(R.id.textViewTime)
        val textViewContent: TextView? = itemView.findViewById<TextView>(R.id.textViewContent)
        val imageViewUsrPic: ImageView? = itemView.findViewById<ImageView>(R.id.imageViewUsrPic)
        val imageViewPic: ImageView? = itemView.findViewById<ImageView>(R.id.imageViewPic)
        val imageViewSex: ImageView? = itemView.findViewById<ImageView>(R.id.imageViewSex)
        val buttonFollow: Button? = itemView.findViewById<Button>(R.id.buttonFollow)
        val imageViewLikeView: LikeView? =
            itemView.findViewById<LikeView>(R.id.imageViewLike)
        val imageViewTalkView: LikeView? =
            itemView.findViewById<LikeView>(R.id.imageViewTalk)
        val textViewMore: TextView? = itemView.findViewById<TextView>(R.id.textViewMore)

    }

    fun setList(list: ArrayList<DynamicItem>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }


}