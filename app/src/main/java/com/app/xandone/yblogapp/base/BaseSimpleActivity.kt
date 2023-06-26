package com.app.xandone.yblogapp.base

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.app.xandone.baselib.base.BaseActivity

/**
 * author: Admin
 * created on: 2020/10/26 10:51
 * description:
 */
abstract class BaseSimpleActivity<VB : ViewBinding>(val initVb: (LayoutInflater) -> VB) :
    BaseActivity() {
    private var _binding: VB? = null
    protected val mBinding
        get() = _binding!!


    override fun doBeforeSetContentView() {

    }

    override fun initContentView() {
        _binding = initVb(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (_binding != null) {
            _binding = null
        }
    }

}