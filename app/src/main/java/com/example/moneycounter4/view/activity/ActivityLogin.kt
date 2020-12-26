package com.example.moneycounter4.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.moneycounter4.R
import com.example.moneycounter4.utils.HttpUtilCallback
import com.example.moneycounter4.utils.HttpUtils.HttpUtil
import com.example.moneycounter4.viewmodel.MainApplication
import com.example.moneycounter4.viewmodel.MainViewModel
import com.example.moneycounter4.widgets.ProgressDialogW
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.imageView

class ActivityLogin : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        imageView.setColorFilter(Color.BLACK)

        val viewModel :MainViewModel by viewModels()

        viewModel.saveUsrInfo("","",-1)

        floatLoginLogin.setOnClickListener {
            ProgressDialogW.show(this,"提示","正在登录中",false)
            HttpUtil.getInstance().httpGet((application as MainApplication).connectionUrlMain,object :
                HttpUtilCallback {
                override fun doSomething(respond: String?) {
                    ProgressDialogW.hide()
                    if(respond.equals("-1")){
                        this@ActivityLogin.runOnUiThread { Toast.makeText(applicationContext,R.string.login_denied_error,
                            Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ActivityLogin,ActivityMain::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }else{
                        this@ActivityLogin.runOnUiThread { Toast.makeText(applicationContext,R.string.login_success,
                            Toast.LENGTH_SHORT).show() }
                        viewModel.saveUsrInfo(edittextLoginAccountnum.text.toString(),edittextLoginPassword.text.toString(),respond?.toInt())
                        respond?.let { viewModel.token = it.toInt() }
                        val intent = Intent(this@ActivityLogin,ActivityMain::class.java)
                        startActivity(intent)
                        finish()
                    }

                }

                override fun error() {
                    ProgressDialogW.hide()
                    this@ActivityLogin.runOnUiThread { Toast.makeText(applicationContext,R.string.login_connection_error,
                        Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ActivityLogin,ActivityMain::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            },this,"page","test","post","login","accountnum",edittextLoginAccountnum.text.toString(),"password",edittextLoginPassword.text.toString())


        }


        floatLoginRegister.setOnClickListener {
            val intent = Intent(this,ActivityRegister::class.java)
            startActivity(intent)
            finish()
        }
    }
}