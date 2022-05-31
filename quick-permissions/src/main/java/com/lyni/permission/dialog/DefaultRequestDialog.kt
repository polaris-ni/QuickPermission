package com.lyni.permission.dialog

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.lyni.permission.R
import com.lyni.permission.core.PermissionActivity


/**
 * @date 2022/3/17
 * @author Liangyong Ni
 * description BaseDialogFragment
 */
class DefaultRequestDialog : DialogFragment(), RequestDialog {
    private val seed = System.currentTimeMillis()

    private var title: String = "请求权限"
    private var desc: String = "应用需要您的授权才能运行，请前往设置中的权限管理进行授权"
    private var posText: String = "去设置"
    private var negText: String = "取消"
    private var posAction: (() -> Unit)? = null
    private var negAction: (() -> Unit)? = null
    private var onDismiss: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStyle(STYLE_NORMAL, android.R.style.ThemeOverlay_Material_Dialog)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return@setOnKeyListener true
                }
                false
            }
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            this@DefaultRequestDialog.isCancelable = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_default_request, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        val params = dialog?.window?.attributes
        params?.let {
            it.width = ViewGroup.LayoutParams.MATCH_PARENT
            it.height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.gravity = Gravity.CENTER
        }
        dialog?.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            decorView.setPadding(getDp32InPx(), 0, getDp32InPx(), 0)
            attributes = params as WindowManager.LayoutParams
        }
        super.onStart()
    }

    private fun initView() {
        view?.findViewById<TextView>(R.id.tvTitle)?.text = title
        view?.findViewById<TextView>(R.id.tvDesc)?.text = desc
        view?.findViewById<TextView>(R.id.tvCancel)?.apply {
            text = negText
            setOnClickListener { negAction?.invoke() }
        }
        view?.findViewById<TextView>(R.id.tvEnsure)?.apply {
            text = posText
            setOnClickListener { posAction?.invoke() }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().remove(this).commit()
            super.show(manager, tag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun info(title: String?, desc: String?): RequestDialog {
        this.title = title ?: "请求权限"
        this.desc = desc ?: "应用需要您的授权才能运行，请前往设置中的权限管理进行授权"
        return this
    }

    override fun positiveBtn(text: String, block: () -> Unit): RequestDialog {
        posAction = block
        this.posText = text
        return this
    }

    override fun negativeBtn(text: String, block: () -> Unit): RequestDialog {
        negAction = block
        this.negText = text
        return this
    }

    override fun onDismiss(block: (() -> Unit)?): RequestDialog {
        onDismiss = block
        return this
    }

    override fun build(): RequestDialog {
        return this
    }

    override fun show(activity: PermissionActivity) = show(activity.requireFragmentManager(), this.javaClass.simpleName + seed)

    private fun getDp32InPx(): Int {
        val scale: Float = requireContext().resources.displayMetrics.density
        return (32 * scale + 0.5f).toInt()
    }
}