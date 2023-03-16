package com.app.xandone.yblogapp.api

import com.app.xandone.yblogapp.model.bean.*
import com.app.xandone.yblogapp.model.repository.ApiResponse
import retrofit2.http.*

/**
 * author: Admin
 * created on: 2020/8/12 16:56
 * description:
 */
interface IApiService {
    @GET("art/artlist")
    suspend fun getCodeDatas(
        @Query("page") page: Int,
        @Query("row") row: Int,
        @Query("type") type: Int
    ): ApiResponse<List<CodeArticleBean>>

    @GET("art/artDetails")
    suspend fun getCodeDetails(@Query("artId") artId: String?): ApiResponse<List<CodeDetailsBean>>

    @GET("essay/essaylist")
    suspend fun getEssayDatas(
        @Query("page") page: Int,
        @Query("row") row: Int
    ): ApiResponse<List<EssayArticleBean>>

    @GET("essay/essayDetails")
    suspend fun getEssayDetails(@Query("essayId") essayId: String?): ApiResponse<List<EssayDetailsBean>>

    @GET("banner/list")
    suspend fun bannerDatas(): ApiResponse<List<BannerBean>>

    @GET("art/artTypeList")
    suspend fun codeTypeDatas(): ApiResponse<List<CodeTypeBean>>

    @FormUrlEncoded
    @POST("admin/login")
    suspend fun login(
        @Field("name") name: String?,
        @Field("psw") psw: String?
    ): ApiResponse<List<AdminBean>>

    @GET("admin/artInfo")
    suspend fun getArtInfoDatas(@Query("adminId") id: String?): ApiResponse<ArtInfoBean>
}