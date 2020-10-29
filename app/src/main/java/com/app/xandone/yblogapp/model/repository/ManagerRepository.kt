package com.app.xandone.yblogapp.model.repository

import androidx.lifecycle.MediatorLiveData
import com.app.xandone.yblogapp.api.ApiClient
import com.app.xandone.yblogapp.api.IManager
import com.app.xandone.yblogapp.model.base.BaseResponse
import com.app.xandone.yblogapp.model.bean.AdminBean
import com.app.xandone.yblogapp.model.bean.ArtInfoBean
import com.app.xandone.yblogapp.rx.BaseSubscriber
import com.app.xandone.yblogapp.rx.IRequestCallback
import com.app.xandone.yblogapp.rx.RxHelper
import io.reactivex.disposables.Disposable

/**
 * author: Admin
 * created on: 2020/9/27 11:32
 * description:
 */
class ManagerRepository : IManager {
    override val adminLiveData: MediatorLiveData<AdminBean> = MediatorLiveData()
    override val artInfoLiveData: MediatorLiveData<ArtInfoBean> = MediatorLiveData()

    override fun login(
        name: String?,
        psw: String?,
        callback: IRequestCallback<AdminBean>
    ): Disposable? {
        return ApiClient.instance
            ?.apiService
            ?.login(name, psw)
            ?.compose(RxHelper.handleIO())
            ?.compose(RxHelper.handleBaseResponse())
            ?.subscribeWith(object :
                BaseSubscriber<BaseResponse<List<AdminBean>>>() {
                override fun onSuccess(response: BaseResponse<List<AdminBean>>) {
                    if (response.data != null && response.data!!.isNotEmpty()) {
                        adminLiveData.setValue(response.data!![0])
                    } else {
                        callback.error(response.msg, response.code)
                    }
                }

                override fun onFail(message: String?, code: Int) {
                    super.onFail(message, code)
                    if (message != null) {
                        callback.error(message, code)
                    }
                }
            })
    }

    override fun getArtInfoData(
        id: String?,
        callback: IRequestCallback<ArtInfoBean>
    ): Disposable? {
        return ApiClient.instance
            ?.apiService
            ?.getArtInfoDatas(id)
            ?.compose(RxHelper.handleIO())
            ?.compose(RxHelper.handleBaseResponse())
            ?.subscribeWith(object :
                BaseSubscriber<BaseResponse<ArtInfoBean>>() {
                override fun onSuccess(response: BaseResponse<ArtInfoBean>) {
                    if (response.data != null) {
                        artInfoLiveData.setValue(response.data)
                    } else {
                        callback.error(response.msg, response.code)
                    }
                }

                override fun onFail(message: String?, code: Int) {
                    super.onFail(message, code)
                    if (message != null) {
                        callback.error(message, code)
                    }
                }
            })
    }

}