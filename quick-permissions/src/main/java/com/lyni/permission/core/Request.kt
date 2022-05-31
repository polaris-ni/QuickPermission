package com.lyni.permission.core

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lyni.permission.contracts.WeakContextHolder
import com.lyni.permission.sources.ActivitySource
import com.lyni.permission.sources.FragmentSource

/**
 * 请求
 */
internal class Request {
    internal val requestTime: Long = System.currentTimeMillis()
    private val source: WeakContextHolder
    val permissions = mutableListOf<String>()
    var successCallback: (() -> Unit)? = null
        private set
    var failureCallback: ((deniedPermissions: Array<String>) -> Unit)? = null
        private set
    var rationale: String? = null
    var title: String? = null

    constructor(activity: FragmentActivity) {
        source = ActivitySource(activity)
    }

    constructor(fragment: Fragment) {
        source = FragmentSource(fragment)
    }

    fun addPermissions(vararg permissions: String) {
        this.permissions.addAll(permissions.toList())
    }

    fun setOnGrantedCallback(callback: () -> Unit) {
        this.successCallback = callback
    }

    fun setOnDeniedCallback(callback: (deniedPermissions: Array<String>) -> Unit) {
        this.failureCallback = callback
    }

    fun start() {
        source.context?.let {
            val intent = Intent(it, PermissionActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.startActivity(intent)
        }
    }

}