package com.app.xandone.yblogapp.ui.manager

import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import com.app.xandone.baselib.cache.SpHelper.getDefaultString
import com.app.xandone.baselib.utils.JsonUtils.json2Obj
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.base.BaseWallFragment
import com.app.xandone.yblogapp.constant.OSpKey
import com.app.xandone.yblogapp.databinding.FragManagerBinding
import com.app.xandone.yblogapp.model.bean.AdminBean
import com.app.xandone.yblogapp.model.event.SwitchEvent
import com.gyf.immersionbar.ImmersionBar
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author: Admin
 * created on: 2020/9/8 11:53
 * description:
 */
class ManagerFragment : BaseWallFragment<FragManagerBinding>(FragManagerBinding::inflate) {

    companion object {
        val instance = ManagerFragment()
    }

    override fun initView(view: View) {
        autoCheckAdminInfo()
    }

    /**
     * 自动检测缓存的Admin信息
     */
    private fun autoCheckAdminInfo() {
        val adminJson = getDefaultString(App.sContext, OSpKey.ADMIN_INFO_KEY)
        if (TextUtils.isEmpty(adminJson)) {
            showInitView()
            return
        }
        val adminBean = json2Obj(adminJson, AdminBean::class.java)
        if (adminBean?.adminId != null) {
            switchFragment(ManagerDataFragment())
        }
    }

    private fun showInitView() {
        childFragmentManager.beginTransaction()
            .replace(R.id.main_fl, ManagerLoginFragment())
            .commitAllowingStateLoss()
    }

    private fun switchFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.main_fl, fragment)
            .commitAllowingStateLoss()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun switchEvent(event: SwitchEvent) {
        val fragment: Fragment = when (event.tag) {
            SwitchEvent.MANAGER_DATA_RAG -> ManagerDataFragment()
            else -> ManagerLoginFragment()
        }
        switchFragment(fragment)
    }

    override fun isRegistEventBus(): Boolean {
        return true
    }

    override fun isStatusBarEnabled(): Boolean {
        return true
    }

    override fun getTitleView(): View {
        return mBinding.mainFl
    }

}