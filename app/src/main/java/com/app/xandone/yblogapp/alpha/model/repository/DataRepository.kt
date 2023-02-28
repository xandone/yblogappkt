package com.xandone.manager2.model.repository

import com.xandone.manager2.api.ApiClient
import com.xandone.manager2.model.bean.UntreateBean

/**
 * @author: xiao
 * created on: 2023/2/14 15:01
 * description:
 */
object DataRepository : BaseResository() {

    suspend fun getUntreateList(
        adminUid: Int,
        page: Int,
        pagesize: Int
    ): ApiResponse<ListBean<UntreateBean>> {
        return excuteHttp {
            ApiClient.create().getUntreateList(adminUid, page, pagesize)
        }
    }
}