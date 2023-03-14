package com.example.weatherapp.helpers

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.example.weatherapp.MainActivity

fun MainActivity.checkPermission(permissionName: String): Boolean {
    return if (Build.VERSION.SDK_INT >= 28) {
        val granted =
            ContextCompat.checkSelfPermission(this, permissionName)
        granted == PackageManager.PERMISSION_GRANTED
    } else {
        val granted =
            PermissionChecker.checkSelfPermission(this, permissionName)
        granted == PermissionChecker.PERMISSION_GRANTED
    }
}