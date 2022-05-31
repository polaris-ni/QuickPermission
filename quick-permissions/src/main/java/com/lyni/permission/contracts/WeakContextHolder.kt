package com.lyni.permission.contracts

import android.content.Context
import java.lang.ref.WeakReference

abstract class WeakContextHolder(private val contextRef: WeakReference<Context>) {

    val context: Context?
        get() = contextRef.get()
}
