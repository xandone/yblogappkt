package com.app.xandone.yblogapp

import android.util.Log
import androidx.lifecycle.*

/**
 * author: Admin
 * created on: 2020/8/11 11:21
 * description:
 */
class ATest : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
     Log.d("hfghfg",event.name)
    }

}