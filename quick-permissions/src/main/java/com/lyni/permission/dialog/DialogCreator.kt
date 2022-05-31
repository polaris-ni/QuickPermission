package com.lyni.permission.dialog

/**
 * @date 2022/5/31
 * @author Liangyong Ni
 * description DialogCreator
 */
interface DialogCreator {
    fun newInstance(): RequestDialog
}