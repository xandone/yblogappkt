package com.app.xandone.baselib.imageload

import android.content.Context
import android.widget.ImageView
import com.app.xandone.baselib.imageload.IImageLoader.SourceCallback

/**
 * author: Admin
 * created on: 2020/8/11 16:35
 * description:
 */
class ImageLoadHelper private constructor() : IImageLoader<ImageView> {
    private var imageLoader: AbstractImageLoader? = null

    @JvmOverloads
    fun initEngine(engine: Int = ENGINE_GLIDE) {
        when (engine) {
            ENGINE_GLIDE -> imageLoader = GlideLoader.instance
            else -> {
            }
        }
    }

    override fun display(
        context: Context?,
        file: Any?,
        view: ImageView
    ) {
        if (imageLoader != null) {
            imageLoader!!.display(context, file, view)
        }
    }

    override fun loadSource(
        context: Context?,
        file: Any?,
        callback: SourceCallback?
    ) {
        if (imageLoader != null) {
            imageLoader!!.loadSource(context, file, callback)
        }
    }

    private object Builder {
        val INSTANCE = ImageLoadHelper()
    }

    companion object {
        const val ENGINE_GLIDE = 1
        val instance: ImageLoadHelper = Builder.INSTANCE
    }
}