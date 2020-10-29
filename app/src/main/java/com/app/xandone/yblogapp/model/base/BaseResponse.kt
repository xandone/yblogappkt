package com.app.xandone.yblogapp.model.base

/**
 * author: Admin
 * created on: 2020/8/13 15:49
 * description:
 */
class BaseResponse<T> {
    var code = 0
    var data: T? = null
    var msg: String? = null
    var total = 0

}