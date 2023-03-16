package com.app.xandone.yblogapp.ui.manager.chart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.xandone.yblogapp.model.bean.ArtInfoBean
import com.app.xandone.yblogapp.model.repository.ManagerRepository
import com.app.xandone.yblogapp.model.repository.ApiResponse

/**
 * author: Admin
 * created on: 2020/8/12 16:29
 * description:
 */
class ManagerChartModel : ViewModel() {
    val datas: MutableLiveData<ApiResponse<ArtInfoBean>> = MutableLiveData()

    suspend fun getArtInfoData(id: String) {
        datas.value = ManagerRepository.getArtInfoData(id)
    }

}