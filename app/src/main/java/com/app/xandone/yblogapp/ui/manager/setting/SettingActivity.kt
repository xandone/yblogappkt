package com.app.xandone.yblogapp.ui.manager.setting

import android.view.View
import butterknife.OnClick
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
import kotlinx.android.synthetic.main.act_setting.*
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
        try {
            all_cache_size_tv.text = getTotalCacheSize(App.sContext!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        onLoadFinish()
    }

    override fun requestData() {}

    @OnClick(R.id.clear_setting_cl, R.id.clear_all_cache_cl)
    fun click(v: View) {
        when (v.id) {
            R.id.clear_setting_cl -> clearSettingInfo()
            R.id.clear_all_cache_cl -> clearAllCache()
            else -> {
            }
        }
    }

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
                all_cache_size_tv.text = "0KB"
                showShort("清除完成")
            }
        })
    }
}