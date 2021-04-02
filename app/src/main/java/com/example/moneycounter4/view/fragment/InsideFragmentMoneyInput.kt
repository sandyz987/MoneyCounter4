package com.example.moneycounter4.view.fragment

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.bean.TranData
import com.example.moneycounter4.utils.Calculater
import com.example.moneycounter4.utils.TimeUtil
import com.example.moneycounter4.viewmodel.MoneyEditViewModel
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.fragment_money_input.*

class InsideFragmentMoneyInput : BaseViewModelFragment<MoneyEditViewModel>(), View.OnClickListener {

    lateinit var listener: View.OnClickListener
    var time: Long? = null

    override fun useActivityViewModel() = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_money_input, container, false)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val calendar = Calendar.getInstance()
        time = calendar.timeInMillis

        textViewDate.textSize = 30f

        textViewAdd.setOnClickListener(this)
        textViewSub.setOnClickListener(this)
        textViewNum0.setOnClickListener(this)
        textViewNum1.setOnClickListener(this)
        textViewNum2.setOnClickListener(this)
        textViewNum3.setOnClickListener(this)
        textViewNum4.setOnClickListener(this)
        textViewNum5.setOnClickListener(this)
        textViewNum6.setOnClickListener(this)
        textViewNum7.setOnClickListener(this)
        textViewNum8.setOnClickListener(this)
        textViewNum9.setOnClickListener(this)
        textViewDot.setOnClickListener(this)
        textViewDate.setOnClickListener {
            val pvTime =
                TimePickerBuilder(requireContext(),
                    OnTimeSelectListener { date, _ ->
                        val calendar = Calendar.getInstance()
                        val calendar2 = java.util.Calendar.getInstance()
                        calendar.time = date
                        time = calendar.timeInMillis
                        textViewDate.text = TimeUtil.monthStr(time!!)
                        if(TimeUtil.monthStr(calendar.timeInMillis) == TimeUtil.monthStr(calendar2.timeInMillis)){
                            textViewDate.text = "今天"
                            textViewDate.textSize = 30f
                        }else{
                            textViewDate.text = TimeUtil.monthStr(time!!)
                            textViewDate.textSize = 15f
                        }
                    })
                    .setType(booleanArrayOf(true, true, true, true, true, false))
                    .isDialog(true)
                    .build()
            pvTime.show()
        }
        textViewDel.setOnClickListener {
            del()
        }

        listener = View.OnClickListener {
            if (Calculater.calculate(textViewMoneyNum.text.toString()).equals(0.0f)) {
                Toast.makeText(requireContext(), "请输入非零数值哦~", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            viewModel.tranData.value = TranData(
                time ?: 0L,
                editTextTip.text.toString(),
                textViewMoneyNum.text.toString().toDouble()
            )

        }

        textViewOk.text = "="
        textViewOk.setOnClickListener {
            textViewMoneyNum.text =
                Calculater.calculate(textViewMoneyNum.text.toString()).toString()
            textViewOk.text = "完成"
            textViewOk.setOnClickListener (listener)
        }

    }

    @SuppressLint("SetTextI18n")
    fun addChar(s: String) {
        if (textViewMoneyNum.text.toString().length >=7) {
            return
        }
        textViewMoneyNum.text = textViewMoneyNum.text.toString() + s

        textViewOk.text = "="
        textViewOk.setOnClickListener {
            textViewMoneyNum.text =
                Calculater.calculate(textViewMoneyNum.text.toString()).toString()
            textViewOk.text = "完成"
            textViewOk.setOnClickListener (listener)

        }
    }

    fun del() {
        if (textViewMoneyNum.text.toString().isNotEmpty())
            textViewMoneyNum.text =
                textViewMoneyNum.text.toString().take(textViewMoneyNum.text.toString().length - 1)
    }

    override fun onClick(v: View?) {
        addChar((v as TextView).text.toString())
    }

}