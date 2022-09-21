package com.app.xandone.baselib.base

import android.view.View

/**
 * @author: xiao
 * created on: 2022/9/21 14:31
 * description:
 */

fun setClickAction(vararg views: View, block: View.() -> Unit) {
    val listener = View.OnClickListener(block)
    views.forEach { it.setOnClickListener(listener) }
}

fun setClickAction(vararg views: View, listener: View.OnClickListener) {
    views.forEach { it.setOnClickListener(listener) }
}