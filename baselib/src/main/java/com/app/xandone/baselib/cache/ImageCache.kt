package com.app.xandone.baselib.cache

import android.content.Context
import android.os.Build
import java.io.File

/**
 * author: Admin
 * created on: 2020/9/24 10:17
 * description:
 */
object ImageCache {
    fun getImageCache(context: Context): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            FileHelper.getExternalFilesDir(context)
        } else {
            FileHelper.getExternalStorageDirectory(context) + File.separator + "yblog"
        }
    }
}