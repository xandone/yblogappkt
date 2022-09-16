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
import com.app.xandone.yblogapp.model.bean.AdminBean
import com.app.xandone.yblogapp.model.event.SwitchEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author: Admin
 * created on: 2020/9/8 11:53
 * description:
 */
class ManagerFragment : BaseWallFragment() {
    override fun getLayout(): Int {
        return R.layout.frag_manager
    }

    override fun initView(view: View) {
        super.initView(view)
        autoCheckAdminInfo()
        onLoadFinish()
    }

    override fun requestData() {}

    /**
     * 自动检测缓存的Admin信息
     */
    private fun autoCheckAdminInfo() {
        val adminJson = getDefaultString(App.sContext!!, OSpKey.ADMIN_INFO_KEY)
        if (TextUtils.isEmpty(adminJson)) {
            showInitView()
            return
        }
        val adminBean = json2Obj(adminJson, AdminBean::class.java)
        if (adminBean.adminId != null) {
            switchFragment(ManagerDataFragment())
        }
    }

    private fun showInitView() {
        childFragmentManager.beginTransaction()
            .replace(R.id.main_fl, ManagerLoginFragment())
            .commit()
    }

    private fun switchFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.main_fl, fragment)
            .commit()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun switchEvent(event: SwitchEvent) {
        val fragment: Fragment
        fragment = when (event.tag) {
            SwitchEvent.MANAGER_DATA_RAG -> ManagerDataFragment()
            else -> ManagerLoginFragment()
        }
        switchFragment(fragment)
    }

    override fun isRegistEventBus(): Boolean {
        return true
    }
}