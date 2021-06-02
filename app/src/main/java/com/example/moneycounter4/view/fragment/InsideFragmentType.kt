package com.example.moneycounter4.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.bean.TypeItem
import com.example.moneycounter4.databinding.FragmentTypeBinding
import com.example.moneycounter4.view.adapter.TypeRecyclerViewAdapter
import com.example.moneycounter4.viewmodel.GlobalViewModel
import com.example.moneycounter4.widgets.ItemTouchGridCallback
import kotlinx.android.synthetic.main.fragment_type.*

//conf表明显示支出列表还是收入列表

class InsideFragmentType : BaseViewModelFragment<GlobalViewModel>() {

    companion object {
        const val CONF_IN = 0//表明这个fragment显示收入类型列表
        const val CONF_OUT = 1//支出类型
    }

    lateinit var adapter: TypeRecyclerViewAdapter
    lateinit var onClickAction: (TypeItem) -> Unit

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
        viewModel = ViewModelProvider(requireActivity()).get(GlobalViewModel::class.java)
        fragmentTypeBinding.lifecycleOwner = this
        return fragmentTypeBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val conf = arguments?.get("conf") ?: CONF_OUT

        recyclerViewType.layoutManager = GridLayoutManager(requireContext(), 4)
        adapter = when (conf) {
            CONF_IN -> {
                TypeRecyclerViewAdapter(
                    requireActivity(),
                    viewModel,
                    viewModel.typeListIn,
                    1
                )
            }
            CONF_OUT -> {
                TypeRecyclerViewAdapter(
                    requireActivity(),
                    viewModel,
                    viewModel.typeListOut,
                    1
                )
            }
            else -> {
                TypeRecyclerViewAdapter(
                    requireActivity(),
                    viewModel,
                    viewModel.typeListOut,
                    1
                )
            }
        }

        val ihCallback =
            ItemTouchGridCallback(adapter)

        val itemTouchHelper = ItemTouchHelper(ihCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewType)

        recyclerViewType.adapter = adapter
        adapter.setOnClick(onClickAction)

    }

    fun setInnerOnClickAction(t: (TypeItem) -> Unit) {
        onClickAction = t
    }

}