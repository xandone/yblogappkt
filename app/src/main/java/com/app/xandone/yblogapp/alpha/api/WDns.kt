package com.xandone.manager2.api

import com.app.xandone.yblogapp.BuildConfig
import okhttp3.Dns
import java.net.InetAddress

/**
 * @author: xiao
 * created on: 2023/2/14 11:00
 * description:
 */
class WDns : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        if (BuildConfig.DEBUG) {
            for (s in host) {
                if (s == hostname) {
                    return listOf(*InetAddress.getAllByName("192.168.1.1"))
                }
            }
        }
        return Dns.SYSTEM.lookup(hostname)
    }


    companion object {
        val host = arrayOf(
            ""
        )
    }
}