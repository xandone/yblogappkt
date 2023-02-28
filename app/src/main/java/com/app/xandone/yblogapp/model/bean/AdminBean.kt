package com.app.xandone.yblogapp.model.bean

import java.io.Serializable

/**
 * author: Admin
 * created on: 2020/10/26 11:14
 * description:
 */
data class AdminBean(val name: String? = null,
                     val password: String? = null,
                     val nickname: String? = null,
                     val adminId: String? = null,
                     val adminIcon: String? = null,
                     val permission: String? = null,
                     val totalArts: Int = 0,
                     val email: String? = null,
                     val registTime: String? = null,
                     val lastLoginTime: String? = null,
                     val token: String = "") : Serializable