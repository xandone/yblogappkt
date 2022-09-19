package com.app.xandone.yblogapp.cache

import com.app.xandone.baselib.cache.SpHelper
import com.app.xandone.baselib.utils.JsonUtils.json2Obj
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.constant.OSpKey
import com.app.xandone.yblogapp.model.bean.AdminBean

/**
 * author: Admin
 * created on: 2020/10/21 17:27
 * description:
 */
object UserInfoHelper {
    val adminId: String?
        get() {
            val adminJson = SpHelper.getDefaultString(App.sContext!!, OSpKey.ADMIN_INFO_KEY)
            return json2Obj(
                adminJson,
                AdminBean::class.java
            ).adminId
        }

    val adminToken: String?
        get() {
            val adminJson = SpHelper.getDefaultString(App.sContext!!, OSpKey.ADMIN_INFO_KEY)
            if (adminJson == null || adminJson.isEmpty()) {
                return ""
            }
            return json2Obj(
                adminJson,
                AdminBean::class.java
            ).token
        }
}