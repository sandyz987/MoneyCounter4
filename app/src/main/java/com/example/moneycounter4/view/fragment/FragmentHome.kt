package com.example.moneycounter4.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.baoyz.widget.PullRefreshLayout
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.databinding.FragmentMyHomeBinding
import com.example.moneycounter4.view.adapter.LogRecyclerViewAdapter
import com.example.moneycounter4.viewmodel.GlobalViewModel
import com.example.moneycounter4.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_my_home.*


class FragmentHome : BaseViewModelFragment<HomeViewModel>() {

    private lateinit var fragmentHomeBinding: FragmentMyHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        fragmentHomeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.fragment_my_home,
            null,
            false
        )

        fragmentHomeBinding.lifecycleOwner = this
        return fragmentHomeBinding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentHomeBinding.vm = viewModel

        viewModel.refreshList()

        val adapter = LogRecyclerViewAdapter(
            this,
            ViewModelProviders.of(requireActivity()).get(GlobalViewModel::class.java),
            viewModel.list,
            recyclerViewLog
        )
        imageViewMonth.setOnClickListener {
            val pvTime =
                TimePickerBuilder(
                    requireContext()
                ) { date, _ ->
                    viewModel.month.set(date.month + 1)
                    viewModel.year.set(date.year + 1900)
                    viewModel.refreshList()
                    adapter.notifyDataSetChanged()
                }
                    .setType(booleanArrayOf(true, true, false, false, false, false))
                    .build()
            pvTime.show()
        }



        recyclerViewLog.adapter = adapter
        recyclerViewLog.layoutManager = LinearLayoutManager(requireContext())
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP)
        pullRefreshLayout.setOnRefreshListener {
            viewModel.refreshList()
            adapter.notifyDataSetChanged()
            pullRefreshLayout.setRefreshing(false)
        }


    }

}