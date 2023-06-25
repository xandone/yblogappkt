package com.app.xandone.yblogapp.ui.essay

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.xandone.yblogapp.model.bean.BannerBean
import com.app.xandone.yblogapp.model.bean.EssayArticleBean
import com.app.xandone.yblogapp.model.repository.CodeRepository
import com.app.xandone.yblogapp.model.repository.ApiResponse
import com.app.xandone.yblogapp.model.repository.HttpResult

/**
 * author: Admin
 * created on: 2020/8/12 16:29
 * description:
 */
class EssayModel : ViewModel() {
    val datas: MutableLiveData<ApiResponse<List<EssayArticleBean>>> = MutableLiveData()

    val datas2: MutableLiveData<ApiResponse<List<BannerBean>>> = MutableLiveData()

    suspend fun getEssayDatas(page: Int = 1, pagesize: Int = 10) {

        datas.value = CodeRepository.getEssayDatas(page, pagesize)
    }

    suspend fun getBannerDatas() {
//        val x = 5 / 0

        //异步请求getEssayDatas和getBannerDatas，其中一个接口false的情况
        val resp = CodeRepository.getBannerDatas()
        resp.result = HttpResult.FAIL
        datas2.value = resp
    }

}