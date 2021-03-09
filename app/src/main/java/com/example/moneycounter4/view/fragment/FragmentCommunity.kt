package com.example.moneycounter4.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.baoyz.widget.PullRefreshLayout
import com.bumptech.glide.Glide
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.bean.TalkItem
import com.example.moneycounter4.beannew.DynamicItem
import com.example.moneycounter4.utils.TimeUtil
import com.example.moneycounter4.view.adapter.TalkRecyclerViewAdapter
import com.example.moneycounter4.viewmodel.CommunityViewModel
import com.example.moneycounter4.viewmodel.MainApplication
import com.example.moneycounter4.viewmodel.MainViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_community.*
import kotlinx.android.synthetic.main.fragment_community.view.*
import kotlinx.android.synthetic.main.item_talk_big.*


class FragmentCommunity : BaseViewModelFragment<CommunityViewModel>() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    lateinit var adapter: TalkRecyclerViewAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




        recyclerViewTalkList.layoutManager = LinearLayoutManager(context)
        if (recyclerViewTalkList.itemDecorationCount == 0) {
            recyclerViewTalkList.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }


        // 设置点击事件
        adapter = TalkRecyclerViewAdapter(requireContext()) { dynamic, v ->
            FragmentTalkDetail.viewModel = viewModel
            Navigation.findNavController(v)
                .navigate(R.id.action_global_fragmentTalkDetails, Bundle().apply {
                    putInt("dynamic_id", dynamic.dynamicId)
                })
        }


        recyclerViewTalkList.adapter = adapter


        val listener = {
            viewModel.getAllDynamic(0, 50, "广场")
        }

        drag_head_view.onRefreshAction = listener
        drag_head_view.refresh()

        sendDynamic.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_global_fragmentTalkEdit)
        }




        viewModel.dynamicList.observeNotNull {
            adapter.setList(it)
            drag_head_view?.finishRefresh()
        }


    }


}