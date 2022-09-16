package com.app.xandone.baselib.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.app.xandone.baselib.event.SimplEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author: Admin
 * created on: 2020/8/12 11:05
 * description:
 */
abstract class BaseSimpleFragment : Fragment(), IFragInit {

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
        mActivity = context as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false)
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
        super.onDestroyView()
    }
}