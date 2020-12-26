package com.example.moneycounter4.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.example.moneycounter4.R

class MainApplication : Application() {

    val connectionUrlMain = "http://47.116.74.96:8080/mweb/demo1"

    companion object{
        lateinit var app: Application
        lateinit var context: Context
    }


    override fun onCreate() {
        super.onCreate()
        initTypeface()
        app = this
        context = applicationContext
    }

    private fun initTypeface(){
        val typeface = ResourcesCompat.getFont(this, R.font.alibaba_medium)
        val field = Typeface::class.java.getDeclaredField("MONOSPACE")
        field.isAccessible = true
        field.set(null,typeface)
    }

}