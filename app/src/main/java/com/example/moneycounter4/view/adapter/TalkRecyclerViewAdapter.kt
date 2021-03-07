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
import com.example.moneycounter4.utils.TimeUtil
import com.example.moneycounter4.view.costom.ImageViewInfoZ
import com.example.moneycounter4.view.costom.ImageViewInfoZLike

//加载帖子的adapter，有两处用了。

class TalkRecyclerViewAdapter(
    private val mActivity: Activity?,
    private var mContext: Context,
    private var mList: ArrayList<TalkItem>,
    private val mUrl: String?,
    private val mUserId: String?,
    private val mToken: Int
) :
    RecyclerView.Adapter<TalkRecyclerViewAdapter.ViewHolder>() {

    private var mLayoutInflater = LayoutInflater.from(mContext)

    override fun getItemViewType(position: Int): Int {
        return if (position == mList.size) {
            1
        } else {
            0
        }
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {

        when(viewType){
            1->return ViewHolder(mLayoutInflater.inflate(R.layout.item_more, container, false))
            0->return ViewHolder(mLayoutInflater.inflate(R.layout.item_talk_big, container, false))
        }
        return ViewHolder(mLayoutInflater.inflate(R.layout.item_talk_big, container, false))
    }

    override fun getItemCount(): Int {

        return mList.size + 1

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == mList.size){
            return
        }
        holder.imageViewLike?.setSelect(false)
        holder.imageViewLike?.setHint("0")

        holder.textViewUsrName?.text = mList[position].userId
        holder.textViewContent?.text = mList[position].text.take(30)

        holder.textViewContent?.text?.length?.let {
            if (it >= 30 ){
                holder.textViewContent.text = holder.textViewContent.text.toString() +"..."
                holder.textViewMore?.visibility = View.VISIBLE
            }else{
                holder.textViewMore?.visibility = View.GONE
            }
        }

        holder.textViewTime?.text = TimeUtil.getChatTimeStr(mList[position].time)
        if(mList[position].picUrl !=null &&mList[position].picUrl != "null" && mList[position].picUrl != ""){
            //ImageLoader.with(mContext).load(mList[position].picUrl).into(holder.imageViewPic)
            holder.imageViewPic?.let { Glide.with(mContext).load(mList[position].picUrl).into(it) }
            holder.imageViewPic?.visibility = View.VISIBLE
        }else{
            holder.imageViewPic?.visibility = View.GONE
        }
        holder.textViewUsrName?.text = mList[position].usrName
        holder.imageViewUsrPic?.let { Glide.with(mContext).load(mList[position].usrPic).into(it) }


//        when(list[position].sex){
//            "男"->holder.imageViewSex?.setImageResource(R.drawable.ic_man)
//            "女"->holder.imageViewSex?.setImageResource(R.drawable.ic_woman)
//            else->holder.imageViewSex?.setImageBitmap(null)
//        }

        holder.imageViewTalk?.setHint(mList[position].replies.size.toString())

        for (account : ItemAccount in mList[position].likeAccounts){
            if (account.userId == mUserId) {
                holder.imageViewLike?.setSelect(true)
                holder.imageViewLike?.setHint((mList[position].likeAccounts.size - 1).toString())
                break
            } else {
                holder.imageViewLike?.setHint(mList[position].likeAccounts.size.toString())
            }
        }


        holder.imageViewUsrPic?.setOnClickListener {
            val navController = Navigation.findNavController(holder.itemView)
            val bundle = Bundle()
            bundle.putBoolean("isMine", false)
            bundle.putString("userId", mList[position].userId)
            navController.navigate(R.id.action_global_fragmentMine, bundle)
        }
        holder.imageViewTalk?.setOnClickListener {
            val navController = Navigation.findNavController(holder.itemView)
            val bundle = Bundle()
            bundle.putLong("talkId",mList[position].time)
            navController.navigate(R.id.action_global_fragmentTalkDetails,bundle)

        }
        holder.imageViewLike?.setOnClickListener {

        }
        holder.itemView.isClickable = true
        holder.itemView.setOnClickListener {
            val navController = Navigation.findNavController(holder.itemView)
            val bundle = Bundle()
            bundle.putLong("talkId",mList[position].time)
            navController.navigate(R.id.action_global_fragmentTalkDetails,bundle)
        }



        val likeListener = View.OnClickListener {
            holder.imageViewLike?.let {
                it.setSelect(!it.getSelect())
            }


        }
        holder.imageViewLike?.setOnClickListener(likeListener)


    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewUsrName: TextView? = itemView.findViewById<TextView>(R.id.textViewUsrName)
        val textViewTime: TextView? = itemView.findViewById<TextView>(R.id.textViewTime)
        val textViewContent: TextView? = itemView.findViewById<TextView>(R.id.textViewContent)
        val imageViewUsrPic: ImageView? = itemView.findViewById<ImageView>(R.id.imageViewUsrPic)
        val imageViewPic: ImageView? = itemView.findViewById<ImageView>(R.id.imageViewPic)
        val imageViewSex: ImageView? = itemView.findViewById<ImageView>(R.id.imageViewSex)
        val buttonFollow: Button? = itemView.findViewById<Button>(R.id.buttonFollow)
        val imageViewLike: ImageViewInfoZLike? = itemView.findViewById<com.example.moneycounter4.view.costom.ImageViewInfoZLike>(R.id.imageViewLike)
        val imageViewTalk: ImageViewInfoZ? = itemView.findViewById<ImageViewInfoZ>(R.id.imageViewTalk)
        val textViewMore: TextView? = itemView.findViewById<TextView>(R.id.textViewMore)

    }

}