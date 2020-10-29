package com.app.xandone.yblogapp.api

import androidx.lifecycle.MediatorLiveData
import com.app.xandone.yblogapp.model.bean.AdminBean
import com.app.xandone.yblogapp.model.bean.ArtInfoBean
import com.app.xandone.yblogapp.rx.IRequestCallback
import io.reactivex.disposables.Disposable

/**
 * author: Admin
 * created on: 2020/9/27 11:31
 * description:
 */
interface IManager {
    fun login(
        name: String?,
        psw: String?,
        callback: IRequestCallback<AdminBean>
    ): Disposable?

    val adminLiveData: MediatorLiveData<AdminBean>
    fun getArtInfoData(
        id: String?,
        callback: IRequestCallback<ArtInfoBean>
    ): Disposable?

    val artInfoLiveData: MediatorLiveData<ArtInfoBean>
}