package com.app.xandone.baselib.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * author: Admin
 * created on: 2020/10/26 10:51
 * description:
 */
abstract class BaseSimpleActivity : AppCompatActivity(), IActivityInit {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        doBeforeSetContentView()
        initContentView()
        initView()
    }

    protected open fun initContentView() {
        setContentView(getLayout())
    }

    override fun doBeforeSetContentView() {

    }
    protected open fun startActivity(activity: Activity) {
//        startActivity(Intent(this, activity::class.java))
    }

}