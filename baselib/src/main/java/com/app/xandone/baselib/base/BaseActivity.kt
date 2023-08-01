package com.app.xandone.baselib.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.app.xandone.baselib.utils.KeyboardUtils
import org.greenrobot.eventbus.EventBus


/**
 * author: Admin
 * created on: 2020/10/26 10:50
 * description:
 */
abstract class BaseActivity : AppCompatActivity(), IActivityInit {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doBeforeSetContentView()
        if (isRegistEventBus()) {
            EventBus.getDefault().register(this)
        }

        initContentView()

        initSoftKeyBoard()

        initView()

    }


    protected open fun startActivity(activity: Activity) {
        startActivity(Intent(this, activity::class.java))
    }


    override fun onDestroy() {
        super.onDestroy()
        if (isRegistEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    open fun isRegistEventBus(): Boolean {
        return false
    }

    private fun initSoftKeyBoard(){
        findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT).setOnClickListener {
            KeyboardUtils.hideSoftInput(this)
        }
    }

}