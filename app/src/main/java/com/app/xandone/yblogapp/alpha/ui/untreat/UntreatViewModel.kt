package com.xandone.manager2.ui.untreat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xandone.manager2.model.bean.UntreateBean
import com.xandone.manager2.model.repository.ApiResponse
import com.xandone.manager2.model.repository.ListBean
import com.xandone.manager2.model.repository.DataRepository

/**
 * @author: xiao
 * created on: 2023/2/8 15:31
 * description:
 */
class UntreatViewModel : ViewModel() {

    val datas: MutableLiveData<ApiResponse<ListBean<UntreateBean>>> = MutableLiveData()

    suspend fun getList(adminUid: Int, page: Int = 0, pagesize: Int = 10) {
        datas.value = DataRepository.getUntreateList(adminUid, page, pagesize)
    }
}
