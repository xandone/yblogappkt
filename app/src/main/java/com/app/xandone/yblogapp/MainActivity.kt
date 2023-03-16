package com.app.xandone.yblogapp

import androidx.fragment.app.FragmentTransaction
import com.app.xandone.baselib.base.BaseSimpleActivity
import com.app.xandone.yblogapp.ui.code.CodeFragment
import com.app.xandone.yblogapp.ui.essay.Essayfragment
import com.app.xandone.yblogapp.ui.manager.ManagerFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseSimpleActivity() {


    private val codeFragment by lazy {
        CodeFragment.instance
    }

    private val essayfragment by lazy {
        Essayfragment.instance
    }


    private val managerFragment by lazy {
        ManagerFragment.instance
    }


    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_frame, codeFragment)
            .commitAllowingStateLoss()


        bottom_bar.setOnItemSelectedListener { item ->
            var isSelect = false
            supportFragmentManager.beginTransaction().apply {
                hideAllFragment(this)
                when (item.itemId) {
                    R.id.main_footer_code_rb -> {
                        isSelect = true
                        if (codeFragment.isAdded) {
                            show(codeFragment)
                        } else {
                            add(R.id.main_frame, codeFragment)
                        }
                    }
                    R.id.main_footer_essay_rb -> {
                        isSelect = true
                        if (essayfragment.isAdded) {
                            show(essayfragment)
                        } else {
                            add(R.id.main_frame, essayfragment)
                        }
                    }
                    R.id.main_footer_manager_rb -> {
                        isSelect = true
                        if (managerFragment.isAdded) {
                            show(managerFragment)
                        } else {
                            add(R.id.main_frame, managerFragment)
                        }
                    }
                }
            }.commitAllowingStateLoss()

            return@setOnItemSelectedListener isSelect
        }
    }


    private fun hideAllFragment(transaction: FragmentTransaction) {
        transaction.apply {
            if (codeFragment.isAdded) hide(codeFragment)
            if (codeFragment.isAdded) hide(essayfragment)
            if (codeFragment.isAdded) hide(managerFragment)
        }

    }
}