package com.example.moneycounter4.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.moneycounter4.R
import com.example.moneycounter4.utils.HttpUtilCallback
import com.example.moneycounter4.utils.HttpUtils.HttpUtil
import com.example.moneycounter4.viewmodel.MainApplication
import com.example.moneycounter4.viewmodel.MainViewModel
import com.example.moneycounter4.widgets.ProgressDialogW
import kotlinx.android.synthetic.main.activity_login.*

@RequiresApi(Build.VERSION_CODES.N)
class ActivityLogin : AppCompatActivity() {

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        edittextLoginAccountnum.setText(intent?.getStringExtra("username")?: "")
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.enterTransition = Slide(Gravity.LEFT)


        val viewModel :MainViewModel by viewModels()

        viewModel.saveUsrInfo("","",-1)

        floatLoginLogin.setOnClickListener {
            ProgressDialogW.show(this,"提示","正在登录中",false)
            HttpUtil.getInstance().httpGet(
                (application as MainApplication).connectionUrlMain, object :
                    HttpUtilCallback {
                    override fun doSomething(respond: String?) {
                        ProgressDialogW.hide()
                        if (respond.equals("-1")) {
                            this@ActivityLogin.runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    R.string.login_denied_error,
                                    Toast.LENGTH_SHORT
                                ).show()
                                edittextLoginPassword.setText("")
                            }
                        } else {
                            this@ActivityLogin.runOnUiThread {
                                Toast.makeText(
                                    applicationContext, R.string.login_success,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            viewModel.saveUsrInfo(
                                edittextLoginAccountnum.text.toString(),
                                edittextLoginPassword.text.toString(),
                                respond?.toInt()
                            )
                            respond?.let { viewModel.token = it.toInt() }
                            val intent = Intent(this@ActivityLogin, ActivityMain::class.java)
                            startActivity(intent)
                            finish()
                        }

                    }

                    override fun error() {
                        ProgressDialogW.hide()
                        this@ActivityLogin.runOnUiThread {
                            Toast.makeText(
                                applicationContext, R.string.login_connection_error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                this,
                "action",
                "login",
                "accountnum",
                edittextLoginAccountnum.text.toString(),
                "password",
                edittextLoginPassword.text.toString()
            )


        }


        tvRegister.setOnClickListener {
            val intent = Intent(this,ActivityRegister::class.java)
            window.exitTransition = Slide(Gravity.LEFT)
            val option = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                Pair(edittextLoginAccountnum, "et1"),
                Pair(edittextLoginPassword, "et2"),
                Pair(floatLoginLogin, "ok"),
                Pair(logo, "logo")
            )
            startActivity(intent, option.toBundle())
        }
    }
}