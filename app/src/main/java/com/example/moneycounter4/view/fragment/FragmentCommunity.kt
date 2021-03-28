package com.example.moneycounter4.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.beannew.ReplyInfo
import com.example.moneycounter4.view.adapter.TalkRecyclerViewAdapter
import com.example.moneycounter4.viewmodel.CommunityViewModel
import kotlinx.android.synthetic.main.fragment_community.*


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
            viewModel.refreshDynamic()
        }

        drag_head_view.onRefreshAction = listener
        listener.invoke()

        sendDynamic.setOnClickListener {
            viewModel.replyInfo.value = ReplyInfo("", "", -1, -1)
            FragmentTalkEdit.viewModel = viewModel
            val navController = findNavController()
            navController.navigate(R.id.action_global_fragmentTalkEdit)
        }




        viewModel.dynamicList.observeNotNull {
            adapter.setList(it)
            drag_head_view?.finishRefresh()
        }


    }


}