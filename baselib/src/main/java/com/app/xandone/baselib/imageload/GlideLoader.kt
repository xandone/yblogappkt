package com.app.xandone.baselib.imageload

import android.content.Context
import android.widget.ImageView
import com.app.xandone.baselib.imageload.IImageLoader.SourceCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File

/**
 * @author: Admin
 * created on: 2020/8/11 16:34
 * description:
 */
class GlideLoader private constructor() : AbstractImageLoader(), IEngine<RequestManager> {
    override fun display(context: Context?, file: Any?, view: ImageView?) {
        if (view != null) {
            Glide.with(context!!).load(file).into(view)
        }
    }

    override fun loadSource(
        context: Context?,
        file: Any?,
        callback: SourceCallback?
    ) {
        callback?.onStart()
        Glide.with(context!!).download(file).listener(object : RequestListener<File?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any,
                target: Target<File?>,
                isFirstResource: Boolean
            ): Boolean {
                callback?.onDelivered(false, null)
                return false
            }

            override fun onResourceReady(
                resource: File?,
                model: Any,
                target: Target<File?>,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                callback?.onDelivered(true, resource)
                return false
            }
        }).preload()
    }

    override fun getEngine(context: Context?): RequestManager {
        return Glide.with(context!!)
    }

    private object Builder {
        val GLIDELOADER = GlideLoader()
    }

    companion object {
        val instance: GlideLoader = Builder.GLIDELOADER
    }
}