package com.xandone.manager2.api

import com.xandone.manager2.model.bean.UntreateBean
import com.xandone.manager2.model.repository.ApiResponse
import com.xandone.manager2.model.repository.ListBean
import retrofit2.http.*

/**
 * @author: xiao
 * created on: 2023/2/14 10:58
 * description:
 */
interface ApiService {

    @POST("UnprocessedList")
    @FormUrlEncoded
    suspend fun getUntreateList(
        @Field("adminUid") adminUid: Int,
        @Field("page") page: Int,
        @Field("pagesize") pagesize: Int
    ): ApiResponse<ListBean<UntreateBean>>
}