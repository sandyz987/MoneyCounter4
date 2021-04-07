package com.example.moneycounter4.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_welfare.*

class FragmentWelfare : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welfare, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_back.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentWelfare_pop)
        }

        tv_to.setOnClickListener {
            findNavController().navigate(R.id.action_global_fragmentEnergy)
        }
    }

}