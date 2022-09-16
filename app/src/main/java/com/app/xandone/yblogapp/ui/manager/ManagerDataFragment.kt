package com.app.xandone.yblogapp.ui.manager

import android.content.Intent
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.app.xandone.baselib.cache.CacheHelper.clearDefaultSp
import com.app.xandone.widgetlib.dialog.MDialogOnclickListener
import com.app.xandone.widgetlib.dialog.MDialogUtils.showSimpleDialog
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.base.BaseWallFragment
import com.app.xandone.yblogapp.constant.OSpKey
import com.app.xandone.yblogapp.model.event.SwitchEvent
import com.app.xandone.yblogapp.ui.manager.chart.ChartDataActivity
import com.app.xandone.yblogapp.ui.manager.setting.SettingActivity
import org.greenrobot.eventbus.EventBus

import kotlinx.android.synthetic.main.frag_manager_data.*

/**
 * author: Admin
 * created on: 2020/9/27 11:02
 * description:
 */
class ManagerDataFragment : BaseWallFragment(), View.OnClickListener {
    private lateinit var toolbar: Toolbar

    override fun getLayout(): Int {
        return R.layout.frag_manager_data
    }

    override fun initView(view: View) {
        super.initView(view)

        toolbar = view.findViewById(R.id.toolbar)

        setToolBar("管理系统")

        setting_cl.setOnClickListener(this)
        exit_btn.setOnClickListener(this)
        chart_tip_cl.setOnClickListener(this)

        onLoadFinish()
    }

    override fun requestData() {}
    private fun setToolBar(title: CharSequence) {
        toolbar.title = title
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.chart_tip_cl -> startActivity(Intent(mActivity, ChartDataActivity::class.java))
            R.id.setting_cl -> startActivity(Intent(mActivity, SettingActivity::class.java))
            R.id.exit_btn -> exit()
            else -> {
            }
        }
    }

    private fun exit() {
        showSimpleDialog(mActivity, "是否退出登录？", object : MDialogOnclickListener() {
            override fun onConfirm() {
                EventBus.getDefault().post(SwitchEvent(SwitchEvent.MANAGER_LOGIN_RAG))
                clearDefaultSp(App.sContext!!, OSpKey.ADMIN_INFO_KEY)
            }
        })
    }
}