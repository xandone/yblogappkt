package com.xandone.manager2.extention

import android.view.View

/**
 * @author: xiao
 * created on: 2023/2/17 16:42
 * description:
 */

fun bindClick(vararg views: View, block: View.() -> Unit) {
    views.forEach {
        it.setOnClickListener {
            block(it)
        }
    }
}

fun bindClick2(vararg views: View, block: View.() -> Unit) {
    views.forEach {
        it.setOnClickListener {
            it.block()
        }
    }
}
