package com.app.xandone.yblogapp.model.bean

import java.io.Serializable

/**
 * author: Admin
 * created on: 2020/10/26 11:14
 * description:
 */
class AdminBean : Serializable {
    /**
     * name : admin
     * password : 0ac8d0d345095205711dacf42de37436
     * nickname : Admin
     * adminId : 250
     * adminIcon : null
     * token : null
     * permission : null
     * totalArts : 0
     * email : 765478955@qq.com
     * registTime : 2019-12-13 14:46:50
     * lastLoginTime : 2020-09-27 16:11:25
     */
    var name: String? = null
    var password: String? = null
    var nickname: String? = null
    var adminId: String? = null
    var adminIcon: String? = null
    var permission: String? = null
    var totalArts = 0
    var email: String? = null
    var registTime: String? = null
    var lastLoginTime: String? = null
    var token: String? = ""

}