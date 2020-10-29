package com.app.xandone.baselib.imageload

import android.content.Context

/**
 * author: Admin
 * created on: 2020/8/11 16:55
 * description:
 */
interface IEngine<T> {
    fun getEngine(context: Context?): T
}