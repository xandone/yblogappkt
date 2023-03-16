package com.app.xandone.yblogapp.ui.articledetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.xandone.yblogapp.model.bean.CodeDetailsBean
import com.app.xandone.yblogapp.model.bean.EssayDetailsBean
import com.app.xandone.yblogapp.model.repository.CodeRepository
import com.app.xandone.yblogapp.model.repository.ApiResponse

/**
 * author: Admin
 * created on: 2020/8/12 16:29
 * description:
 */
class CodeDetailsModel : ViewModel() {
    val datas: MutableLiveData<ApiResponse<List<CodeDetailsBean>>> = MutableLiveData()

    val datas2: MutableLiveData<ApiResponse<List<EssayDetailsBean>>> = MutableLiveData()

    suspend fun getCodeDetails(id: String?) {
        datas.value = CodeRepository.getCodeDetails(id)
    }

    suspend fun getEssayDetails(id: String?) {
        datas2.value = CodeRepository.getEssayDetails(id)
    }
}