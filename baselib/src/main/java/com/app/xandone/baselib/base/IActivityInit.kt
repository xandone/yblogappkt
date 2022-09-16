package com.app.xandone.baselib.base

import androidx.annotation.LayoutRes


/**
 *
 * author: Admin
 * created on: 2020/10/26 10:53
 * description:
 */
interface IActivityInit {
    /**
     * 加载布局前的一些操作
     */
    fun doBeforeSetContentView()

    @LayoutRes
    fun getLayout(): Int

    fun initView()
}