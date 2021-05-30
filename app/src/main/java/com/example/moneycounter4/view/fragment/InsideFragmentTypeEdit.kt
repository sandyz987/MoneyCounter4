package com.example.moneycounter4.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.databinding.FragmentTypeBinding
import com.example.moneycounter4.view.adapter.TypeEditRecyclerViewAdapter
import com.example.moneycounter4.viewmodel.MainViewModel
import com.example.moneycounter4.widgets.ItemTouchLinearCallback
import kotlinx.android.synthetic.main.fragment_type.*

//conf表明显示支出列表还是收入列表

class InsideFragmentTypeEdit(private val conf: Int) : BaseViewModelFragment<MainViewModel>() {
    override fun useActivityViewModel() = true

    companion object {
        const val CONF_IN = 0//表明这个fragment显示收入类型列表
        const val CONF_OUT = 1//支出类型
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentTypeBinding = DataBindingUtil.inflate<FragmentTypeBinding>(
            LayoutInflater.from(requireContext()),
            R.layout.fragment_type,
            null,
            false
        )
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        fragmentTypeBinding.lifecycleOwner = this
        return fragmentTypeBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerViewType.layoutManager = LinearLayoutManager(requireContext())
        val adapter = when (conf) {
            CONF_IN -> {
                TypeEditRecyclerViewAdapter(
                    requireActivity(),
                    viewModel,
                    viewModel.typeListIn
                )
            }
            CONF_OUT -> {
                TypeEditRecyclerViewAdapter(
                    requireActivity(),
                    viewModel,
                    viewModel.typeListOut
                )
            }
            else -> {
                TypeEditRecyclerViewAdapter(
                    requireActivity(),
                    viewModel,
                    viewModel.typeListOut
                )
            }
        }


        recyclerViewType.adapter = adapter

        val ihCallback =
            ItemTouchLinearCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(ihCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewType)

    }

}