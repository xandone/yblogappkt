package com.app.xandone.yblogapp.model.repository

import androidx.lifecycle.MediatorLiveData
import com.app.xandone.yblogapp.api.ApiClient
import com.app.xandone.yblogapp.api.IFetchArticle
import com.app.xandone.yblogapp.model.bean.*
import com.app.xandone.yblogapp.rx.BaseSubscriber
import com.app.xandone.yblogapp.rx.IRequestCallback
import com.app.xandone.yblogapp.rx.RxHelper
import io.reactivex.disposables.Disposable

/**
 * author: Admin
 * created on: 2020/8/12 17:19
 * description:
 */
class CodeRepository : IFetchArticle {
    override val codeArticleLiveData: MediatorLiveData<List<CodeArticleBean>> = MediatorLiveData()
    override val codeDetailsLiveData: MediatorLiveData<CodeDetailsBean> = MediatorLiveData()
    override val essayArticleLiveData: MediatorLiveData<List<EssayArticleBean>> = MediatorLiveData()
    override val essayDetailsLiveData: MediatorLiveData<EssayDetailsBean> = MediatorLiveData()
    override val bannerLiveData: MediatorLiveData<List<BannerBean>> = MediatorLiveData()
    override val codeTypeLiveData: MediatorLiveData<List<CodeTypeBean>> = MediatorLiveData()

    override fun getCodeDatas(
        page: Int,
        row: Int,
        type: Int,
        isLoadMore: Boolean,
        callback: IRequestCallback<List<CodeArticleBean>>
    ): Disposable? {
        return ApiClient.instance
            ?.apiService
            ?.getCodeDatas(page, row, type)
            ?.compose(RxHelper.handleIO())
            ?.compose<List<CodeArticleBean>>(RxHelper.handleRespose())
            ?.subscribeWith(object :
                BaseSubscriber<List<CodeArticleBean>>() {
                override fun onSuccess(articleBeans: List<CodeArticleBean>) {
                    if (codeArticleLiveData.value == null) {
                        codeArticleLiveData.value = articleBeans
                        return
                    }
                    if (!isLoadMore) {
                        codeArticleLiveData.setValue(articleBeans)
                    } else {
                        val list = mutableListOf<CodeArticleBean>()
                        list.addAll(codeArticleLiveData.value!!)
                        list.addAll(articleBeans)
                        codeArticleLiveData.setValue(list)
                    }
                }

                override fun onFail(message: String?, code: Int) {
                    super.onFail(message, code)
                    if (message != null)
                        callback.error(message, code)
                }
            })
    }

    override fun getCodeDetails(
        id: String?,
        callback: IRequestCallback<CodeDetailsBean>
    ): Disposable? {
        return ApiClient.instance
            ?.apiService
            ?.getCodeDetails(id)
            ?.compose(RxHelper.handleIO())
            ?.compose<List<CodeDetailsBean>>(RxHelper.handleRespose())
            ?.subscribeWith(object :
                BaseSubscriber<List<CodeDetailsBean>>() {
                override fun onSuccess(detailsBeans: List<CodeDetailsBean>) {
                    codeDetailsLiveData.value = detailsBeans[0]
                }

                override fun onFail(message: String?, code: Int) {
                    super.onFail(message, code)
                    if (message != null)
                        callback.error(message, code)
                }
            })
    }

    override fun getEssayDatas(
        page: Int,
        row: Int,
        isLoadMore: Boolean,
        callback: IRequestCallback<List<EssayArticleBean>>
    ): Disposable? {
        return ApiClient.instance
            ?.apiService
            ?.getEssayDatas(page, row)
            ?.compose(RxHelper.handleIO())
            ?.compose<List<EssayArticleBean>>(RxHelper.handleRespose())
            ?.subscribeWith(object :
                BaseSubscriber<List<EssayArticleBean>>() {
                override fun onSuccess(beans: List<EssayArticleBean>) {
                    if (essayArticleLiveData.value == null) {
                        essayArticleLiveData.value = beans
                        return
                    }
                    if (!isLoadMore) {
                        essayArticleLiveData.setValue(beans)
                    } else {
                        val list = mutableListOf<EssayArticleBean>()
                        list.addAll(essayArticleLiveData.value!!)
                        essayArticleLiveData.setValue(list)
                    }
                }

                override fun onFail(message: String?, code: Int) {
                    super.onFail(message, code)
                    if (message != null)
                        callback.error(message, code)
                }
            })
    }

    override fun getEssayDetails(
        id: String?,
        callback: IRequestCallback<EssayDetailsBean>
    ): Disposable? {
        return ApiClient.instance
            ?.apiService
            ?.getEssayDetails(id)
            ?.compose(RxHelper.handleIO())
            ?.compose(RxHelper.handleRespose())
            ?.subscribeWith(object :
                BaseSubscriber<List<EssayDetailsBean>>() {
                override fun onSuccess(detailsBeans: List<EssayDetailsBean>) {
                    essayDetailsLiveData.value = detailsBeans[0]
                }

                override fun onFail(message: String?, code: Int) {
                    super.onFail(message, code)
                    if (message != null)
                        callback.error(message, code)
                }
            })
    }

    override fun getBannerDatas(callback: IRequestCallback<List<BannerBean>>): Disposable? {
        return ApiClient.instance
            ?.apiService
            ?.bannerDatas
            ?.compose(RxHelper.handleIO())
            ?.compose(RxHelper.handleRespose())
            ?.subscribeWith(object : BaseSubscriber<List<BannerBean>>() {
                override fun onSuccess(beans: List<BannerBean>) {
                    bannerLiveData.value = beans
                }

                override fun onFail(message: String?, code: Int) {
                    super.onFail(message, code)
                    if (message != null)
                        callback.error(message, code)
                }
            })
    }

    override fun getCodeTypeDatas(callback: IRequestCallback<List<CodeTypeBean>>): Disposable? {
        return ApiClient.instance
            ?.apiService
            ?.codeTypeDatas
            ?.compose(RxHelper.handleIO())
            ?.compose(RxHelper.handleRespose())
            ?.subscribeWith(object :
                BaseSubscriber<List<CodeTypeBean>>() {
                override fun onSuccess(beans: List<CodeTypeBean>) {
                    codeTypeLiveData.value = beans
                }

                override fun onFail(message: String?, code: Int) {
                    super.onFail(message, code)
                    if (message != null) {
                        callback.error(message, code)
                    }
                }
            })
    }
}