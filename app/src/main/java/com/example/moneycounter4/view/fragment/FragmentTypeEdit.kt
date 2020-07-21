package com.example.moneycounter4.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moneycounter4.R
import com.example.moneycounter4.databinding.FragmentTypeEditBinding
import com.example.moneycounter4.view.adapter.TypeFragmentPagerAdapter
import com.example.moneycounter4.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_counter_edit.*

class FragmentTypeEdit : Fragment() {

    lateinit var adapter: TypeFragmentPagerAdapter
    lateinit var viewModel : MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val fragmentTypeEditBinding = DataBindingUtil.inflate<FragmentTypeEditBinding>(LayoutInflater.from(requireContext()),R.layout.fragment_type_edit,null,false)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        fragmentTypeEditBinding.vm = viewModel

        fragmentTypeEditBinding.lifecycleOwner = this
        return fragmentTypeEditBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(InsideFragmentTypeEdit(InsideFragmentType.CONF_OUT))
        fragmentList.add(InsideFragmentTypeEdit(InsideFragmentType.CONF_IN))
        adapter = TypeFragmentPagerAdapter(requireActivity().supportFragmentManager,0,fragmentList)
        viewPager.adapter = adapter
        viewPager.currentItem = arguments?.getInt("position",0)?:0
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)?.text = "支出"
        tabLayout.getTabAt(1)?.text = "收入"
        textViewCancel.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentTypeEdit_pop)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.removeAllFragment()
    }

}