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
import androidx.lifecycle.MutableLiveData
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.example.calculatorjni.jni.Calculator
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.utils.TimeUtil
import com.example.moneycounter4.viewmodel.MoneyEditViewModel
import com.example.moneycounter4.widgets.InputDialog
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.fragment_money_input.*

class InsideFragmentMoneyInput : BaseViewModelFragment<MoneyEditViewModel>(), View.OnClickListener {

    lateinit var listener: View.OnClickListener
    private var time: Long? = null

    private var tip: String = ""

    private val inputMoney = MutableLiveData<String>("")

    override fun useActivityViewModel() = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_money_input, container, false)

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val calendar = Calendar.getInstance()
        time = calendar.timeInMillis

        textViewDate.textSize = 30f

        inputMoney.observeNotNull {
            textViewMoneyNum.text = "￥$it"
        }

        tv_tip.setOnClickListener {
            InputDialog(requireContext(), "请输入备注", tip) {
                tip = it
                tv_tip.text = tip
            }.show()
        }


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
                TimePickerBuilder(
                    requireContext()
                ) { date, _ ->
                    val calendar1 = Calendar.getInstance()
                    val calendar2 = java.util.Calendar.getInstance()
                    calendar1.time = date
                    time = calendar1.timeInMillis
                    textViewDate.text = TimeUtil.monthStr(time!!)
                    if (TimeUtil.monthStr(calendar1.timeInMillis) == TimeUtil.monthStr(calendar2.timeInMillis)) {
                        textViewDate.text = "今天"
                        textViewDate.textSize = 30f
                    } else {
                        textViewDate.text = TimeUtil.monthStr(time!!)
                        textViewDate.textSize = 15f
                    }
                }
                    .setType(booleanArrayOf(true, true, true, true, true, false))
                    .isDialog(true)
                    .build()
            pvTime.show()
        }
        textViewDel.setOnClickListener {
            del()
        }

        listener = View.OnClickListener {
            if (getValue(inputMoney.value!!).equals(0.0)) {
                Toast.makeText(requireContext(), "请输入非零数值哦~", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            viewModel.willBeAddedItem.value = CounterDataItem(
                time = time ?: 0L,
                tips = tv_tip.text.toString(),
                money = (inputMoney.value ?: "0").toDouble()
            )

        }

        textViewOk.text = "="
        textViewOk.setOnClickListener {
            inputMoney.value =
                String.format("%.2f", getValue(inputMoney.value!!))
            textViewOk.text = "完成"
            textViewOk.setOnClickListener(listener)
        }

    }

    @SuppressLint("SetTextI18n")
    fun addChar(s: String) {
        if (inputMoney.value!!.length >= 15) {
            return
        }
        inputMoney.value = inputMoney.value!! + s

        textViewOk.text = "="
        textViewOk.setOnClickListener {
            inputMoney.value =
                String.format("%.2f", getValue(inputMoney.value!!))
            textViewOk.text = "完成"
            textViewOk.setOnClickListener(listener)

        }
    }

    fun getValue(exp: String): Double {
        val c = Calculator()
        if (exp.isEmpty()) {
            c.setExpression("0")
        } else {
            c.setExpression(exp)
        }

        return c.getAns().also { c.destroy() }
    }

    private fun del() {
        if (inputMoney.value!!.isNotEmpty())
            inputMoney.value =
                inputMoney.value!!.take(inputMoney.value!!.length - 1)
    }

    override fun onClick(v: View?) {
        addChar((v as TextView).text.toString())
    }

}