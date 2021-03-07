package com.example.moneycounter4.base

import android.app.ProgressDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.lang.reflect.ParameterizedType

/**
 *@author zhangzhe
 *@date 2021/3/6
 *@description
 */

abstract class BaseViewModelActivity<T : BaseViewModel> : BaseActivity() {
    lateinit var viewModel: T

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(this).apply {
            isIndeterminate = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNCHECKED_CAST")
        viewModel = ViewModelProviders.of(this)
            .get((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>)
        viewModel.toastEvent.observeNotNull {
            if (it.isNotBlank()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.progressDialogEvent.observeNotNull {
            progressDialog.setMessage(it)
            if (it.isNotBlank()) {
                if (!progressDialog.isShowing) {
                    progressDialog.show()
                }
            } else {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    inline fun <T> LiveData<T>.observe(crossinline onChange: (T?) -> Unit) =
        observe(this@BaseViewModelActivity, Observer { onChange(it) })

    inline fun <T> LiveData<T>.observeNotNull(crossinline onChange: (T) -> Unit) =
        observe(this@BaseViewModelActivity, Observer {
            it ?: return@Observer
            onChange(it)
        })
}

