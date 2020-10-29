package com.app.xandone.baselib.base

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.app.xandone.baselib.R


/**
 * author: Admin
 * created on: 2020/10/26 10:50
 * description:
 */
abstract class BaseActivity : BaseSimpleActivity() {

    override fun initContentView() {
        val inflater = LayoutInflater.from(this)
        val rootView = inflater.inflate(R.layout.base_toolbar_layout, null) as ViewGroup
        val content = rootView.findViewById<FrameLayout>(R.id.content)
        val view = inflater.inflate(getLayout(), null)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        view.layoutParams = params
        content.addView(view)
        setContentView(rootView)
    }
}