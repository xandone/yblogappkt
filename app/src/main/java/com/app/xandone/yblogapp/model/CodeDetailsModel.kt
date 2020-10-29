package com.app.xandone.yblogapp.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.app.xandone.yblogapp.api.IFetchArticle
import com.app.xandone.yblogapp.model.bean.CodeDetailsBean
import com.app.xandone.yblogapp.model.bean.EssayDetailsBean
import com.app.xandone.yblogapp.model.repository.CodeRepository
import com.app.xandone.yblogapp.rx.IRequestCallback
import com.app.xandone.yblogapp.viewmodel.BaseViewModel

/**
 * author: Admin
 * created on: 2020/9/4 11:18
 * description:
 */
class CodeDetailsModel : BaseViewModel(), IArtDetailsModel<CodeDetailsBean> {
    private var articleRepo: IFetchArticle? = null
    private lateinit var callback: IRequestCallback<CodeDetailsBean>
    override fun onCreate(owner: LifecycleOwner?) {
        articleRepo = CodeRepository()
        if (owner != null) {
            (articleRepo as CodeRepository).codeDetailsLiveData
                .observe(owner, Observer<CodeDetailsBean> { codeDetailsBean ->
                    callback.success(codeDetailsBean!!)
                })
        }
    }

    override fun getDetails(
        id: String?,
        callback: IRequestCallback<CodeDetailsBean>
    ) {
        this.callback = callback
        addSubscrible((articleRepo as CodeRepository).getCodeDetails(id, callback))
    }

}