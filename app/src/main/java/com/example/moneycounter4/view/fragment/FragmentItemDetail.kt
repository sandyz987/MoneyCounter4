package com.example.moneycounter4.view.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moneycounter4.R
import com.example.moneycounter4.bean.DataItem
import com.example.moneycounter4.model.TypeFinder
import com.example.moneycounter4.utils.TimeUtil
import com.example.moneycounter4.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_item_detail.*

class FragmentItemDetail : Fragment() {

    lateinit var viewModel : MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        return inflater.inflate(R.layout.fragment_item_detail, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dataItem = arguments?.get("dataItem") as DataItem

        textViewTime.text = TimeUtil.dayStr(dataItem.time)
        textViewMoney.text = String.format("%.2f", kotlin.math.abs(dataItem.money))
        textViewExpend.text = if(dataItem.money<0)"支出" else "收入"
        textViewType.text = dataItem.type
        textViewTips.text = if(dataItem.tips.isEmpty())"无" else dataItem.tips
        imageViewTypeImage.setImageResource(TypeFinder.findTypePicIdByName(viewModel,dataItem.type))
        constraintDel.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("真的要删除嘛~")
                .setNegativeButton("手滑了", null)
            builder.setPositiveButton("是的"
            ) { _, _ ->
                viewModel.delItemByTime(dataItem.time)
                close()
            }.show()

        }
        textViewFinish.setOnClickListener {
            close()
        }
    }

    fun close(){
        val navController = findNavController()
        navController.popBackStack()
    }

}