package com.app.xandone.yblogapp

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.TextView
import com.app.xandone.yblogapp.config.AppConfig
import com.app.xandone.yblogapp.model.repository.ApiEmptyResponse
import com.app.xandone.yblogapp.model.repository.ApiErrorResponse
import com.app.xandone.yblogapp.model.repository.ApiOtherErrorResponse
import com.app.xandone.yblogapp.view.statelayout.StateConfig
import com.app.xandone.yblogapp.view.statelayout.handler.FadeStateChangedHandler
import com.app.xandone.yblogapp.widget.ClassicsHeaderVisible
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
        lateinit var sContext: Application

        //static 代码段可以防止内存泄露
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.app_bg_color, R.color.light_tv)
                val header = ClassicsHeaderVisible(context)
                header.setEnableLastTime(false)
                header.setTextSizeTitle(0F)
                header
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

        StateConfig.apply {
            emptyLayout = R.layout.layout_empty
            errorLayout = R.layout.layout_error
            loadingLayout = R.layout.layout_loading
            stateChangedHandler = FadeStateChangedHandler()
            setRetryIds(R.id.msg, R.id.iv, R.id.btn)

            onEmpty {
                var resp = it as ApiEmptyResponse<*>
                findViewById<TextView>(R.id.msg).text = resp.empty
            }

            onError {
                when (it) {
                    is ApiErrorResponse<*> -> {
                        findViewById<TextView>(R.id.msg).text = it.msg
                    }
                    is ApiOtherErrorResponse<*> -> {
                        findViewById<TextView>(R.id.msg).text = it.errorMessage
                    }
                }
            }
        }
    }

    private fun init() {
        AppConfig.init(this, BuildConfig.DEBUG)
        //管理Activity
        ActManager.getInstance().init(this)

    }

}