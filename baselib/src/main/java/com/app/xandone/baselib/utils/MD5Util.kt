package com.app.xandone.baselib.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * author: Admin
 * created on: 2020/9/27 15:48
 * description:
 */
object MD5Util {
    fun MD5(str: String): String? {
        return try {
            val md5 = MessageDigest.getInstance("MD5")
            md5.update(str.toByteArray())
            val bytes = md5.digest()
            val sb = StringBuilder("")
            for (n in bytes.indices) {
                var i = bytes[n].toInt()
                if (i < 0) {
                    i += 256
                }
                if (i < 16) {
                    sb.append("0")
                }
                sb.append(Integer.toHexString(i))
            }
            sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }
    }
}