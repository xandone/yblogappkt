package com.app.xandone.yblogapp.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.app.xandone.yblogapp.api.IFetchArticle
import com.app.xandone.yblogapp.model.bean.BannerBean
import com.app.xandone.yblogapp.model.bean.EssayArticleBean
import com.app.xandone.yblogapp.model.repository.CodeRepository
import com.app.xandone.yblogapp.rx.IRequestCallback
import com.app.xandone.yblogapp.viewmodel.BaseViewModel

/**
 * author: Admin
 * created on: 2020/9/6 21:29
 * description:
 */
class EssayModel : BaseViewModel() {
    private var articleRepo: IFetchArticle? = null
    private lateinit var callback: IRequestCallback<List<EssayArticleBean>>
    private lateinit var bannerCallback: IRequestCallback<List<BannerBean>>
    override fun onCreate(owner: LifecycleOwner?) {
        articleRepo = CodeRepository()
        if (owner != null) {
            (articleRepo as CodeRepository).essayArticleLiveData.observe(
                owner,
                Observer<List<EssayArticleBean>> { beans ->
                    callback.success(beans)
                })
        }
        if (owner != null) {
            (articleRepo as CodeRepository).bannerLiveData.observe(
                owner,
                Observer<List<BannerBean>> { beans ->
                    bannerCallback.success(beans)
                })
        }
    }

    fun getEssayDatas(
        page: Int,
        row: Int,
        isLoadMore: Boolean,
        callback: IRequestCallback<List<EssayArticleBean>>
    ) {
        this.callback = callback
        addSubscrible(articleRepo?.getEssayDatas(page, row, isLoadMore, callback))
    }

    fun getBannerDatas(callback: IRequestCallback<List<BannerBean>>) {
        bannerCallback = callback
        addSubscrible(articleRepo?.getBannerDatas(callback))
    }
}