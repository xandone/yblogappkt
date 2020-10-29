package com.app.xandone.baselib.cache

import android.content.Context
import android.os.Environment
import com.app.xandone.baselib.config.BaseConfig
import com.app.xandone.baselib.log.LogHelper
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.math.BigDecimal

/**
 * author: Admin
 * created on: 2020/8/11 11:32
 * description:
 */
object FileHelper {
    /**
     * @param context
     * @return
     */
    fun getCacheDir(context: Context): String {
        return context.cacheDir.absolutePath
    }

    fun getExternalFilesDirFile(context: Context): File? {
        return context.getExternalFilesDir(null)
    }

    fun getExternalFilesDir(context: Context): String {
        return context.getExternalFilesDir(null)!!.absolutePath
    }

    fun getExternalStorageDirectory(context: Context?): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }

    fun getExternalFilesDirDcim(context: Context): String {
        return context.getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.absolutePath
    }

    fun getExternalCacheDir(context: Context): String {
        return context.externalCacheDir!!.absolutePath
    }

    /**
     * 获取app缓存文件夹位置
     *
     * @param context
     * @return
     */
    fun getAppCacheDir(context: Context): String {
        val cacheDir =
            File(getExternalCacheDir(context) + File.separator + BaseConfig.appName)
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        return cacheDir.path
    }

    fun readAssetsFile(filename: String?, context: Context): String {
        return try {
            val inputStream = context.assets.open(filename!!)
            val bytes = ByteArray(1024)
            var read: Int
            val content = StringBuilder()
            while (inputStream.read(bytes, 0, bytes.size).also { read = it } != -1) {
                content.append(String(bytes, 0, read))
            }
            closeIo(inputStream)
            content.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    fun closeIo(vararg closeables: Closeable?) {
        for (closeable in closeables) {
            if (closeable != null) {
                try {
                    closeable.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun deleteDir(path: String?): Boolean {
        val file = File(path)
        //判断是否待删除目录是否存在
        if (!file.exists()) {
            return false
        }
        //取得当前目录下所有文件和文件夹
        val content = file.list() ?: return true
        for (name in content) {
            val temp = File(path, name)
            //判断是否是目录
            if (temp.isDirectory) {
                //递归调用，删除目录里的内容
                deleteDir(temp.absolutePath)
                //删除空目录
                temp.delete()
            } else {
                //直接删除文件
                if (!temp.delete()) {
                    LogHelper.Companion.d("Failed to delete $name")
                }
            }
        }
        return true
    }

    @Throws(Exception::class)
    fun getFolderSize(file: File?): Long {
        if (!file!!.exists()) {
            return 0
        }
        var size: Long = 0
        try {
            val fileList = file.listFiles() ?: return 0
            for (i in fileList.indices) {
                // 如果下面还有文件
                size = if (fileList[i].isDirectory) {
                    size + getFolderSize(fileList[i])
                } else {
                    size + fileList[i].length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     * 格式化文件单位
     *
     * @param size
     * @return
     */
    fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0KB"
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(kiloByte.toString())
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "KB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "MB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return (result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB")
    }
}