package com.app.xandone.baselib.utils

import android.app.Application
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.app.xandone.baselib.R

/**
 * author: Admin
 * created on: 2020/8/13 16:17
 * description:
 */
object ToastUtils {
    private var toast_1: Toast? = null
    private var toast_2: Toast? = null
    private var app: Application? = null

    //静态变量传递的是值，不存在创建了多个app
    fun init(application: Application?) {
        app = application
    }

    private fun createToast(msg: CharSequence, duration: Int): Toast? {
        if (toast_1 == null) {
            toast_1 = Toast.makeText(app, msg, duration)
        } else {
            toast_1!!.setText(msg)
            toast_1!!.duration = duration
        }
        return toast_1
    }

    fun showShort(msg: CharSequence) {
        createToast(msg, Toast.LENGTH_SHORT)!!.show()
    }

    fun showShort(strId: Int) {
        createToast(app!!.getText(strId), Toast.LENGTH_SHORT)!!.show()
    }

    fun showLong(msg: CharSequence) {
        createToast(msg, Toast.LENGTH_SHORT)!!.show()
    }

    fun showLong(strId: Int) {
        createToast(app!!.getText(strId), Toast.LENGTH_SHORT)!!.show()
    }

    fun showToastWithImg(tvStr: String?, imageResource: Int): Toast? {
        if (toast_2 == null) {
            toast_2 = Toast(app)
        }
        val view: View =LayoutInflater.from(app).inflate(R.layout.toast_custom, null)
        val tv = view.findViewById<View>(R.id.toast_custom_tv) as TextView
        tv.text = if (TextUtils.isEmpty(tvStr)) "" else tvStr
        val iv =view.findViewById<View>(R.id.toast_custom_iv) as ImageView
        if (imageResource > 0) {
            iv.visibility = View.VISIBLE
            iv.setImageResource(imageResource)
        } else {
            iv.visibility = View.GONE
        }
        toast_2!!.setView(view)
        toast_2!!.setGravity(Gravity.CENTER, 0, 0)
        toast_2!!.show()
        return toast_2
    }
}