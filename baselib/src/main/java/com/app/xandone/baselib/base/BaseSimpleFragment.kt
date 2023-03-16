package com.app.xandone.baselib.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import org.greenrobot.eventbus.EventBus

/**
 * author: Admin
 * created on: 2020/8/12 11:05
 * description:
 */
abstract class BaseSimpleFragment<VB : ViewBinding> : Fragment(), IFragInit {

    protected lateinit var mActivity: FragmentActivity

    protected var _binding: VB? = null
    protected val mBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doBeforeSetContentView()
        if (isRegistEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    abstract fun initVB(): VB

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = initVB()
        return mBinding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun doBeforeSetContentView() {}

    /**
     * 是否注册EventBus
     */
    open fun isRegistEventBus(): Boolean {
        return false
    }


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