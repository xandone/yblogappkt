package com.app.xandone.widgetlib.utils

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import java.util.*

/**
 * copy https://github.com/wenzhihao123/Android-loginsmooth-master
 */
class KeyboardWatcher @JvmOverloads constructor(
    private val activityRootView: View,
    var isSoftKeyboardOpened: Boolean = false) : OnGlobalLayoutListener {
    interface SoftKeyboardStateListener {
        fun onSoftKeyboardOpened(keyboardHeightInPx: Int)
        fun onSoftKeyboardClosed()
    }

    private val listeners: MutableList<SoftKeyboardStateListener> =
        LinkedList()

    /**
     * Default value is zero `0`.
     *
     * @return last saved keyboard height in px
     */
    var lastSoftKeyboardHeightInPx = 0
        private set
    private var statusBarHeight = -1
    fun isFullScreen(activity: Activity): Boolean {
        return activity.window.attributes.flags and
                WindowManager.LayoutParams.FLAG_FULLSCREEN == WindowManager.LayoutParams.FLAG_FULLSCREEN
    }

    override fun onGlobalLayout() {
        val r = Rect()
        //r will be populated with the coordinates of your view that area still visible.
        activityRootView.getWindowVisibleDisplayFrame(r)
        val heightDiff = activityRootView.rootView.height - (r.bottom - r.top)
        if (!isSoftKeyboardOpened && heightDiff > activityRootView.rootView.height / 4) {
            isSoftKeyboardOpened = true
            if (activityRootView.context is Activity
                && !isFullScreen(activityRootView.context as Activity)
            ) {
                notifyOnSoftKeyboardOpened(heightDiff - statusBarHeight)
            } else {
                notifyOnSoftKeyboardOpened(heightDiff)
            }
        } else if (isSoftKeyboardOpened && heightDiff < activityRootView.rootView
                .height / 4
        ) {
            isSoftKeyboardOpened = false
            notifyOnSoftKeyboardClosed()
        }
    }

    fun addSoftKeyboardStateListener(listener: SoftKeyboardStateListener) {
        listeners.add(listener)
    }

    fun removeSoftKeyboardStateListener(listener: SoftKeyboardStateListener?) {
        listeners.remove(listener)
    }

    private fun notifyOnSoftKeyboardOpened(keyboardHeightInPx: Int) {
        lastSoftKeyboardHeightInPx = keyboardHeightInPx
        for (listener in listeners) {
            listener?.onSoftKeyboardOpened(keyboardHeightInPx)
        }
    }

    private fun notifyOnSoftKeyboardClosed() {
        for (listener in listeners) {
            listener?.onSoftKeyboardClosed()
        }
    }

    init {
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener(this)
        //获取status_bar_height资源的ID
        val resourceId = activityRootView.context.resources
            .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight =
                activityRootView.context.resources.getDimensionPixelSize(resourceId)
        }
    }
}