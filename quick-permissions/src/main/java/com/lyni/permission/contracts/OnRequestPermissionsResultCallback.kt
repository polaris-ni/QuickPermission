package com.lyni.permission.contracts

interface OnRequestPermissionsResultCallback {

    fun onRequestPermissionsResult(permissions: List<String>, grantResults: IntArray)

}
