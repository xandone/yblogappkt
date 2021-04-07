package com.app.xandone.yblogapp.rx

import com.app.xandone.baselib.log.LogHelper
import com.app.xandone.baselib.utils.JsonUtils.obj2Json
import com.app.xandone.yblogapp.constant.OResponseCode
import com.app.xandone.yblogapp.exception.ApiException
import com.app.xandone.yblogapp.model.base.BaseResponse
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers


/**
 * author: Admin
 * created on: 2019/7/3 14:44
 * description:
 */
object RxHelper {
    fun <T> handleIO(): FlowableTransformer<T, T> {
        return FlowableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

//    fun <T> handleRespose(): FlowableTransformer<BaseResponse<T>, T> {
//        return FlowableTransformer<BaseResponse<T>, T> { httpResponseFlowable ->
//            httpResponseFlowable.flatMap(Function<BaseResponse<T>, Flowable<T>> { response ->
//                LogHelper.d(obj2Json(response))
//                if (response.code == OResponseCode.SUCCESS) {
//                    createData(response.data!!)
//                } else {
//                    Flowable.error(ApiException(response.msg, response.code))
//                }
//            })
//        }
//    }

    fun <T> handleRespose(): FlowableTransformer<BaseResponse<T>, T> {
        return FlowableTransformer<BaseResponse<T>, T> { flowable ->
            flowable.flatMap { response ->
                if (response.code == OResponseCode.SUCCESS) {
                    createData(response.data!!)
                } else {
                    Flowable.error(ApiException(response.msg, response.code))
                }
            }
        }
    }

    /**
     * 处理不转换类型，code非200的情况
     *
     * @return
     */
    fun <T> handleBaseResponse(): FlowableTransformer<BaseResponse<T>, BaseResponse<T>> {
        return FlowableTransformer { flowable ->
            flowable.flatMap(Function<BaseResponse<T>, Flowable<BaseResponse<T>>> { bean ->
                LogHelper.d(obj2Json(bean))
                if (bean.code == OResponseCode.SUCCESS) {
                    createData(bean)
                } else {
                    Flowable.error(ApiException(bean.msg, bean.code))
                }
            })
        }
    }

    private fun <T> createData(t: T): Flowable<T> {
        return Flowable.create(FlowableOnSubscribe { emitter ->
            try {
                emitter.onNext(t)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
                LogHelper.e(e.toString())
            }
        }, BackpressureStrategy.BUFFER)
    }
}