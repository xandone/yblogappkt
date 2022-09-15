package com.app.xandone.yblogapp.api.interceptor

import com.app.xandone.baselib.log.LogHelper
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author: xiao
 * created on: 2022/9/15 15:44
 * description:
 */
class LogInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        LogHelper.d(TAG, "${request.url}\n${response.headers}")
        return response
    }


    companion object {
        val TAG = LogInterceptor::class.simpleName
    }
}