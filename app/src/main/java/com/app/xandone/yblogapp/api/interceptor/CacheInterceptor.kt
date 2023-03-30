package com.app.xandone.yblogapp.api.interceptor

import com.app.xandone.baselib.utils.NetworkUtils
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.cache.UserInfoHelper
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author: xiao
 * created on: 2022/9/15 11:11
 * description:
 */
class CacheInterceptor(var isCache: Boolean = true) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!NetworkUtils.isConnected(App.sContext) && isCache) {
            val maxStale = 2 * 24 * 60 * 60
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("Retrofit")
                .build()
        } else {
            val maxAge = 0
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_NETWORK)
                .header("token", UserInfoHelper.adminToken!!)
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Retrofit")
                .build()
        }
        return chain.proceed(request)
    }
}