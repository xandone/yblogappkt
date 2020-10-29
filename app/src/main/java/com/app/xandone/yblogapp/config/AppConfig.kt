package com.app.xandone.yblogapp.config

import android.app.Application
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.app.xandone.baselib.config.BaseConfig.init

/**
 * author: Admin
 * created on: 2020/8/11 14:07
 * description:
 */
object AppConfig {
    private const val APP_NAME = ""
    var SCREEN_WIDTH = -1
    var SCREEN_HEIGHT = -1
    fun init(application: Application, isDebug: Boolean) {
        init(application, APP_NAME, isDebug)
        getScreenSize(application)
    }

    fun getScreenSize(context: Context) {
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        if (windowManager == null) {
            return
        }
        val display = windowManager.defaultDisplay
        display.getMetrics(dm)
        SCREEN_WIDTH = dm.widthPixels
        SCREEN_HEIGHT = dm.heightPixels
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            val t = SCREEN_HEIGHT
            SCREEN_HEIGHT = SCREEN_WIDTH
            SCREEN_WIDTH = t
        }
    }
}