package com.app.xandone.yblogapp.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import com.app.xandone.baselib.base.BaseActivity
import com.app.xandone.widgetlib.view.LoadingLayout
import com.app.xandone.widgetlib.view.LoadingLayout.OnReloadListener
import com.app.xandone.yblogapp.R
import kotlinx.android.synthetic.main.act_base_wall.*
import java.util.*

/**
 * author: Admin
 * created on: 2020/9/1 10:52
 * description:有加载状态页的基类Fragment
 */
abstract class BaseWallActivity : BaseActivity(), ILoadingWall, OnReloadListener {
    lateinit var toolbar: Toolbar
    @SuppressLint("InflateParams")
    override fun initContentView() {
        val inflater = LayoutInflater.from(this)
        val rootView = inflater.inflate(R.layout.act_base_wall, null)
        toolbar = rootView.findViewById(R.id.toolbar)
        val walFrame = rootView.findViewById<FrameLayout>(R.id.wall_frame)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val view = inflater.inflate(getLayout(), null)
        view.layoutParams = params
        walFrame.addView(view)
        setContentView(rootView)
    }

    override fun initView() {
        loadLayout!!.setOnReloadListener(this)
        onLoading()
        initToolbar()
        wallInit()
    }

    protected fun initToolbar() {
        setToolBar(title, R.mipmap.back_ic)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
    }

    fun setToolBar(title: CharSequence?, icon: Int) {
        setToolBar(title)
        toolbar.setNavigationIcon(icon)
    }

    fun setToolBar(title: CharSequence?) {
        toolbar.title = title
        toolbar.setNavigationIcon(R.mipmap.back_ic)
    }

    /**
     * 重新加载按钮
     */
    override fun reLoadData() {
        onLoading()
        requestData()
    }

    /**
     * 初始化，实现该方法
     */
    protected abstract fun wallInit()

    /**
     * 加载数据，实现该方法
     */
    protected abstract fun requestData()
    override fun onLoading() {
        loadLayout!!.setLoadingStatus(LoadingLayout.ILoadingStatus.LOADING)
    }

    override fun onLoadEmpty(tag: Any? ) {
        loadLayout!!.setLoadingStatus(LoadingLayout.ILoadingStatus.EMPTY)
    }

    override fun onLoadSeverError(tag: Any?) {
        loadLayout!!.setLoadingStatus(LoadingLayout.ILoadingStatus.SERVER_ERROR)
    }

    override fun onLoadNetError(tag: Any?) {
        loadLayout!!.setLoadingStatus(LoadingLayout.ILoadingStatus.NET_ERROR)
    }

    override fun onLoadFinish(tag: Any?) {
        loadLayout!!.setLoadingStatus(LoadingLayout.ILoadingStatus.FINISH)
    }


    override fun reload(tag: Any?) {
        TODO("Not yet implemented")
    }
}