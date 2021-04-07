package com.app.xandone.yblogapp.api

import androidx.lifecycle.MediatorLiveData
import com.app.xandone.yblogapp.model.base.BaseResponse
import com.app.xandone.yblogapp.model.bean.*
import com.app.xandone.yblogapp.rx.IRequestCallback
import io.reactivex.disposables.Disposable

/**
 * author: Admin
 * created on: 2020/8/12 17:59
 * description:
 */
interface IFetchArticle {
    fun getCodeDatas(
        page: Int,
        row: Int,
        type: Int,
        isLoadMore: Boolean,
        callback: IRequestCallback<List<CodeArticleBean>>
    ): Disposable?

    val codeArticleLiveData: MediatorLiveData<List<CodeArticleBean>>
    fun getCodeDetails(
        id: String?,
        callback: IRequestCallback<CodeDetailsBean>
    ): Disposable?

    val codeDetailsLiveData: MediatorLiveData<CodeDetailsBean>
    fun getEssayDatas(
        page: Int,
        row: Int,
        callback: IRequestCallback<BaseResponse<List<EssayArticleBean>>>
    ): Disposable?

    val essayArticleLiveData: MediatorLiveData<BaseResponse<List<EssayArticleBean>>>
    fun getEssayDetails(
        id: String?,
        callback: IRequestCallback<EssayDetailsBean>
    ): Disposable?

    val essayDetailsLiveData: MediatorLiveData<EssayDetailsBean>
    fun getBannerDatas(callback: IRequestCallback<List<BannerBean>>): Disposable?
    val bannerLiveData: MediatorLiveData<List<BannerBean>>
    fun getCodeTypeDatas(callback: IRequestCallback<List<CodeTypeBean>>): Disposable?
    val codeTypeLiveData: MediatorLiveData<List<CodeTypeBean>>
}