package com.app.xandone.yblogapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.app.xandone.yblogapp.config.AppConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * author: Admin
 * created on: 2020/8/11 14:08
 * description:
 */
class App : Application() {
    companion object {
        var sContext: Application? = null

        //static 代码段可以防止内存泄露
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)
                ClassicsHeader(context)
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter(context).setDrawableSize(20f)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        sContext = this
        init()
    }

    private fun init() {
        AppConfig.init(this, BuildConfig.DEBUG)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}