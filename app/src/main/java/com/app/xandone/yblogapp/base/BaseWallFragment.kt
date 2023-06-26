package com.app.xandone.yblogapp.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.viewbinding.ViewBinding
import com.app.xandone.baselib.base.BaseFrament
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.api.WDns
import com.app.xandone.yblogapp.databinding.FragBaseWallBinding
import com.app.xandone.yblogapp.view.statelayout.StateLayout
import com.gyf.immersionbar.ImmersionBar


/**
 * author: Admin
 * created on: 2020/9/1 10:52
 * description:有加载状态页的Fragment基类
 */
abstract class BaseWallFragment<VB : ViewBinding>(private val initVb: (LayoutInflater) -> VB) :
    BaseFrament(), ILoadingWall {

    private var _wallBinding: FragBaseWallBinding? = null
    protected val mWallBinding
        get() = _wallBinding!!

    private var _binding: VB? = null
    protected val mBinding
        get() = _binding!!

    override fun doBeforeSetContentView() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _wallBinding = FragBaseWallBinding.inflate(layoutInflater)
        _binding = initVb(layoutInflater)

        val param = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        mBinding.root.layoutParams = param
        mWallBinding.wallFrame.addView(mBinding.root)
        return mWallBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mWallBinding.stateLayout.onRefresh { tag ->
            reload(tag)
        }
        initImmersionBar()
    }


    protected open fun getTitleView(): View? {
        return null
    }

    protected var mImmersionBar: ImmersionBar? = null

    private fun initImmersionBar() {
        if (isStatusBarEnabled()) {
            getStatusBarConfig().init()

            getTitleView()?.let {
                ImmersionBar.setTitleBar(this, getTitleView())
            }
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
        return false
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

    override fun onLoading() {
        mWallBinding.stateLayout.showLoading(refresh = false)
    }

    override fun onLoadEmpty(tag: Any?) {
        mWallBinding.stateLayout.showEmpty(tag)
    }

    override fun onLoadSeverError(tag: Any?) {
        mWallBinding.stateLayout.showError(tag)
    }

    override fun onLoadNetError(tag: Any?) {
        mWallBinding.stateLayout.showError(tag)
    }

    override fun onLoadFinish(tag: Any?) {
        mWallBinding.stateLayout.showContent(tag)
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