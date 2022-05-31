package com.lyni.permission.dialog

import com.lyni.permission.core.PermissionActivity

/**
 * @date 2022/5/31
 * @author Liangyong Ni
 * description RequestDialog
 */
interface RequestDialog {


    fun info(title: String?, desc: String?): RequestDialog

    fun positiveBtn(text: String, block: () -> Unit): RequestDialog

    fun negativeBtn(text: String, block: () -> Unit): RequestDialog

    fun onDismiss(block: (() -> Unit)? = null): RequestDialog

    fun build(): RequestDialog

    fun show(activity: PermissionActivity)

}