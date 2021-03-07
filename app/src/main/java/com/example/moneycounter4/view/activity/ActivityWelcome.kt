package com.example.moneycounter4.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelActivity
import com.example.moneycounter4.extensions.toast
import com.example.moneycounter4.model.Config
import com.example.moneycounter4.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class ActivityWelcome : BaseViewModelActivity<LoginViewModel>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)


        val p = viewModel.getUserInfo()
        Config.userId = p.first
        Config.password = p.second


        Thread {
            Thread.sleep(1000)
            viewModel.checkLogin(this, {
                toast("登录成功！欢迎回来~")
                startMainActivity()
            }, {
                startLoginActivity()
            })
        }.start()


    }

    private fun startMainActivity() {
        this@ActivityWelcome.finish()
        val mainIntent = Intent(this@ActivityWelcome, ActivityMain::class.java)
        startActivity(mainIntent)
    }

    private fun startLoginActivity() {
        this@ActivityWelcome.finish()
        val mainIntent = Intent(this@ActivityWelcome, ActivityLogin::class.java)
        startActivity(mainIntent)

    }
}