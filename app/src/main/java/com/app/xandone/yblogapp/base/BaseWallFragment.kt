package com.app.xandone.yblogapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import com.app.xandone.baselib.base.BaseFrament
import com.app.xandone.widgetlib.view.LoadingLayout
import com.app.xandone.widgetlib.view.LoadingLayout.OnReloadListener
import com.app.xandone.yblogapp.R

import kotlinx.android.synthetic.main.frag_base_wall.*

/**
 * author: Admin
 * created on: 2020/9/1 10:52
 * description:有加载状态页的基类Fragment
 */
abstract class BaseWallFragment : BaseFrament(), ILoadingWall, OnReloadListener {

    private lateinit var loadLayout: LoadingLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.frag_base_wall, container, false) as ViewGroup
        val walFrame = rootView.findViewById<FrameLayout>(R.id.wall_frame)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val view = inflater.inflate(getLayout(), null)
        view.layoutParams = params
        walFrame.addView(view)
        return rootView
    }

    @CallSuper
    override fun initView(view: View) {
        loadLayout = view.findViewById(R.id.loadLayout)
        loadLayout.setOnReloadListener(this)
        onLoading()
    }

    /**
     * 重新加载按钮
     */
    override fun reLoadData() {
        onLoading()
        requestData()
    }

    /**
     * 加载数据，实现该方法
     */
    protected abstract fun requestData()
    override fun onLoading() {
        loadLayout.setLoadingStatus(LoadingLayout.ILoadingStatus.LOADING)
    }

    override fun onLoadEmpty() {
        loadLayout.setLoadingStatus(LoadingLayout.ILoadingStatus.EMPTY)
    }

    override fun onLoadSeverError() {
        loadLayout.setLoadingStatus(LoadingLayout.ILoadingStatus.SERVER_ERROR)
    }

    override fun onLoadNetError() {
        loadLayout.setLoadingStatus(LoadingLayout.ILoadingStatus.NET_ERROR)
    }

    override fun onLoadFinish() {
        loadLayout.setLoadingStatus(LoadingLayout.ILoadingStatus.FINISH)
    }

    override fun onLoadStatus(statusCode: Int) {
        loadLayout.setLoadingStatus(statusCode)
    }
}