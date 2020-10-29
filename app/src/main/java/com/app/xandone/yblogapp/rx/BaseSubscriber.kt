package com.app.xandone.yblogapp.rx

import android.text.TextUtils
import com.app.xandone.baselib.log.LogHelper
import com.app.xandone.baselib.utils.JsonUtils
import com.app.xandone.baselib.utils.NetworkUtils.isConnected
import com.app.xandone.baselib.utils.ToastUtils.showShort
import com.app.xandone.widgetlib.view.LoadingLayout
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.exception.ApiException
import io.reactivex.subscribers.ResourceSubscriber
import retrofit2.HttpException

/**
 * author: xandone
 * created on: 2020/8/13 16:48
 * description:
 */
abstract class BaseSubscriber<T> : ResourceSubscriber<T> {
    private var mErrorMsg: String? = null
    private var isShowErrorState = false

    //默认开启
    constructor() : this(true) {}
    protected constructor(errorMsg: String?) {
        mErrorMsg = errorMsg
    }

    protected constructor(isShowErrorState: Boolean) {
        this.isShowErrorState = isShowErrorState
    }

    protected constructor(errorMsg: String?, isShowErrorState: Boolean) {
        mErrorMsg = errorMsg
        this.isShowErrorState = isShowErrorState
    }

    override fun onNext(t: T) {
        if (!isConnected(App.sContext!!)) {
            showShort("无法连接，请检查网络")
        }
        onSuccess(t)
    }

    override fun onError(t: Throwable) {
        if (isShowErrorState) {
            if (!TextUtils.isEmpty(mErrorMsg)) {
                onFail(mErrorMsg, LoadingLayout.ILoadingStatus.SERVER_ERROR)
            } else if (t is ApiException) {
                onFail(t.toString(), LoadingLayout.ILoadingStatus.SERVER_ERROR)
            } else if (t is HttpException) {
                onFail("数据加载失败", LoadingLayout.ILoadingStatus.NET_ERROR)
            } else {
                onFail("未知错误", LoadingLayout.ILoadingStatus.SERVER_ERROR)
                LogHelper.d(t.toString())
            }
        }
        val ex: ApiException = ApiException.handleException(t)
        if (!TextUtils.isEmpty(ex.message)) {
            showShort(ex.message!!)
        }
    }

    override fun onComplete() {}
    abstract fun onSuccess(t: T)
    open fun onFail(message: String?, statusCode: Int) {}
}