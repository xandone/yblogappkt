package com.app.xandone.baselib.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * author: Admin
 * created on: 2020/8/12 17:34
 * description:
 */
object NetworkUtils {
    /**
     * 判断网络连接是否有效（此时可传输数据）。
     *
     * @return boolean 不管wifi，还是mobile net，只有当前在连接状态（可有效传输数据）才返回true,反之false。
     */
    fun isConnected(context: Context): Boolean {
        val net = getConnectivityManager(context).activeNetworkInfo
        return net != null && net.isConnected
    }

    /**
     * 获取ConnectivityManager
     */
    fun getConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}