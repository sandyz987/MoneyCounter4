package com.example.moneycounter4.view.activity

import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.widget.Toast
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelActivity
import com.example.moneycounter4.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class ActivityRegister : BaseViewModelActivity<LoginViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        window.enterTransition = Slide(Gravity.RIGHT)


        floatRegisterBack.setOnClickListener {
            onBackPressed()
        }


        floatRegisterRegister.setOnClickListener {

            if (edittextRegisterUserId.text.toString().length <= 3 || edittextRegisterPassword.text.toString().length <= 3) {
                Toast.makeText(this, "账号和密码的长度应大于3~", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(
                edittextRegisterUserId.text.toString(),
                edittextRegisterPassword.text.toString()
            ) {
//                startActivity(Intent(this, ActivityMain::class.java))
//                finish()
                onBackPressed()
            }

        }

    }
}