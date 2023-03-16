package com.app.xandone.yblogapp.ui.essay

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.xandone.yblogapp.model.bean.BannerBean
import com.app.xandone.yblogapp.model.bean.EssayArticleBean
import com.app.xandone.yblogapp.model.repository.CodeRepository
import com.app.xandone.yblogapp.model.repository.ApiResponse

/**
 * author: Admin
 * created on: 2020/8/12 16:29
 * description:
 */
class EssayModel : ViewModel() {
    val datas: MutableLiveData<ApiResponse<List<EssayArticleBean>>> = MutableLiveData()

    val datas2: MutableLiveData<ApiResponse<List<BannerBean>>> = MutableLiveData()

    suspend fun getEssayDatas(isMore: Boolean = false, page: Int = 1, pagesize: Int = 10) {
        datas.value = CodeRepository.getEssayDatas(page, pagesize)
    }

    suspend fun getBannerDatas() {
        datas2.value = CodeRepository.getBannerDatas()
    }

}