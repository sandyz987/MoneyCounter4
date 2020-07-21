package com.example.moneycounter4.view.activity

import android.content.Intent
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
import com.example.moneycounter4.utils.ThreadPool
import com.example.moneycounter4.viewmodel.MainApplication
import com.example.moneycounter4.viewmodel.MainViewModel
import com.example.moneycounter4.widgets.LogW
import kotlinx.android.synthetic.main.activity_login.*

class ActivityWelcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val viewModel :MainViewModel by viewModels()

        if(viewModel.accountNum.get() != null){
            //如果保存了账号密码，就自动登录
            HttpUtil.getInstance().httpGet((application as MainApplication).connectionUrlMain,object :
                HttpUtilCallback {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun doSomething(respond: String?) {
                    if(respond.equals("-1")){//登录失败
                        this@ActivityWelcome.runOnUiThread {
                            startLoginActivity()
                        }

                    }else{//成功
                        this@ActivityWelcome.runOnUiThread {
                            Toast.makeText(applicationContext,R.string.login_success,
                            Toast.LENGTH_SHORT).show()
                            viewModel.saveUsrInfo(null,null,respond?.toInt())
                            startMainActivity()
                        }

                    }

                }

                override fun error() {
                    this@ActivityWelcome.runOnUiThread {
                        Toast.makeText(applicationContext,R.string.login_connection_error,
                        Toast.LENGTH_SHORT).show() }
                    startLoginActivity()
                }
            },this,"page","test","post","login","accountnum",viewModel.accountNum.get(),"password",viewModel.password.get())

        }else{
            ThreadPool.getInstance().execute{
                try {
                    Thread.sleep(1000)
                    startLoginActivity()
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }
        }



    }

    fun startMainActivity(){
        this@ActivityWelcome.finish()
        val mainIntent = Intent(this@ActivityWelcome, ActivityMain::class.java)
        startActivity(mainIntent)
        //this@ActivityWelcome.finish()
    }

    fun startLoginActivity(){
        this@ActivityWelcome.finish()
        val mainIntent = Intent(this@ActivityWelcome, ActivityLogin::class.java)
        startActivity(mainIntent)

    }
}