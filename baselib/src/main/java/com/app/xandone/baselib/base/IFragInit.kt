package com.app.xandone.baselib.base

import android.view.View
import androidx.annotation.LayoutRes

/**
 * author: Admin
 * created on: 2020/10/26 15:21
 * description:
 */
interface IFragInit {
    /**
     * 加载布局前的一些操作
     */
    fun doBeforeSetContentView()

    @LayoutRes
    fun getLayout(): Int

    fun init(view: View?)
}