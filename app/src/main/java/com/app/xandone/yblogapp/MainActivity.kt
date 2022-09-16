package com.app.xandone.yblogapp

import androidx.fragment.app.Fragment
import com.app.xandone.baselib.base.BaseSimpleActivity
import com.app.xandone.yblogapp.ui.code.CodeFragment
import com.app.xandone.yblogapp.ui.essay.Essayfragment
import com.app.xandone.yblogapp.ui.manager.ManagerFragment
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseSimpleActivity() {
    private lateinit var fragments: ArrayList<Fragment>
    private var mCurrentFragment: Fragment? = null
    private var mCurrentIndex = 0
    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
//        val a = ATest()
//        lifecycle.addObserver(a)
        fragments = ArrayList()
        fragments.add(CodeFragment())
        fragments.add(Essayfragment.instance)
        fragments.add(ManagerFragment())
        bottom_bar.setOnTabSelectListener { tabId ->
            when (tabId) {
                R.id.main_footer_code_rb -> mCurrentIndex = 0
                R.id.main_footer_essay_rb -> mCurrentIndex = 1
                R.id.main_footer_manager_rb -> mCurrentIndex = 2
                else -> {
                }
            }
            turn2Fragment(mCurrentIndex)
        }
    }

    fun turn2Fragment(index: Int) {
        val toFragment = fragments[index]
        val ft = supportFragmentManager.beginTransaction()
        if (mCurrentFragment != null) {
            ft.hide(mCurrentFragment!!)
        }
        if (toFragment.isAdded) {
            ft.show(toFragment)
        } else {
            ft.add(R.id.main_frame, toFragment)
        }
        ft.commitAllowingStateLoss()
        mCurrentFragment = toFragment
    }
}