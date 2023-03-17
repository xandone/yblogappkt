package com.app.xandone.yblogapp.api

import com.app.xandone.baselib.cache.FileHelper.getAppCacheDir
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.api.interceptor.CacheInterceptor
import com.app.xandone.yblogapp.api.interceptor.LogInterceptor
import com.app.xandone.yblogapp.config.ApiHost
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * author: Admin
 * created on: 2020/8/12 16:56
 * description:
 */
object ApiClient {

    val cacheFile by lazy {
        File(getAppCacheDir(App.sContext))
    }
    val cache by lazy {
        Cache(cacheFile, 1024 * 1024 * 50L)
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
        .addInterceptor(HeaderInterceptor())
        .addNetworkInterceptor(CacheInterceptor())
        .addInterceptor(LogInterceptor())
        .cache(cache)
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        //错误重连
        .retryOnConnectionFailure(true)
        .dns(WDns())
        .build()

    private val builder = Retrofit.Builder()
        .baseUrl(ApiHost.DEFAULT_HOST)
        .client(httpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().registerTypeAdapterFactory(
                    GsonTypeAdapterFactory()
                ).create()
            )
        )

    private val mRetrofit = builder.build()

    fun create(): IApiService = mRetrofit.create(IApiService::class.java)

    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val request = originalRequest.newBuilder().apply {
                header("model", "Android")
                header("If-Modified-Since", "${Date()}")
                header("User-Agent", System.getProperty("http.agent") ?: "unknown")
            }.build()
            return chain.proceed(request)
        }
    }
}