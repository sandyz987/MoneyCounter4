package com.example.moneycounter4.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baoyz.widget.PullRefreshLayout
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseFragment
import com.example.moneycounter4.databinding.FragmentMyHomeBinding
import com.example.moneycounter4.view.adapter.LogRecyclerViewAdapter
import com.example.moneycounter4.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_my_home.*


class FragmentHome : BaseFragment() {

    lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val fragmentHomeBinding = DataBindingUtil.inflate<FragmentMyHomeBinding>(
            LayoutInflater.from(requireContext()),
            R.layout.fragment_my_home,
            null,
            false
        )
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        fragmentHomeBinding.vm = viewModel

        fragmentHomeBinding.lifecycleOwner = this
        return fragmentHomeBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = LogRecyclerViewAdapter(
            requireActivity(),
            viewModel,
            requireContext(),
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
            adapter.notifyDataSetChanged()
            pullRefreshLayout.setRefreshing(false)
        }


    }

}