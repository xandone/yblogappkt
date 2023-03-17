package com.app.xandone.yblogapp.ui.manager

import android.content.Intent
import android.view.View
import com.app.xandone.baselib.base.setClickAction
import com.app.xandone.baselib.imageload.ImageLoadHelper
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.base.BaseWallFragment
import com.app.xandone.yblogapp.cache.UserInfoHelper
import com.app.xandone.yblogapp.databinding.FragManagerDataBinding
import com.app.xandone.yblogapp.ui.manager.chart.ChartDataActivity
import com.app.xandone.yblogapp.ui.manager.setting.SettingActivity


/**
 * author: Admin
 * created on: 2020/9/27 11:02
 * description:
 */
class ManagerDataFragment : BaseWallFragment<FragManagerDataBinding>() {

    override fun initView(view: View) {
        val adminBean = UserInfoHelper.adminBean
        adminBean?.let {
            ImageLoadHelper.instance.display(mActivity, it.adminIcon, mBinding.adminIconUv)
            mBinding.adminNameTv.text=it.nickname
            mBinding.adminIdentityTv.text=it.identity
            mBinding.adminLastLoginTv.text=it.lastLoginTime
        }

        setClickAction(mBinding.settingCl, mBinding.chartTipCl) {
            when (this) {
                mBinding.chartTipCl -> startActivity(Intent(mActivity,
                    ChartDataActivity::class.java))
                mBinding.settingCl -> startActivity(Intent(mActivity, SettingActivity::class.java))
                else -> {
                }
            }
        }

    }

    override fun initVB(): FragManagerDataBinding {
        return FragManagerDataBinding.inflate(layoutInflater)
    }
}