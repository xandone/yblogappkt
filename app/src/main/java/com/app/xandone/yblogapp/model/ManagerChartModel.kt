package com.app.xandone.yblogapp.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.app.xandone.yblogapp.api.IManager
import com.app.xandone.yblogapp.model.bean.ArtInfoBean
import com.app.xandone.yblogapp.model.repository.ManagerRepository
import com.app.xandone.yblogapp.rx.IRequestCallback
import com.app.xandone.yblogapp.viewmodel.BaseViewModel

/**
 * author: Admin
 * created on: 2020/9/27 11:30
 * description:
 */
class ManagerChartModel : BaseViewModel() {
    private var manaerRepo: IManager? = null
    private lateinit var adminCallback: IRequestCallback<ArtInfoBean>

    override fun onCreate(owner: LifecycleOwner?) {
        manaerRepo = ManagerRepository()
        if (owner != null) {
            (manaerRepo as ManagerRepository).artInfoLiveData?.observe(
                owner,
                Observer<ArtInfoBean?> { adminBean ->
                    if (adminBean != null) {
                        adminCallback.success(adminBean)
                    }
                })
        }
    }

    fun getArtInfoData(
        id: String?,
        callback: IRequestCallback<ArtInfoBean>
    ) {
        adminCallback = callback
        addSubscrible(manaerRepo?.getArtInfoData(id, callback))
    }
}