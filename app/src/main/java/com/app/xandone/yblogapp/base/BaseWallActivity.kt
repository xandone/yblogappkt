package com.app.xandone.yblogapp.base

import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.annotation.NonNull
import androidx.viewbinding.ViewBinding
import com.app.xandone.baselib.base.BaseActivity
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.databinding.ActBaseWallBinding
import com.gyf.immersionbar.ImmersionBar

/**
 * author: Admin
 * created on: 2020/9/1 10:52
 * description:有加载状态页的基类Fragment
 */
abstract class BaseWallActivity<VB : ViewBinding>(val initVb: (LayoutInflater) -> VB) :
    BaseActivity(), ILoadingWall {

    private var _wallBinding: ActBaseWallBinding? = null
    protected val mWallABinding
        get() = _wallBinding!!

    private var _binding: VB? = null
    protected val mBinding
        get() = _binding!!

    override fun doBeforeSetContentView() {

    }

    override fun initContentView() {
        _wallBinding = ActBaseWallBinding.inflate(layoutInflater)
        _binding = initVb(layoutInflater)

        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        mBinding.root.layoutParams = params
        mWallABinding.wallFrame.addView(mBinding.root)

        setContentView(mWallABinding.root)
    }

    @CallSuper
    override fun initView() {
        onLoading()
        initToolbar()
        initImmersionBar()
        mWallABinding.stateLayout.onRefresh { tag ->
            reload(tag)
        }
    }

    protected fun initToolbar() {
        setToolBar(title, R.mipmap.back_ic)
        setSupportActionBar(mWallABinding.vToolBar.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        mWallABinding.vToolBar.toolbar.setNavigationOnClickListener { finish() }
    }

    fun setToolBar(title: CharSequence?, icon: Int) {
        setToolBar(title)
        mWallABinding.vToolBar.toolbar.setNavigationIcon(icon)
    }

    fun setToolBar(title: CharSequence?) {
        mWallABinding.vToolBar.toolbar.title = title
        mWallABinding.vToolBar.toolbar.setNavigationIcon(R.mipmap.back_ic)
    }

    private var mImmersionBar: ImmersionBar? = null

    private fun initImmersionBar() {
        if (isStatusBarEnabled()) {
            getStatusBarConfig().init()
            ImmersionBar.setTitleBar(this, mWallABinding.vToolBar.toolbar)
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
        mWallABinding.stateLayout.showLoading(refresh = false)
    }

    override fun onLoadEmpty(tag: Any?) {
        mWallABinding.stateLayout.showEmpty(tag)
    }

    override fun onLoadSeverError(tag: Any?) {
        mWallABinding.stateLayout.showError(tag)
    }

    override fun onLoadNetError(tag: Any?) {
        mWallABinding.stateLayout.showError(tag)
    }

    override fun onLoadFinish(tag: Any?) {
        mWallABinding.stateLayout.showContent(tag)
    }


    override fun reload(tag: Any?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        if (_wallBinding != null) {
            _wallBinding = null
        }
        if (_binding != null) {
            _binding = null
        }
    }
}