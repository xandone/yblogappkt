package com.app.xandone.yblogapp.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.app.xandone.yblogapp.api.IFetchArticle
import com.app.xandone.yblogapp.model.bean.CodeArticleBean
import com.app.xandone.yblogapp.model.repository.CodeRepository
import com.app.xandone.yblogapp.rx.IRequestCallback
import com.app.xandone.yblogapp.viewmodel.BaseViewModel

/**
 * author: Admin
 * created on: 2020/8/12 16:29
 * description:
 */
class CodeModel : BaseViewModel() {
    private var articleRepo: IFetchArticle? = null
    private lateinit var callback: IRequestCallback<List<CodeArticleBean>>

    override fun onCreate(owner: LifecycleOwner?) {
        articleRepo = CodeRepository()
        if (owner != null) {
            (articleRepo as CodeRepository).codeArticleLiveData
                .observe(
                owner,
                Observer<List<CodeArticleBean>> { beans ->
                    callback.success(beans)
                })
        }
    }

    fun getCodeDatas(
        page: Int,
        row: Int,
        type: Int,
        isLoadMore: Boolean,
        callback: IRequestCallback<List<CodeArticleBean>>
    ) {
        this.callback = callback
        addSubscrible(articleRepo?.getCodeDatas(page, row, type, isLoadMore, callback))
    }
}