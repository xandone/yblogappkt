package com.app.xandone.yblogapp.ui.manager.setting

import android.view.View
import com.app.xandone.baselib.base.setClickAction
import com.app.xandone.baselib.cache.CacheHelper
import com.app.xandone.baselib.cache.CacheHelper.clearDefaultSp
import com.app.xandone.baselib.cache.CacheHelper.clearExternalFilesDir
import com.app.xandone.baselib.cache.CacheHelper.getTotalCacheSize
import com.app.xandone.baselib.utils.ToastUtils.showShort
import com.app.xandone.widgetlib.dialog.MDialogOnclickListener
import com.app.xandone.widgetlib.dialog.MDialogUtils.showSimpleDialog
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.base.BaseWallActivity
import com.app.xandone.yblogapp.constant.OSpKey
import com.app.xandone.yblogapp.model.event.SwitchEvent
import kotlinx.android.synthetic.main.act_setting.*
import org.greenrobot.eventbus.EventBus

/**
 * author: Admin
 * created on: 2020/9/29 11:09
 * description:
 */
class SettingActivity : BaseWallActivity() {
    override fun getLayout(): Int {
        return R.layout.act_setting
    }

    public override fun wallInit() {
        all_cache_sv.setSettingRightTv(getTotalCacheSize(App.sContext))

        setClickAction(setting_sv, all_cache_sv, version_sv, exit_btn) {
            when (this) {
                setting_sv -> clearSettingInfo()
                all_cache_sv -> clearAllCache()
                exit_btn -> exit()
            }
        }


        onLoadFinish()
    }

    override fun requestData() {}

    private fun clearSettingInfo() {
        showSimpleDialog(this, "是否清除配置信息?", object : MDialogOnclickListener() {
            override fun onConfirm() {
                clearDefaultSp(App.sContext!!, OSpKey.CODE_TYPE_KEY)
                showShort("清除完成")
            }
        })
    }

    private fun clearAllCache() {
        showSimpleDialog(this, "是否清除所有缓存文件?", object : MDialogOnclickListener() {
            override fun onConfirm() {
                clearExternalFilesDir(App.sContext!!)
                all_cache_sv.setSettingRightTv("0KB")
                showShort("清除完成")
            }
        })
    }

    private fun exit() {
        showSimpleDialog(this, "是否退出?", object : MDialogOnclickListener() {
            override fun onConfirm() {
                EventBus.getDefault().post(SwitchEvent(SwitchEvent.MANAGER_LOGIN_RAG))
                clearDefaultSp(App.sContext, OSpKey.ADMIN_INFO_KEY);
                finish()
            }

        })
    }
}