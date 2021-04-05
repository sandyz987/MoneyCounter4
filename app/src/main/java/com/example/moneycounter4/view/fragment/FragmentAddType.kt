package com.example.moneycounter4.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseFragment
import com.example.moneycounter4.bean.TypeItem
import com.example.moneycounter4.databinding.FragmentTypeBinding
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.model.TypeIndex
import com.example.moneycounter4.view.adapter.TypeRecyclerViewAdapter
import com.example.moneycounter4.view.costom.EditTextZ
import com.example.moneycounter4.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_type.*


//conf表明显示支出列表还是收入列表

class FragmentAddType : BaseFragment() {

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
        recyclerViewType.layoutManager = GridLayoutManager(requireContext(),4)
        val list = ObservableArrayList<TypeItem>()
        TypeIndex.getAllType().forEach { list.add(it) }
        val adapter = TypeRecyclerViewAdapter(requireActivity(),requireContext(),list,0)
        recyclerViewType.adapter = adapter


        adapter.setOnClick { t ->

            val dialog = BottomSheetDialog(requireContext())
            val view = requireActivity().layoutInflater.inflate(
                R.layout.dialog_bottom_sheet_add_type,
                null
            )
            dialog.setContentView(view)
            val tabLayout = view.findViewById<TabLayout>(R.id.tabLayoutAdd)
            dialog.show()
            view.findViewById<TextView>(R.id.textViewOk).setOnClickListener {

                val a = tabLayout.selectedTabPosition
                val picResId = t.resId
                val typeName = view.findViewById<EditTextZ>(R.id.editTextZAdd).text.toString()
                if (typeName.isEmpty() || picResId == 0) {
                    Toast.makeText(requireContext(), "是不是少了点什么鸭", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                when (a) {
                    0 -> viewModel.addType(TypeItem(typeName, picResId), DataReader.OPTION_OUT)
                    1 -> viewModel.addType(TypeItem(typeName, picResId), DataReader.OPTION_IN)
                }
                findNavController().navigate(R.id.action_fragmentAddType_pop)
                dialog.hide()
                Toast.makeText(requireContext(), "已添加！", Toast.LENGTH_SHORT).show()

            }
        }
    }

}