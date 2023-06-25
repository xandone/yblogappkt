package com.app.xandone.yblogapp.api

import com.app.xandone.yblogapp.BuildConfig
import okhttp3.Dns
import java.net.InetAddress

/**
 * @author: xiao
 * created on: 2023/3/17 14:09
 * description:
 */
class WDns : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        if (BuildConfig.DEBUG && hostname in host) {
            return listOf(*InetAddress.getAllByName("192.168.3.117"))
        }
        return Dns.SYSTEM.lookup(hostname)
    }

    companion object {
        val host = arrayOf("www.test.com")
    }
}