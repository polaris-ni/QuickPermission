package com.lyni.permission.core

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lyni.permission.QuickPermission
import com.lyni.permission.R
import com.lyni.permission.contracts.OnRequestPermissionsResultCallback

class PermissionActivity : AppCompatActivity(), OnRequestPermissionsResultCallback {

    companion object {
        const val REQUEST_CODE = 1029
    }

    private lateinit var request: Request

    private val deniedPermissions: Array<String>
        get() = getDeniedPermissions(request.permissions)

    private lateinit var settingActivityResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        request = RequestManager.getCurrentRequest()
        settingActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (deniedPermissions.isEmpty()) {
                request.successCallback?.invoke()
                finish()
            } else {
                showDialog()
            }
        }
        requestPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(permissions.toList(), grantResults)
    }

    private fun requestPermissions() {
        val deniedPermissions = getDeniedPermissions(request.permissions)
        if (deniedPermissions.isEmpty()) {
            request.successCallback?.invoke()
            finish()
        } else {
            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE)
        }
    }

    private fun openSetting() {
        try {
            val settingIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            settingIntent.data = Uri.fromParts("package", packageName, null)
            settingActivityResult.launch(settingIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, R.string.fail_open_setting, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getDeniedPermissions(permissions: List<String>?): Array<String> {
        val deniedPermissionList = ArrayList<String>()
        permissions?.forEach { permission ->
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissionList.add(permission)
            }
        }
        return deniedPermissionList.toTypedArray()
    }

    private fun showDialog() {
        QuickPermission.newRequestDialog().info(request.title, request.rationale).positiveBtn("去设置") {
            openSetting()
        }.negativeBtn("取消") {
            request.failureCallback?.invoke(deniedPermissions)
            finish()
        }.onDismiss { finish() }.show(this)
    }

    override fun onRequestPermissionsResult(permissions: List<String>, grantResults: IntArray) {
        val deniedPermissions = getDeniedPermissions(permissions)
        if (deniedPermissions.isEmpty()) {
            request.successCallback?.invoke()
            finish()
        } else {
            showDialog()
        }
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        RequestManager.startNextRequest()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    fun requireFragmentManager() = supportFragmentManager
}
