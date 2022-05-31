package com.lyni.permission.dialog

/**
 * @date 2022/5/31
 * @author Liangyong Ni
 * description DefaultProvider
 */
class DefaultProvider : DialogCreator {
    override fun newInstance(): RequestDialog {
        return DefaultRequestDialog()
    }
}