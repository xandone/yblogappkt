package com.app.xandone.baselib.cache

import android.content.Context

/**
 * author: Admin
 * created on: 2020/8/11 15:52
 * description:
 */
object CacheHelper {
    /**
     * 清除默认的sp下的某个key值
     *
     * @param context
     * @param key
     */
    fun clearDefaultSp(context: Context, key: String?) {
        SpHelper.clearDefaultSp(context, key)
    }

    /**
     * 清除所有sp缓存
     *
     * @param context
     * @param names
     */
    fun clearSpCache(context: Context, vararg names: String?) {
        SpHelper.clearAllSp(context, *names)
    }

    /**
     * 清除ExternalFilesDir文件夹
     */
    fun clearExternalFilesDir(context: Context) {
        FileHelper.deleteDir(FileHelper.getExternalFilesDir(context))
    }

    /**
     * 清除所有缓存,包括sp缓存
     *
     * @param context
     * @param names
     */
    fun clearAllCache(context: Context, vararg names: String?) {
        clearSpCache(context, *names)
        clearExternalFilesDir(context)
    }

    /**
     * 获取全部缓存文件大小
     *
     * @param context
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getTotalCacheSize(context: Context): String? {
        val cacheSize = FileHelper.getFolderSize(FileHelper.getExternalFilesDirFile(context))
        return FileHelper.getFormatSize(cacheSize.toDouble())
    }
}