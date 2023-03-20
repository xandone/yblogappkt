package com.app.xandone.baselib.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * author: Admin
 * created on: 2020/10/26 10:51
 * description:
 */
abstract class BaseSimpleActivity<VB : ViewBinding> : AppCompatActivity(), IActivityInit {
    protected var _binding: VB? = null
    protected val mBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        doBeforeSetContentView()
        initContentView()
        initView()
    }

    protected open fun initContentView(){
        _binding = initVB()
        setContentView(mBinding.root)
    }


    override fun doBeforeSetContentView() {

    }

    protected open fun startActivity(activity: Activity) {
//        startActivity(Intent(this, activity::class.java))
    }

    protected abstract fun initVB(): VB

    override fun onDestroy() {
        super.onDestroy()
        if (_binding != null) {
            _binding = null
        }
    }

}