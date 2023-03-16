package com.app.xandone.yblogapp.config

import android.app.Application
import com.app.xandone.baselib.imageload.ImageLoadHelper
import com.app.xandone.baselib.log.LogHelper
import com.app.xandone.baselib.utils.ToastUtils

/**
 * author: Admin
 * created on: 2020/8/11 11:56
 * description:
 */
object BaseConfig {
    /**
     * app名称
     */
    var appName: String? = null
    fun init(application: Application?, appName: String?, isDebug: Boolean) {
        BaseConfig.appName = appName

        //初始化日志库
        LogHelper.init(LogHelper.ENGINE_LOGGER, isDebug)
        //初始化图片加载引擎
        ImageLoadHelper.instance.initEngine(ImageLoadHelper.Companion.ENGINE_GLIDE)
        //初始化toast，主要是获取application
        ToastUtils.init(application)
    }
}