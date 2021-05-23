package com.example.moneycounter4.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.model.Config
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.model.dao.getByDuration
import com.example.moneycounter4.network.safeSubscribeBy
import com.example.moneycounter4.network.setSchedulers
import com.example.moneycounter4.utils.CalendarUtil
import com.example.moneycounter4.utils.CounterAnalyze
import com.example.moneycounter4.utils.toDateItem
import com.example.moneycounter4.viewmodel.IndividualViewModel
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_individual.*
import java.util.*


class FragmentIndividual : BaseViewModelFragment<IndividualViewModel>() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_individual, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val isMine = arguments?.getBoolean("isMine")
        val userId = arguments?.getString("userId")

        isMine?.let {
            if (!isMine) {
                layoutCondition.visibility = View.GONE
                imageViewSetting.visibility = View.GONE
            } else {
                textViewTag.text = "我的主题"
            }
        }

        val listener = SwipeRefreshLayout.OnRefreshListener {


            if (isMine == true) {
                viewModel.getUser(Config.userId)
            } else {
                viewModel.getUser(userId ?: "")
            }

        }

        viewModel.user.observeNotNull { user ->
            textViewUsrName.text = user.nickname
            textViewUserId.text = user.userId
            if (user.text.isBlank()) {
                tv_text.text = "TA还没有写个性签名~"
            } else {
                tv_text.text = user.text
            }

            when (user.sex) {
                "男" -> imageViewSex.setImageResource(R.drawable.ic_man)
                "女" -> imageViewSex.setImageResource(R.drawable.ic_woman)
            }
            context?.let { Glide.with(it).load(user.avatarUrl).into(imageViewUsrPic) }
            swipeRefreshLayout.isRefreshing = false

        }

        swipeRefreshLayout.setOnRefreshListener(listener)
        listener.onRefresh()
        swipeRefreshLayout.isRefreshing = true

        imageViewSetting.setOnClickListener {
            findNavController().navigate(R.id.action_global_fragmentSetting)
        }
        val o = Observable.create<String> {
            val list = getProposal()
            if (list.isNotEmpty()) {
                it.onNext(list[0])
            }

        }
            .setSchedulers()
            .safeSubscribeBy {
                tv_proposal_mine.text = it
            }

        layoutCondition.setOnClickListener {
            findNavController().navigate(R.id.action_global_fragmentProposal)
        }


    }

    private fun getProposal(): List<String> {
        val dateItem = CalendarUtil.getCalendar().apply { add(Calendar.DATE, -90) }.toDateItem()
        DataReader.db?.userDao()?.getByDuration(dateItem, 0L, 93 * 86400000L, -1)?.let {
            return CounterAnalyze.getStringProposal(
                it
            )
        }
        return listOf()
    }


}