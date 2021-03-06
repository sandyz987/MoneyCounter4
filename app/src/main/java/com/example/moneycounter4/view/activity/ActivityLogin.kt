package com.example.moneycounter4.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelActivity
import com.example.moneycounter4.extensions.toast
import com.example.moneycounter4.model.Config
import com.example.moneycounter4.viewmodel.LoginViewModel
import com.example.moneycounter4.widgets.ProgressDialogW
import kotlinx.android.synthetic.main.activity_login.*

@RequiresApi(Build.VERSION_CODES.N)
class ActivityLogin : BaseViewModelActivity<LoginViewModel>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.enterTransition = Slide(Gravity.LEFT)


        floatLoginLogin.setOnClickListener {
            ProgressDialogW.show(this, "提示", "正在登录中", false)
            Config.userId = edittextLoginUserId.text.toString()
            Config.password = edittextLoginPassword.text.toString()
            viewModel.checkLogin(this, {
                ProgressDialogW.hide()
                toast("登录成功！欢迎回来！")
                val intent = Intent(this@ActivityLogin, ActivityMain::class.java)
                startActivity(intent)
            }, {
                ProgressDialogW.hide()
                toast("登录失败，请检查用户名或密码")
            })

        }


        tvRegister.setOnClickListener {
            val intent = Intent(this, ActivityRegister::class.java)
            window.exitTransition = Slide(Gravity.LEFT)
            val option = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(edittextLoginUserId, "et1"),
                Pair(edittextLoginPassword, "et2"),
                Pair(floatLoginLogin, "ok"),
                Pair(logo, "logo")
            )
            startActivity(intent, option.toBundle())
        }
    }
}