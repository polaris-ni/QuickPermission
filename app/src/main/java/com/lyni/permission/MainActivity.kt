package com.lyni.permission

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.lyni.permission.core.Permissions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.btnPermissionRequest)
        btn.setOnClickListener {
            QuickPermission.with(this)
                .addPermissions(*Permissions.Group.STORAGE)
                .info("存储权限申请", "应用需要存储权限才能运行，请前往设置中的权限管理进行授权")
                .onDenied {
                    Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show()
                }
                .onGranted {
                    Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show()
                    Permissions.Group.STORAGE.forEach {
                        if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) {
                            Log.e("TAG", "onCreate: $it")
                        }
                    }
                }.request()
        }
    }
}