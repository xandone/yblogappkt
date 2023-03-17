package com.app.xandone.yblogapp.base

/**
 * author: Admin
 * created on: 2020/9/1 11:19
 * description:
 */
interface ILoadingWall {
    fun onLoading()
    fun onLoadEmpty(tag: Any? = null)
    fun onLoadSeverError(tag: Any? = null)
    fun onLoadNetError(tag: Any? = null)
    fun onLoadFinish(tag: Any? = null)


    fun reload(tag: Any? = null)
}