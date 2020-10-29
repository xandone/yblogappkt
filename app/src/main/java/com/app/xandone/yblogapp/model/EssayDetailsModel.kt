package com.app.xandone.yblogapp.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.app.xandone.yblogapp.api.IFetchArticle
import com.app.xandone.yblogapp.model.bean.EssayDetailsBean
import com.app.xandone.yblogapp.model.repository.CodeRepository
import com.app.xandone.yblogapp.rx.IRequestCallback
import com.app.xandone.yblogapp.viewmodel.BaseViewModel

/**
 * author: Admin
 * created on: 2020/9/4 11:18
 * description:
 */
class EssayDetailsModel : BaseViewModel(), IArtDetailsModel<EssayDetailsBean> {
    private lateinit var articleRepo: IFetchArticle
    private lateinit var callback: IRequestCallback<EssayDetailsBean>
    override fun onCreate(owner: LifecycleOwner?) {
        articleRepo = CodeRepository()
        if (owner != null) {
            (articleRepo as CodeRepository).essayDetailsLiveData
                .observe(owner, Observer<EssayDetailsBean> { codeDetailsBean ->
                    callback?.success(codeDetailsBean)
                }
                )
        }
    }

    override fun getDetails(
        id: String?,
        callback: IRequestCallback<EssayDetailsBean>
    ) {
        this.callback = callback
        addSubscrible((articleRepo as CodeRepository).getEssayDetails(id, callback))
    }
}