package com.app.xandone.yblogapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.app.xandone.baselib.base.BaseFrament
import org.greenrobot.eventbus.EventBus

/**
 * author: Admin
 * created on: 2020/8/12 11:05
 * description:
 */
abstract class BaseSimpleFragment<VB : ViewBinding>(private val initVb: (LayoutInflater) -> VB) :
    BaseFrament() {

    private var _binding: VB? = null
    protected val mBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = initVb(layoutInflater)
        return mBinding.root
    }

    override fun doBeforeSetContentView() {}

    override fun onDestroyView() {
        if (isRegistEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        if (_binding != null) {
            _binding = null
        }
        super.onDestroyView()
    }
}