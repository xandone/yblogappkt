package com.app.xandone.yblogapp.ui.code.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.xandone.yblogapp.model.bean.CodeArticleBean
import com.app.xandone.yblogapp.model.repository.CodeRepository
import com.app.xandone.yblogapp.model.repository.ApiResponse

/**
 * author: Admin
 * created on: 2020/8/12 16:29
 * description:
 */
class CodeModel : ViewModel() {
    val datas: MutableLiveData<ApiResponse<List<CodeArticleBean>>> = MutableLiveData()

    suspend fun getCodeDatas(isMore: Boolean = false,
                             page: Int = 1,
                             pagesize: Int = 10,
                             type: Int) {
        datas.value = CodeRepository.getCodeDatas(page, pagesize, type)
    }

}