package com.app.xandone.yblogapp.model.repository

import com.app.xandone.yblogapp.api.ApiClient
import com.app.xandone.yblogapp.model.bean.AdminBean
import com.app.xandone.yblogapp.model.bean.ArtInfoBean

/**
 * author: Admin
 * created on: 2020/9/27 11:32
 * description:
 */
object ManagerRepository : BaseResository() {

    suspend fun login(
        name: String,
        psw: String
    ): ApiResponse<List<AdminBean>> {
        return excuteHttp { ApiClient.create().login(name, psw) }
    }

    suspend fun getArtInfoData(
        id: String
    ): ApiResponse<ArtInfoBean> {
        return excuteHttp { ApiClient.create().getArtInfoDatas(id) }
    }

}