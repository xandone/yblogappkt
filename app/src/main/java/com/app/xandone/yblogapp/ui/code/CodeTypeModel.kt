package com.app.xandone.yblogapp.ui.code

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.xandone.yblogapp.model.bean.CodeTypeBean
import com.app.xandone.yblogapp.model.repository.CodeRepository
import com.app.xandone.yblogapp.model.repository.ApiResponse

/**
 * author: Admin
 * created on: 2020/8/12 16:29
 * description:
 */
class CodeTypeModel : ViewModel() {
    val datas: MutableLiveData<ApiResponse<List<CodeTypeBean>>> = MutableLiveData()

    suspend fun getCodeTypeDatas() {
        datas.value = CodeRepository.getCodeTypeDatas()
    }

}