package com.app.xandone.yblogapp.ui.manager.setting

import com.app.xandone.baselib.base.setClickAction
import com.app.xandone.baselib.cache.CacheHelper.clearDefaultSp
import com.app.xandone.baselib.cache.CacheHelper.clearExternalFilesDir
import com.app.xandone.baselib.cache.CacheHelper.getTotalCacheSize
import com.app.xandone.baselib.utils.ToastUtils.showShort
import com.app.xandone.widgetlib.dialog.MDialogOnclickListener
import com.app.xandone.widgetlib.dialog.MDialogUtils.showSimpleDialog
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.base.BaseWallActivity
import com.app.xandone.yblogapp.constant.OSpKey
import com.app.xandone.yblogapp.databinding.ActSettingBinding
import com.app.xandone.yblogapp.model.event.SwitchEvent
import org.greenrobot.eventbus.EventBus

/**
 * author: Admin
 * created on: 2020/9/29 11:09
 * description:
 */
class SettingActivity : BaseWallActivity<ActSettingBinding>(ActSettingBinding::inflate) {

    override fun initView() {
        super.initView()
        mBinding.allCacheSv.setSettingRightTv(getTotalCacheSize(App.sContext))

        setClickAction(
            mBinding.settingSv,
            mBinding.allCacheSv,
            mBinding.versionSv,
            mBinding.exitBtn
        ) {
            when (this) {
                mBinding.settingSv -> clearSettingInfo()
                mBinding.allCacheSv -> clearAllCache()
                mBinding.exitBtn -> exit()
            }
        }


        onLoadFinish()
    }

    override fun requestData() {}

    private fun clearSettingInfo() {
        showSimpleDialog(this, "是否清除配置信息?", object : MDialogOnclickListener() {
            override fun onConfirm() {
                clearDefaultSp(App.sContext, OSpKey.CODE_TYPE_KEY)
                showShort("清除完成")
            }
        })
    }

    private fun clearAllCache() {
        showSimpleDialog(this, "是否清除所有缓存文件?", object : MDialogOnclickListener() {
            override fun onConfirm() {
                clearExternalFilesDir(App.sContext)
                mBinding.allCacheSv.setSettingRightTv("0KB")
                showShort("清除完成")
            }
        })
    }

    private fun exit() {
        showSimpleDialog(this, "是否退出?", object : MDialogOnclickListener() {
            override fun onConfirm() {
                EventBus.getDefault().post(SwitchEvent(SwitchEvent.MANAGER_LOGIN_RAG))
                clearDefaultSp(App.sContext, OSpKey.ADMIN_INFO_KEY)
                finish()
            }

        })
    }
}