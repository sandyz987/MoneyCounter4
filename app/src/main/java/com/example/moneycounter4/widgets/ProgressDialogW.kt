package com.example.moneycounter4.widgets

import android.app.ProgressDialog
import android.content.Context

//转圈圈的弹窗

object ProgressDialogW {
    private var progressDialog : ProgressDialog? = null

    fun show(context: Context, title: String, message: String, cancelable: Boolean) {
        progressDialog = ProgressDialog.show(context, title, message, true, cancelable);
        progressDialog?.setTitle(title);
        progressDialog?.setMessage(message);
        progressDialog?.show()
    }
    fun hide(){
        if (progressDialog != null && progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }
}