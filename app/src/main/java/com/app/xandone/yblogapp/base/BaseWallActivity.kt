package com.app.xandone.yblogapp.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.app.xandone.baselib.base.BaseActivity
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.view.statelayout.StateLayout
import com.gyf.immersionbar.ImmersionBar

/**
 * author: Admin
 * created on: 2020/9/1 10:52
 * description:有加载状态页的基类Fragment
 */
abstract class BaseWallActivity<VB : ViewBinding> : BaseActivity<VB>(), ILoadingWall {

    lateinit var toolbar: Toolbar

    protected lateinit var mStateLayout: StateLayout

    @SuppressLint("InflateParams")
    override fun initContentView() {
        _binding = initVB()
        val inflater = LayoutInflater.from(this)
        val rootView = inflater.inflate(R.layout.act_base_wall, null)
        mStateLayout = rootView.findViewById(R.id.stateLayout)
        toolbar = rootView.findViewById(R.id.toolbar)
        val walFrame = rootView.findViewById<FrameLayout>(R.id.wall_frame)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val view = mBinding.root
        view.layoutParams = params
        walFrame.addView(view)
        setContentView(rootView)
    }

    @CallSuper
    override fun initView() {
        onLoading()
        initToolbar()
        initImmersionBar()
        mStateLayout.onRefresh { tag ->
            reload(tag)
        }
    }

    protected fun initToolbar() {
        setToolBar(title, R.mipmap.back_ic)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
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

    private var mImmersionBar: ImmersionBar? = null

    private fun initImmersionBar() {
        if (isStatusBarEnabled()) {
            getStatusBarConfig().init()
            ImmersionBar.setTitleBar(this, toolbar)
        }
    }

    @NonNull
    private fun getStatusBarConfig(): ImmersionBar {
        if (mImmersionBar == null) {
            mImmersionBar = statusBarConfig()
        }
        return mImmersionBar as ImmersionBar
    }


    protected open fun isStatusBarEnabled(): Boolean {
        return true
    }

    @NonNull
    protected open fun statusBarConfig(): ImmersionBar {
        return ImmersionBar.with(this)
            // 默认状态栏字体颜色为黑色
            .statusBarDarkFont(true)
//                .statusBarColor(R.color.white_color)
            // 指定导航栏背景颜色
//                .navigationBarColor(android.R.color.white)
            // 状态栏字体和导航栏内容自动变色，必须指定状态栏颜色和导航栏颜色才可以自动变色
            .autoDarkModeEnable(true, 0.2f)
    }

    /**
     * 加载数据，实现该方法
     */
    protected abstract fun requestData()
    override fun onLoading() {
        mStateLayout.showLoading(refresh = false)
    }

    override fun onLoadEmpty(tag: Any?) {
        mStateLayout.showEmpty(tag)
    }

    override fun onLoadSeverError(tag: Any?) {
        mStateLayout.showError(tag)
    }

    override fun onLoadNetError(tag: Any?) {
        mStateLayout.showError(tag)
    }

    override fun onLoadFinish(tag: Any?) {
        mStateLayout.showContent(tag)
    }


    override fun reload(tag: Any?) {

    }
}