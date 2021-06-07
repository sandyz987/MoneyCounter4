package com.example.moneycounter4.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.bean.TypeItem
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.databinding.FragmentCounterEditBinding
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.view.adapter.TypeFragmentPagerAdapter
import com.example.moneycounter4.view.costom.BaseFullBottomSheetFragment
import com.example.moneycounter4.viewmodel.MoneyEditViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_input.*
import kotlinx.android.synthetic.main.fragment_counter_edit.*
import java.lang.ref.WeakReference

class FragmentCounterEdit : BaseViewModelFragment<MoneyEditViewModel>() {

    lateinit var adapter: TypeFragmentPagerAdapter
    var dialog = BaseFullBottomSheetFragment()
    var type: TypeItem? = null
    var float = 0f
    override fun useActivityViewModel() = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val fragmentCounterEditBinding = DataBindingUtil.inflate<FragmentCounterEditBinding>(
            LayoutInflater.from(requireContext()),
            R.layout.fragment_counter_edit,
            null,
            false
        )

        fragmentCounterEditBinding.lifecycleOwner = this
        return fragmentCounterEditBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //当子recyclerview点击种类按钮，则调用callback显示输入金额界面

        val onClickAction: ((TypeItem) -> Unit) = { t ->
            //LogW.d(t?.name?:"null")//=============================
            type = t
            if(!dialog.isVisible) {
                dialog = BaseFullBottomSheetFragment()

                dialog.show(requireActivity().supportFragmentManager, "")
                val weakReference = WeakReference(this@FragmentCounterEdit)
//            viewModel.handlerAddType =
//                SafeHandler(weakReference, object : SafeHandlerCallback<FragmentCounterEdit> {
//                    @RequiresApi(Build.VERSION_CODES.N)
//                    override fun doSomething(
//                        msg: Message,
//                        weakReference: WeakReference<FragmentCounterEdit>
//                    ) {
//                        val dataItem = CounterDataItem()
//                        dataItem.time = (msg.obj as TranData).time
//                        if (weakReference.get()?.tabLayout?.selectedTabPosition == 0) {
//                            dataItem.money = -(msg.obj as TranData).money
//                        } else {
//                            dataItem.money = (msg.obj as TranData).money
//                        }
//                        dataItem.type = weakReference.get()?.type?.name.toString()
//                        dataItem.tips = (msg.obj as TranData).tips
//                        weakReference.get()?.viewModel?.addItem(dataItem)
//                        Toast.makeText(requireContext(), "已添加", Toast.LENGTH_SHORT).show()
//                        weakReference.get()?.findNavController()
//                            ?.navigate(R.id.action_fragmentCounterEdit_pop)
//                        weakReference.get()?.dialog?.behavior?.state =
//                            BottomSheetBehavior.STATE_HIDDEN
//
//                    }
//                }).make()
                viewModel.willBeAddedItem.observeNotNull {
                    val dataItem = CounterDataItem()
                    dataItem.time = it.time
                    dataItem.accountBook = it.accountBook
                    if (tabLayout.selectedTabPosition == 0) {
                        dataItem.money = -(it.money ?: 0.0)
                    } else {
                        dataItem.money = it.money
                    }
                    dataItem.type = type?.name.toString()
                    dataItem.tips = it.tips
                    DataReader.addItem(dataItem)
                    Toast.makeText(requireContext(), "已添加", Toast.LENGTH_SHORT).show()
                    findNavController()
                        .navigate(R.id.action_fragmentCounterEdit_pop, Bundle().apply {
                            putBoolean("need_refresh", true)
                        })
                    dialog.behavior?.state =
                        BottomSheetBehavior.STATE_HIDDEN
                }
            }


        }


        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(InsideFragmentType().apply {
            arguments = Bundle().apply {
                putInt("conf", InsideFragmentType.CONF_OUT)
            }
            setInnerOnClickAction(onClickAction)
        })
        fragmentList.add(InsideFragmentType().apply {
            arguments = Bundle().apply {
                putInt("conf", InsideFragmentType.CONF_IN)
            }
            setInnerOnClickAction(onClickAction)
        })
        adapter =
            TypeFragmentPagerAdapter(requireActivity().supportFragmentManager, 0, fragmentList)
        viewPager.adapter = adapter
        viewPager.currentItem = 0
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)?.text = "支出"
        tabLayout.getTabAt(1)?.text = "收入"
        textViewCancel.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentCounterEdit_pop)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.removeAllFragment()
    }

}