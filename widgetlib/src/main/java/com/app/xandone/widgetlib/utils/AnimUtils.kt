package com.app.xandone.widgetlib.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

/**
 * author: Admin
 * created on: 2018/11/2 09:49
 * description:
 */
object AnimUtils {
    /**
     * 缩小
     *
     * @param view
     */
    fun zoomIn(view: View, dist: Float, scale: Float) {
        view.pivotY = view.height.toFloat()
        view.pivotX = view.width / 2.toFloat()
        val mAnimatorSet = AnimatorSet()
        val mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale)
        val mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale)
        val mAnimatorTranslateY =
            ObjectAnimator.ofFloat(view, "translationY", 0.0f, -dist)
        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX).with(mAnimatorScaleY)
        mAnimatorSet.duration = 300
        mAnimatorSet.start()
    }

    /**
     * f放大
     *
     * @param view
     */
    fun zoomOut(view: View, scale: Float) {
        if (view.translationY == 0f) {
            return
        }
        view.pivotY = view.height.toFloat()
        view.pivotX = view.width / 2.toFloat()
        val mAnimatorSet = AnimatorSet()
        val mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f)
        val mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f)
        val mAnimatorTranslateY =
            ObjectAnimator.ofFloat(view, "translationY", view.translationY, 0f)
        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX).with(mAnimatorScaleY)
        mAnimatorSet.duration = 300
        mAnimatorSet.start()
    }
}