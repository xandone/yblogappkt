package com.app.xandone.yblogapp.rx

import io.reactivex.disposables.Disposable

/**
 * author: Admin
 * created on: 2020/9/8 15:31
 * description:
 */
interface IManagerDisposable {
    fun addSubscrible(disposable: Disposable?)
    fun clearSubscrible()
}