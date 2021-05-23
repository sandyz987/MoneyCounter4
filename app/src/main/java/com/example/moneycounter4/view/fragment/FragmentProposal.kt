package com.example.moneycounter4.view.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseFragment
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.model.dao.getByDuration
import com.example.moneycounter4.network.safeSubscribeBy
import com.example.moneycounter4.network.setSchedulers
import com.example.moneycounter4.utils.CalendarUtil
import com.example.moneycounter4.utils.CounterAnalyze
import com.example.moneycounter4.utils.toDateItem
import com.example.moneycounter4.view.adapter.ProposalAdapter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_proposal.*
import java.util.*

class FragmentProposal : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_proposal, container, false)
    }


    val adapter = ProposalAdapter()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        ObjectAnimator.ofInt(0, 100).setDuration(2000).apply {
            addUpdateListener {
                counter_pb_proposal.progressInt = it.animatedValue as Int
                if (it.animatedValue as Int == 100) {
                    refresh()
                }
            }
            start()
        }
        rv_proposal.layoutManager = LinearLayoutManager(requireContext())
        rv_proposal.adapter = adapter

    }

    private fun refresh() {
        counter_pb_proposal.visibility = View.GONE
        textView.visibility = View.GONE

        val o = Observable.create<List<String>> {
            it.onNext(getProposal())
        }.setSchedulers()
            .safeSubscribeBy {
                adapter.setList(it)
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