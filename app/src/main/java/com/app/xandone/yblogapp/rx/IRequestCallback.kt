package com.app.xandone.yblogapp.rx

/**
 * author: Admin
 * created on: 2020/9/2 10:07
 * description:
 */
interface IRequestCallback<T> {
    fun success(t: T)
    fun error(message: String?, statusCode: Int)
}