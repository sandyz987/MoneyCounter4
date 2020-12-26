package com.example.moneycounter4.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.moneycounter4.R
import com.example.moneycounter4.bean.ItemAccount
import com.example.moneycounter4.bean.TalkItem
import com.example.moneycounter4.utils.HttpUtilCallback
import com.example.moneycounter4.utils.HttpUtils.HttpUtil
import com.example.moneycounter4.utils.TimeUtil
import com.example.moneycounter4.view.adapter.LikeRecyclerViewAdapter
import com.example.moneycounter4.view.adapter.TalkRecyclerViewAdapter
import com.example.moneycounter4.view.costom.ImageViewInfoZLike
import com.example.moneycounter4.viewmodel.MainApplication
import com.example.moneycounter4.viewmodel.MainViewModel
import com.example.moneycounter4.widgets.ProgressDialogW
import com.google.gson.Gson
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.fragment_talk_details.*
import kotlinx.android.synthetic.main.item_talk_big.*

class FragmentTalkDetails : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textViewMore.visibility = View.GONE
        val talkId = arguments?.getLong("talkId")

        val viewModel : MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)



        recyclerViewLikeAccount.layoutManager = LinearLayoutManager(requireContext())
        (recyclerViewLikeAccount.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL






        var listener : SwipeRefreshLayout.OnRefreshListener? = null
        talkId?.let {
            listener = SwipeRefreshLayout.OnRefreshListener {
                HttpUtil.getInstance().httpGet((activity?.application as MainApplication).connectionUrlMain,object :
                    HttpUtilCallback {
                    override fun doSomething(respond: String?) {
                        var talkItem :TalkItem? = null
                        try {
                            val gson = Gson()
                            talkItem = gson.fromJson(respond,TalkItem::class.javaObjectType)
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                        activity?.runOnUiThread {
                            talkItem?.let {
                                val includeView = view?.findViewById<View>(R.id.include)
                                includeView?.findViewById<TextView>(R.id.textViewUsrName)?.text = talkItem.usrName
                                includeView?.findViewById<TextView>(R.id.textViewTime)?.text = TimeUtil.getChatTimeStr(talkItem.time)
                                includeView?.findViewById<TextView>(R.id.textViewContent)?.text = talkItem.text
                                includeView?.findViewById<ImageViewInfoZLike>(R.id.imageViewLike)?.setHint(talkItem.likeAccounts.size.toString())
                                if(it.accountNum == viewModel.accountNum.get()){
                                    textViewDelTalk?.visibility = View.VISIBLE
                                }else{
                                    textViewDelTalk?.visibility = View.GONE
                                }
                                val likeAdapter = LikeRecyclerViewAdapter(activity,requireContext(),talkItem.likeAccounts)
                                recyclerViewLikeAccount?.adapter = likeAdapter

                                if(talkItem.likeAccounts.size == 0){
                                    recyclerViewLikeAccount?.visibility = View.GONE
                                    imageViewLikeView?.visibility = View.GONE
                                }else{
                                    recyclerViewLikeAccount?.visibility = View.VISIBLE
                                    imageViewLikeView?.visibility = View.VISIBLE
                                }

                                when(talkItem.sex){
                                    "男"->includeView?.findViewById<ImageView>(R.id.imageViewSex)?.setImageResource(R.drawable.ic_man)
                                    "女"->includeView?.findViewById<ImageView>(R.id.imageViewSex)?.setImageResource(R.drawable.ic_woman)
                                    else->includeView?.findViewById<ImageView>(R.id.imageViewSex)?.setImageBitmap(null)
                                }
                                for (account : ItemAccount in talkItem.likeAccounts){
                                    if (account.accountNum == viewModel.accountNum.get()){
                                        imageViewLike?.setSelect(true)
                                        imageViewLike?.setHint((talkItem.likeAccounts.size-1).toString())
                                        break
                                    }else{
                                        imageViewLike?.setHint(talkItem.likeAccounts.size.toString())
                                    }
                                }
                                textViewRepliesCount?.text = "评论详细（${talkItem.replies.size}）"
                                includeView?.findViewById<RoundedImageView>(R.id.imageViewUsrPic)?.let { context?.let { it1 ->
                                    Glide.with(
                                        it1
                                    ).load(talkItem.usrPic).into(it)
                                } }
                                //ImageLoader.with(context).load(talkItem.usrPic).into(includeView?.findViewById<ImageViewCorner>(R.id.imageViewUsrPic))
                                if(talkItem.picUrl!=null &&talkItem.picUrl!="null"&&talkItem.picUrl!=""){
                                    includeView?.findViewById<RoundedImageView>(R.id.imageViewPic)?.let { context?.let { it1 ->
                                        Glide.with(
                                            it1
                                        ).load(talkItem.picUrl).into(it)
                                    } }
                                    //ImageLoader.with(context).load(talkItem.picUrl)?.into(includeView?.findViewById<ImageViewCorner>(R.id.imageViewPic))
                                    includeView?.findViewById<RoundedImageView>(R.id.imageViewPic)?.visibility = View.VISIBLE
                                }
                                includeView?.findViewById<RoundedImageView>(R.id.imageViewUsrPic)?.setOnClickListener {
                                    val navController = findNavController()
                                    val bundle = Bundle()
                                    bundle.putString("accountNum",talkItem.accountNum)
                                    bundle.putBoolean("isMain",false)
                                    navController.navigate(R.id.action_global_fragmentMine,bundle)
                                }

                                imageViewTalk?.setOnClickListener {
                                    val bundle = Bundle()
                                    bundle.putLong("talkId",talkId)
                                    bundle.putString("usrName",talkItem.usrName+" ("+ talkItem.accountNum+")")
                                    val navController = findNavController()
                                    navController.navigate(R.id.action_global_fragmentTalkEdit,bundle)
                                }



                                val adapter=
                                    context?.let { it1 -> TalkRecyclerViewAdapter(
                                        activity,
                                        it1,
                                        talkItem.replies,
                                        (activity?.application as MainApplication).connectionUrlMain,
                                        viewModel.accountNum.get(),
                                        viewModel.token
                                    ) }

                                recyclerViewTalkReplies?.adapter = adapter
                                recyclerViewTalkReplies?.layoutManager = LinearLayoutManager(context)
                                if(recyclerViewTalkReplies.itemDecorationCount == 0){
                                    recyclerViewTalkReplies.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                                }

                            }
                            swipeRefreshLayout?.isRefreshing = false
                        }

                    }
                    override fun error() {
                        activity?.runOnUiThread {
                            Toast.makeText(activity,"加载失败，请检查网络连接呀", Toast.LENGTH_SHORT).show()
                            swipeRefreshLayout?.isRefreshing = false
                        }

                    }
                },activity,"page","test","post","findtalk","id",talkId.toString())
            }
            swipeRefreshLayout.setOnRefreshListener(listener)
            swipeRefreshLayout.isRefreshing = true
            listener?.onRefresh()
        }



        textViewDelTalk.setOnClickListener {


            val builder =
                AlertDialog.Builder(requireContext())
            builder.setTitle("真的要删除嘛？o(╥﹏╥)o").setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton("不是", null)
            builder.setPositiveButton(
                "是"
            ) { _, _ ->
                ProgressDialogW.show(requireContext(),"提示","正在删除",false)
                HttpUtil.getInstance().httpGet((activity?.application as MainApplication).connectionUrlMain,object :
                    HttpUtilCallback {
                    override fun doSomething(respond: String?) {
                        activity?.runOnUiThread {
                            when(respond){
                                "1"->{
                                    Toast.makeText(requireContext(),"已删除~",Toast.LENGTH_SHORT).show()
                                    val navController = findNavController()
                                    navController.popBackStack()
                                }
                                else->Toast.makeText(activity,"删除失败，请求错误", Toast.LENGTH_SHORT).show()
                            }
                            ProgressDialogW.hide()
                        }
                    }
                    override fun error() {
                        activity?.runOnUiThread {
                            Toast.makeText(activity,"删除失败，请检查网络连接呀", Toast.LENGTH_SHORT).show()
                            ProgressDialogW.hide()
                        }

                    }
                },activity,"page","test","post","deltalk","token",viewModel.token.toString(),"id",talkId.toString())

            }.show()


        }



        val likeListener = View.OnClickListener {
            swipeRefreshLayout?.isRefreshing = true
            if (imageViewLike.getSelect()){
                //取消点赞
                HttpUtil.getInstance().httpGet((activity?.application as MainApplication).connectionUrlMain,object :
                    HttpUtilCallback {
                    override fun doSomething(respond: String?) {
                        activity?.runOnUiThread {
                            when(respond){
                                "1"->{
                                    imageViewLike.setSelect(false)
                                    listener?.onRefresh()
                                }
                                else->Toast.makeText(activity,"取消点赞失败，请求错误", Toast.LENGTH_SHORT).show()
                            }
                            swipeRefreshLayout?.isRefreshing = false
                        }
                    }
                    override fun error() {
                        activity?.runOnUiThread {
                            Toast.makeText(activity,"取消点赞失败，请检查网络连接呀", Toast.LENGTH_SHORT).show()
                            swipeRefreshLayout?.isRefreshing = false
                        }

                    }
                },activity,"page","test","post","cancellike","token",viewModel.token.toString(),"talkid",talkId.toString())

            }else{
                //点赞
                HttpUtil.getInstance().httpGet((activity?.application as MainApplication).connectionUrlMain,object :
                    HttpUtilCallback {
                    override fun doSomething(respond: String?) {
                        activity?.runOnUiThread {
                            when(respond){
                                "1"->{
                                    imageViewLike.setSelect(true)
                                    listener?.onRefresh()
                                }
                                else->Toast.makeText(activity,"点赞失败，请求错误", Toast.LENGTH_SHORT).show()
                            }
                            swipeRefreshLayout?.isRefreshing = false
                        }
                    }
                    override fun error() {
                        activity?.runOnUiThread {
                            Toast.makeText(activity,"点赞失败，请检查网络连接呀", Toast.LENGTH_SHORT).show()
                            swipeRefreshLayout?.isRefreshing = false
                        }

                    }
                },activity,"page","test","post","like","token",viewModel.token.toString(),"talkid",talkId.toString())
            }
        }

        imageViewLike.setOnClickListener(likeListener)

        imageViewTalk.setHint("回复")


    }




}