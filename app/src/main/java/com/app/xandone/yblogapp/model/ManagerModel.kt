package com.app.xandone.yblogapp.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.app.xandone.yblogapp.api.IManager
import com.app.xandone.yblogapp.model.bean.AdminBean
import com.app.xandone.yblogapp.model.repository.ManagerRepository
import com.app.xandone.yblogapp.rx.IRequestCallback
import com.app.xandone.yblogapp.viewmodel.BaseViewModel

/**
 * author: Admin
 * created on: 2020/9/27 11:30
 * description:
 */
class ManagerModel : BaseViewModel() {
    private lateinit var manaerRepo: IManager
    private lateinit var adminCallback: IRequestCallback<AdminBean>

    override fun onCreate(owner: LifecycleOwner?) {
        manaerRepo = ManagerRepository()
        if (owner != null) {
            (manaerRepo as ManagerRepository).adminLiveData.observe(
                owner,
                Observer<AdminBean> { adminBean ->
                    adminCallback.success(adminBean)
                })
        }
    }

    fun login(
        name: String?,
        psw: String?,
        callback: IRequestCallback<AdminBean>
    ) {
        adminCallback = callback
        addSubscrible(manaerRepo.login(name, psw, callback))
    }
}