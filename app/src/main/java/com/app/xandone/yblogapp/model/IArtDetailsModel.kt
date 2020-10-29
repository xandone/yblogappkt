package com.app.xandone.yblogapp.model

import com.app.xandone.yblogapp.rx.IRequestCallback

/**
 * author: Admin
 * created on: 2020/9/7 10:10
 * description:
 */
interface IArtDetailsModel<T> {
    fun getDetails(id: String?, callback: IRequestCallback<T>)
}