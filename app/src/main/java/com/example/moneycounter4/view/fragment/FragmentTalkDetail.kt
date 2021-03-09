package com.example.moneycounter4.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseFragment
import com.example.moneycounter4.beannew.DynamicItem
import com.example.moneycounter4.utils.TimeUtil
import com.example.moneycounter4.view.adapter.TalkCommentRecyclerViewAdapter
import com.example.moneycounter4.viewmodel.CommunityViewModel
import kotlinx.android.synthetic.main.fragment_talk_detail.*
import kotlinx.android.synthetic.main.item_talk_big.*

class FragmentTalkDetail : BaseFragment() {
    
    private var dynamic: DynamicItem? = null

    lateinit var adapter: TalkCommentRecyclerViewAdapter

    companion object {
        var viewModel: CommunityViewModel? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textViewMore.visibility = View.GONE
        val dynamicId = arguments?.getInt("dynamic_id")
        viewModel?.dynamicList?.value?.forEach {
            if (it.dynamicId == dynamicId) {
                dynamic = it
            }
        }
        adapter = TalkCommentRecyclerViewAdapter(requireContext(), {_,_->}, {_,_->})

        rv_talk.layoutManager = LinearLayoutManager(context)
        rv_talk.adapter = adapter
        adapter.setList(dynamic?.commentList?: listOf())
        

        bindView()

        val listener = SwipeRefreshLayout.OnRefreshListener {
            viewModel?.getAllDynamic(0, 50, "广场")
        }
        swipeRefreshLayout.setOnRefreshListener(listener)
        listener.onRefresh()
        swipeRefreshLayout.isRefreshing = true

        viewModel?.dynamicList?.observe(viewLifecycleOwner, Observer {
            bindView()
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        })


    }

    fun bindView() {



        imageViewLike?.setSelect(false)
        imageViewLike?.setHint("0")

        textViewUsrName?.text = dynamic?.userId
        textViewContent?.text = dynamic?.text?.take(30)

        textViewContent?.text?.length?.let {
            if (it >= 30) {
                textViewContent.text = textViewContent.text.toString() + "..."
                textViewMore?.visibility = View.VISIBLE
            } else {
                textViewMore?.visibility = View.GONE
            }
        }

        textViewTime?.text = TimeUtil.getChatTimeStr(dynamic?.submitTime?: 0L)
        // TODO 图片显示
        textViewUsrName?.text = dynamic?.nickname
        imageViewUsrPic?.let { Glide.with(requireActivity()).load(dynamic?.avatarUrl).into(it) }



        imageViewTalk?.setHint(dynamic?.commentList?.size.toString())


        imageViewUsrPic?.setOnClickListener {
            val navController = Navigation.findNavController(it)
            val bundle = Bundle()
            bundle.putBoolean("isMine", false)
            bundle.putString("userId", dynamic?.userId)
            navController.navigate(R.id.action_global_fragmentMine, bundle)
        }

        imageViewLike?.setOnClickListener {
            // TODO 点赞
        }
        imageViewTalk?.setOnClickListener {
            val navController = Navigation.findNavController(it)
            val bundle = Bundle()
            bundle.putInt("dynamic_id", dynamic?.dynamicId?: 0)
            navController.navigate(R.id.action_global_fragmentTalkDetails, bundle)
        }



        val likeListener = View.OnClickListener {
            imageViewLike?.let {
                it.setSelect(!it.getSelect())
            }


        }
        imageViewLike?.setOnClickListener(likeListener)
    }




}