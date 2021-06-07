package com.example.moneycounter4.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseFragment
import com.example.moneycounter4.databinding.FragmentBillBinding
import com.example.moneycounter4.model.BillItem
import com.example.moneycounter4.model.BillWalletSettingUtil
import com.example.moneycounter4.view.adapter.BillCreateAdapter
import com.example.moneycounter4.widgets.InputDialog
import kotlinx.android.synthetic.main.fragment_bill.*


class FragmentAddBill : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentTypeBinding = DataBindingUtil.inflate<FragmentBillBinding>(
            LayoutInflater.from(requireContext()),
            R.layout.fragment_bill,
            null,
            false
        )
        fragmentTypeBinding.lifecycleOwner = this
        return fragmentTypeBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_bill_create.adapter = BillCreateAdapter { id ->
            InputDialog(requireContext(), "账单名称", "") { name ->
                BillWalletSettingUtil.settingData?.billList?.add(BillItem(name, id))
                BillWalletSettingUtil.save()
                findNavController().navigate(R.id.action_fragmentAddBill_pop)
            }.show()
        }
        rv_bill_create.layoutManager = LinearLayoutManager(requireContext())


    }

}