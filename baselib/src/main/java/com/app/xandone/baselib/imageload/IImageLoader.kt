package com.app.xandone.baselib.imageload

import android.content.Context
import android.view.View
import androidx.annotation.UiThread
import java.io.File

/**
 * @author: Admin
 * created on: 2020/8/11 16:24
 * description:
 */
interface IImageLoader<T : View?> {
    fun display(context: Context?, file: Any?, view: T)
    fun loadSource(
        context: Context?,
        file: Any?,
        callback: SourceCallback?
    )

    interface SourceCallback {
        @UiThread
        fun onStart()

        @UiThread
        fun onProgress(progress: Int)

        @UiThread
        fun onDelivered(isDisplaySuccess: Boolean, source: File?)
    }
}