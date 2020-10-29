package com.app.xandone.widgetlib.utils

import android.content.Context

/**
 * author: Admin
 * created on: 2020/9/4 09:53
 * description:
 */
object SizeUtils {
    /**
     * dp 转 px
     *
     * @param dpValue dp 值
     * @return px 值
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px 转 dp
     *
     * @param pxValue px 值
     * @return dp 值
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * sp 转 px
     *
     * @param spValue sp 值
     * @return px 值
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * px 转 sp
     *
     * @param pxValue px 值
     * @return sp 值
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }
}