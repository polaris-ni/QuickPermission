package com.lyni.permission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lyni.permission.core.Request
import com.lyni.permission.core.RequestManager
import com.lyni.permission.dialog.DefaultProvider
import com.lyni.permission.dialog.DialogCreator
import com.lyni.permission.dialog.RequestDialog

@Suppress("unused")
class QuickPermission private constructor() {
    private var request: Request? = null

    companion object {

        private var dialogCreator: DialogCreator? = null

        fun with(fragment: Fragment) = Builder(fragment)

        fun with(activity: FragmentActivity) = Builder(activity)

        fun setDialogCreator(dialogCreator: DialogCreator) {
            this.dialogCreator = dialogCreator
        }

        @Synchronized
        fun newRequestDialog(): RequestDialog {
            if (dialogCreator == null) {
                dialogCreator = DefaultProvider()
            }
            return dialogCreator!!.newInstance()
        }
    }

    fun request() {
        request?.run {
            RequestManager.pushRequest(this)
        } ?: throw NullPointerException("request is null, consider to call QuickPermission#Builder first.")
    }

    class Builder {
        private val request: Request

        internal constructor(activity: FragmentActivity) {
            request = Request(activity)
        }

        internal constructor(fragment: Fragment) {
            request = Request(fragment)
        }

        fun addPermissions(vararg permissions: String): Builder {
            request.addPermissions(*permissions)
            return this
        }

        fun onGranted(callback: () -> Unit): Builder {
            request.setOnGrantedCallback(callback)
            return this
        }

        fun onDenied(callback: (deniedPermissions: Array<String>) -> Unit): Builder {
            request.setOnDeniedCallback(callback)
            return this
        }

        fun info(title: String? = null, desc: String? = null): Builder {
            request.title = title
            request.rationale = desc
            return this
        }

        private fun build(): QuickPermission {
            val compat = QuickPermission()
            compat.request = request
            return compat
        }

        fun request() = build().apply { request() }
    }

}
