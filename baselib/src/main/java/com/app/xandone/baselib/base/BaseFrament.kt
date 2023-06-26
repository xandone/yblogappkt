package com.app.xandone.baselib.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import org.greenrobot.eventbus.EventBus

/**
 * author: Admin
 * created on: 2020/8/12 11:11
 * description:
 */
abstract class BaseFrament : Fragment(), IFragInit {

    protected lateinit var mActivity: FragmentActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doBeforeSetContentView()
        if (isRegistEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = requireActivity()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    /**
     * 是否注册EventBus
     */
    open fun isRegistEventBus(): Boolean {
        return false
    }
}