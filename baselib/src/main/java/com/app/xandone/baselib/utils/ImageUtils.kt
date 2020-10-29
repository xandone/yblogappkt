package com.app.xandone.baselib.utils

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.provider.MediaStore
import java.io.*
import java.net.URLConnection

/**
 * author: Admin
 * created on: 2020/9/24 14:10
 * description:
 */
object ImageUtils {
    /**
     * 适配 anroid10
     * 通过MediaStore保存，兼容AndroidQ，保存成功自动添加到相册数据库，无需再发送广播告诉系统插入相册
     *
     * @param context     context
     * @param sourceFile  源文件
     * saveFileName 保存的文件名
     * @param saveDirName picture子目录
     * @return 成功或者失败
     */
    fun saveFile2SdCard(
        context: Context,
        sourceFile: File,
        saveDirName: String
    ) {
        val mimeType = getMimeType(sourceFile)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val fileName = sourceFile.name
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DESCRIPTION, "This is an image")
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            values.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + File.separator + saveDirName
            )
            val contentResolver = context.contentResolver
            val uri =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri == null) {
                ToastUtils.showShort("保存失败")
                return
            }
            var out: OutputStream? = null
            var fis: FileInputStream? = null
            try {
                out = contentResolver.openOutputStream(uri)
                fis = FileInputStream(sourceFile)
                if (out != null) {
                    FileUtils.copy(fis, out)
                    ToastUtils.showShort("已保存至相册")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                closeIo(out!!, fis!!)
            }
        } else {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(sourceFile.path),
                arrayOf(mimeType)
            ) { path, uri -> }
            ToastUtils.showShort("已保存至相册")
        }
    }

    private fun getMimeType(file: File): String {
        val fileNameMap = URLConnection.getFileNameMap()
        return fileNameMap.getContentTypeFor(file.name)
    }

    private fun closeIo(vararg closeables: Closeable) {
        for (closeable in closeables) {
            try {
                closeable.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}