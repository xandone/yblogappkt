package com.xandone.manager2.extention

import android.widget.Toast
import com.app.xandone.yblogapp.App

/**
 * @author: xiao
 * created on: 2023/1/31 11:36
 * description:
 */

fun CharSequence.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(App.sContext, this, duration).show()
}