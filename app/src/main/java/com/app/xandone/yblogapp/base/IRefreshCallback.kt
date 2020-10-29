package com.app.xandone.yblogapp.base

/**
 * author: Admin
 * created on: 2020/9/2 11:31
 * description:
 */
interface IRefreshCallback {
    fun getData()
    fun getDataMore()
    fun finishRefresh()
    fun finishLoadMore()
}