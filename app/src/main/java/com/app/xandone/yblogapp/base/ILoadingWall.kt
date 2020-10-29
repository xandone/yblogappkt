package com.app.xandone.yblogapp.base

/**
 * author: Admin
 * created on: 2020/9/1 11:19
 * description:
 */
interface ILoadingWall {
    fun onLoading()
    fun onLoadEmpty()
    fun onLoadSeverError()
    fun onLoadNetError()
    fun onLoadFinish()
    fun onLoadStatus(statusCode: Int)
}