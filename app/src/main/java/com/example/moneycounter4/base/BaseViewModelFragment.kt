package com.example.moneycounter4.base

import android.app.ProgressDialog
import android.os.Bundle
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

abstract class BaseViewModelFragment<T : BaseViewModel> : BaseFragment() {
    lateinit var viewModel: T

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(this.context).apply {
            isIndeterminate = true
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        @Suppress("UNCHECKED_CAST")
        viewModel = ViewModelProviders.of(this)
            .get((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>)
        viewModel.toastEvent.observeNotNull {
            if (it.isNotBlank()) {
                Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    inline fun <T> LiveData<T>.observe(crossinline onChange: (T?) -> Unit) =
        activity?.let { observe(it, Observer { onChange(it) }) }

    inline fun <T> LiveData<T>.observeNotNull(crossinline onChange: (T) -> Unit) =
        activity?.let {
            observe(it, Observer { obj ->
                obj ?: return@Observer
                onChange(obj)
            })
        }
}

