package com.example.moneycounter4.widgets

import android.app.Activity
import android.os.Message
import java.lang.ref.WeakReference

interface SafeHandlerCallback<T> {
    fun doSomething(msg:Message,weakReference :WeakReference<T>)
}