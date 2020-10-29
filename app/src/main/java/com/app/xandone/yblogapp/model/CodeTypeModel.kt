package com.app.xandone.yblogapp.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.app.xandone.yblogapp.api.IFetchArticle
import com.app.xandone.yblogapp.model.bean.CodeTypeBean
import com.app.xandone.yblogapp.model.repository.CodeRepository
import com.app.xandone.yblogapp.rx.IRequestCallback
import com.app.xandone.yblogapp.viewmodel.BaseViewModel

/**
 * author: Admin
 * created on: 2020/9/9 16:29
 * description:
 */
class CodeTypeModel : BaseViewModel() {
    private lateinit var articleRepo: IFetchArticle
    private lateinit var callback: IRequestCallback<List<CodeTypeBean>>
    override fun onCreate(owner: LifecycleOwner?) {
        articleRepo = CodeRepository()
        if (owner != null) {
            (articleRepo as CodeRepository).codeTypeLiveData
                .observe(owner, Observer<List<CodeTypeBean>> { beans ->
                    callback.success(beans)
                })
        }
    }

    fun getCodeTypeDatas(callback: IRequestCallback<List<CodeTypeBean>>) {
        this.callback = callback
        addSubscrible(articleRepo.getCodeTypeDatas(callback))
    }
}