package com.app.xandone.yblogapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import com.app.xandone.baselib.base.BaseSimpleActivity
import com.app.xandone.yblogapp.databinding.ActivityMainBinding
import com.app.xandone.yblogapp.ui.code.CodeFragment
import com.app.xandone.yblogapp.ui.essay.Essayfragment
import com.app.xandone.yblogapp.ui.manager.ManagerFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseSimpleActivity<ActivityMainBinding>() {

    private var codeFragment: CodeFragment? = null
    private var essayfragment: Essayfragment? = null
    private var managerFragment: ManagerFragment? = null

    override fun initVB(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {
        val aTest = ATest()
        lifecycle.addObserver(aTest)
        codeFragment = CodeFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_frame, codeFragment!!)
            .commitAllowingStateLoss()

        bottom_bar.setOnItemSelectedListener { item ->
            var isSelect = false
            supportFragmentManager.beginTransaction().apply {
                hideAllFragment(this)
                when (item.itemId) {
                    R.id.main_footer_code_rb -> {
                        isSelect = true
                        if (codeFragment == null) {
                            codeFragment = CodeFragment()
                            add(R.id.main_frame, codeFragment!!)
                        } else {
                            show(codeFragment!!)
                        }
                    }
                    R.id.main_footer_essay_rb -> {
                        isSelect = true
                        if (essayfragment == null) {
                            essayfragment = Essayfragment()
                            add(R.id.main_frame, essayfragment!!)
                        } else {
                            show(essayfragment!!)
                        }
                    }
                    R.id.main_footer_manager_rb -> {
                        isSelect = true
                        if (managerFragment == null) {
                            managerFragment = ManagerFragment()
                            add(R.id.main_frame, managerFragment!!)
                        } else {
                            show(managerFragment!!)
                        }
                    }
                }
            }.commitAllowingStateLoss()

            return@setOnItemSelectedListener isSelect
        }
    }


    private fun hideAllFragment(transaction: FragmentTransaction) {
        transaction.apply {
            if (codeFragment != null) hide(codeFragment!!)
            if (essayfragment != null) hide(essayfragment!!)
            if (managerFragment != null) hide(managerFragment!!)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("tagerr","$isChangingConfigurations")
    }

}