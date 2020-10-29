package com.app.xandone.yblogapp

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * author: Admin
 * created on: 2020/8/11 11:21
 * description:
 */
class ATest : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.d(TAG, "Atest====>onCreate..")
    }

    companion object {
        val TAG = ATest::class.java.name
    }
}