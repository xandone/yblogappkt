package com.app.xandone.yblogapp.model.repository

import com.app.xandone.yblogapp.api.ApiClient
import com.app.xandone.yblogapp.model.bean.*

/**
 * author: Admin
 * created on: 2020/8/12 17:19
 * description:
 */
object CodeRepository : BaseResository() {

    suspend fun getCodeDatas(
        page: Int,
        row: Int,
        type: Int,
    ): ApiResponse<List<CodeArticleBean>> {
        return excuteHttp { ApiClient.create().getCodeDatas(page, row, type) }
    }

    suspend fun getCodeDetails(
        id: String?
    ): ApiResponse<List<CodeDetailsBean>> {
        return excuteHttp { ApiClient.create().getCodeDetails(id) }
    }

    suspend fun getEssayDatas(
        page: Int,
        row: Int,
    ): ApiResponse<List<EssayArticleBean>> {
        return excuteHttp { ApiClient.create().getEssayDatas(page, row) }
    }

    suspend fun getEssayDetails(
        id: String?
    ): ApiResponse<List<EssayDetailsBean>> {
        return excuteHttp { ApiClient.create().getEssayDetails(id) }
    }


    suspend fun getBannerDatas(): ApiResponse<List<BannerBean>> {
        return excuteHttp { ApiClient.create().bannerDatas() }
    }

    suspend fun getCodeTypeDatas(): ApiResponse<List<CodeTypeBean>> {
        return excuteHttp { ApiClient.create().codeTypeDatas() }
    }

}