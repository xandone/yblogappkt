package com.app.xandone.yblogapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author: xiao
 * created on: 2022/9/19 09:59
 * description:
 */
class ActManager private constructor() : Application.ActivityLifecycleCallbacks {
    private val actStack = Stack<WeakReference<Activity>>()
    private lateinit var app: Application

    fun init(application: Application) {
        this.app = application
        this.app.registerActivityLifecycleCallbacks(this)
    }

    companion object {
        fun getInstance(): ActManager {
            return Holder.INSTANCE
        }
    }

    object Holder {
        val INSTANCE = ActManager()
    }

    fun pushAct(ref: WeakReference<Activity>) {
        actStack.push(ref)
    }

    fun removeAct(ref: WeakReference<Activity>) {
        actStack.remove(ref)
    }

    fun removeById(id: Int) {
        if (id < actStack.size) {
            actStack.removeAt(id)
        }
    }

    fun removeAll() {
        for (ref in actStack) {
            val act = ref.get()
            if (act != null && !act.isFinishing) {
                act.finish()
            }
        }
    }

    fun getTopAct(): Activity? {
        return actStack[actStack.size - 1].get()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        pushAct(WeakReference(activity))
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        removeAct(WeakReference(activity))
    }


}