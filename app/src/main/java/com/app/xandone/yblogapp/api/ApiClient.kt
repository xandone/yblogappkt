package com.app.xandone.yblogapp.api

import com.app.xandone.baselib.cache.FileHelper.getAppCacheDir
import com.app.xandone.baselib.utils.NetworkUtils.isConnected
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.BuildConfig
import com.app.xandone.yblogapp.cache.UserInfoHelper
import com.app.xandone.yblogapp.config.ApiHost
import okhttp3.Cache
import okhttp3.CacheControl.Companion.FORCE_CACHE
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * author: Admin
 * created on: 2020/8/12 16:56
 * description:
 */
class ApiClient {
    private var retrofit: Retrofit? = null

    private constructor() {}
    private constructor(host: String?, isCache: Boolean) {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
            builder.addInterceptor(loggingInterceptor)
        }
        val cacheFile = File(getAppCacheDir(App.sContext!!))
        val cache = Cache(cacheFile, 1024 * 1024 * 50)
        val cacheInterceptor: Interceptor = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                request = request.newBuilder().header("token", UserInfoHelper.adminToken!!).build()
                if (!isConnected(App.sContext!!) && isCache) {
                    request = request.newBuilder()
                        .cacheControl(FORCE_CACHE)
                        .build()
                }
                val response = chain.proceed(request)
                if (isConnected(App.sContext!!)) {
                    val maxAge = 0
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                        .header("Cache-Control", "public, max-age=$maxAge")
                        .removeHeader("Pragma")
                        .build()
                } else {
                    // 无网络时，设置超时为2天
                    val maxStale = 60 * 60 * 24 * 2
                    response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .removeHeader("Pragma")
                        .build()
                }
                return response
            }
        }
        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor)
        builder.addInterceptor(cacheInterceptor)
        builder.cache(cache)
        //设置超时
        builder.connectTimeout(5, TimeUnit.SECONDS)
        builder.readTimeout(10, TimeUnit.SECONDS)
        builder.writeTimeout(10, TimeUnit.SECONDS)
        //错误重连
        builder.retryOnConnectionFailure(true)
        val client = builder.build()
        retrofit = Retrofit.Builder()
            .baseUrl(host!!)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: IApiService?
        get() {
            var apiService: IApiService? = null
            if (retrofit != null) {
                apiService = retrofit?.create(IApiService::class.java)
            }
            return apiService
        }

    private object Builder {
        private var netClient: ApiClient? = null
        fun create(host: String?, isCache: Boolean): ApiClient? {
            netClient = ApiClient(host, isCache)
            return netClient
        }
    }

    companion object {
        val instance: ApiClient? = Builder.create(ApiHost.DEFAULT_HOST, true)

        fun getInstance(host: String?, isCache: Boolean): ApiClient? {
            return Builder.create(host, isCache)
        }
    }
}