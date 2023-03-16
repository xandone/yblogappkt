package com.app.xandone.yblogapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import com.app.xandone.baselib.base.BaseFrament
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.view.statelayout.StateLayout


/**
 * author: Admin
 * created on: 2020/9/1 10:52
 * description:有加载状态页的基类Fragment
 */
abstract class BaseWallFragment<VB : ViewBinding> : BaseFrament<VB>() {

    protected lateinit var mStateLayout: StateLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = initVB()
        mStateLayout = inflater.inflate(R.layout.frag_base_wall, container, false) as StateLayout
        val walFrame = mStateLayout.findViewById<FrameLayout>(R.id.wall_frame)
        val v = mBinding.root
        val param = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT)
        v.layoutParams = param
        walFrame.addView(v)
        return mStateLayout
    }
}