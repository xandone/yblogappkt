package com.app.xandone.yblogapp.config

import android.Manifest

/**
 * author: Admin
 * created on: 2020/9/23 16:49
 * description:
 */
object IMyPermission {
    const val RC_WRITE_AND_READ_PERM_CODE = 100
    const val RC_BROWSE_THE_CAMERA_CODE = 101
    val WRITE_AND_READ_PERMS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )
    const val BROWSE_THE_CAMERA = Manifest.permission.CAMERA
}