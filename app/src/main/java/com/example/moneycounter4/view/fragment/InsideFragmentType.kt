package com.example.moneycounter4.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseFragment
import com.example.moneycounter4.bean.TypeItem
import com.example.moneycounter4.databinding.FragmentTypeBinding
import com.example.moneycounter4.view.adapter.TypeRecyclerViewAdapter
import com.example.moneycounter4.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_type.*

//conf表明显示支出列表还是收入列表

class InsideFragmentType : BaseFragment() {

    companion object {
        const val CONF_IN = 0//表明这个fragment显示收入类型列表
        const val CONF_OUT = 1//支出类型
    }

    lateinit var adapter: TypeRecyclerViewAdapter
    lateinit var onClickAction: (TypeItem) -> Unit

    lateinit var viewModel: MainViewModel
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
        fragmentTypeBinding.vm = viewModel
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
                    requireContext(),
                    viewModel.typeListIn,
                    1
                )
            }
            CONF_OUT -> {
                TypeRecyclerViewAdapter(
                    requireActivity(),
                    requireContext(),
                    viewModel.typeListOut,
                    1
                )
            }
            else -> {
                TypeRecyclerViewAdapter(
                    requireActivity(),
                    requireContext(),
                    viewModel.typeListOut,
                    1
                )
            }
        }
        val list = when(conf) {
            CONF_IN -> {
                viewModel.typeListIn
            }
            CONF_OUT -> {
                viewModel.typeListOut
            }
            else -> {
                viewModel.typeListIn
            }
        }

        recyclerViewType.adapter = adapter
        adapter.setOnClick(onClickAction)

        list.addOnListChangedCallback(object :
            ObservableList.OnListChangedCallback<ObservableArrayList<TypeItem>>() {
            override fun onChanged(sender: ObservableArrayList<TypeItem>?) {
                adapter.setList(sender)
                adapter.notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(
                sender: ObservableArrayList<TypeItem>?,
                positionStart: Int,
                itemCount: Int
            ) {
                adapter.setList(sender)
                adapter.notifyDataSetChanged();
            }

            override fun onItemRangeMoved(
                sender: ObservableArrayList<TypeItem>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {
                adapter.setList(sender)
                adapter.notifyDataSetChanged();
            }

            override fun onItemRangeInserted(
                sender: ObservableArrayList<TypeItem>?,
                positionStart: Int,
                itemCount: Int
            ) {
                adapter.setList(sender)
                adapter.notifyDataSetChanged()
            }

            override fun onItemRangeChanged(
                sender: ObservableArrayList<TypeItem>?,
                positionStart: Int,
                itemCount: Int
            ) {
                adapter.setList(sender)
                adapter.notifyDataSetChanged()
            }
        })
    }

    fun setInnerOnClickAction(t: (TypeItem) -> Unit) {
        onClickAction = t
    }

}