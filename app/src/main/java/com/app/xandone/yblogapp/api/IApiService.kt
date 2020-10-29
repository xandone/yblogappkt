package com.app.xandone.yblogapp.api

import com.app.xandone.yblogapp.model.base.BaseResponse
import com.app.xandone.yblogapp.model.bean.*
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * author: Admin
 * created on: 2020/8/12 16:56
 * description:
 */
interface IApiService {
    @GET("art/artlist")
    fun getCodeDatas(
        @Query("page") page: Int,
        @Query("row") row: Int,
        @Query("type") type: Int
    ): Flowable<BaseResponse<List<CodeArticleBean>>>

    @GET("art/artDetails")
    fun getCodeDetails(@Query("artId") artId: String?): Flowable<BaseResponse<List<CodeDetailsBean>>>

    @GET("essay/essaylist")
    fun getEssayDatas(
        @Query("page") page: Int,
        @Query("row") row: Int
    ): Flowable<BaseResponse<List<EssayArticleBean>>>

    @GET("essay/essayDetails")
    fun getEssayDetails(@Query("essayId") essayId: String?): Flowable<BaseResponse<List<EssayDetailsBean>>>

    @get:GET("banner/list")
    val bannerDatas: Flowable<BaseResponse<List<BannerBean>>>

    @get:GET("art/artTypeList")
    val codeTypeDatas: Flowable<BaseResponse<List<CodeTypeBean>>>

    @FormUrlEncoded
    @POST("admin/login")
    fun login(
        @Field("name") name: String?,
        @Field("psw") psw: String?
    ): Flowable<BaseResponse<List<AdminBean>>>

    @GET("admin/artInfo")
    fun getArtInfoDatas(@Query("adminId") id: String?): Flowable<BaseResponse<ArtInfoBean>>
}