package com.app.xandone.yblogapp.ui.manager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.xandone.yblogapp.model.bean.AdminBean
import com.app.xandone.yblogapp.model.repository.ManagerRepository
import com.app.xandone.yblogapp.model.repository.ApiResponse

/**
 * author: Admin
 * created on: 2020/8/12 16:29
 * description:
 */
class ManagerModel : ViewModel() {
    val datas: MutableLiveData<ApiResponse<List<AdminBean>>> = MutableLiveData()

    suspend fun login(name: String,
                      psw: String) {
        datas.value = ManagerRepository.login(name, psw)
    }

}