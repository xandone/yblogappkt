//package com.app.xandone.yblogapp.rx
//
//import com.app.xandone.yblogapp.exception.ApiException
//import com.app.xandone.yblogapp.model.base.BaseResponse
//import io.reactivex.*
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers
//import io.reactivex.functions.Function
//
///**
// * author: Admin
// * created on: 2020/10/28 10:29
// * description:
// */
//object RxHelper2 {
//    /**
//     * compose简化线程切换
//     * @param <T>
//     * @return
//    </T> */
//    fun <T> rxSchedulerObservableHelper(): ObservableTransformer<T, T> {
//        return ObservableTransformer { observable ->
//            observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//        }
//    }
//
//    /**
//     * compose简化线程切换
//     * @param <T>
//     * @return
//    </T> */
//    fun <T> rxSchedulerFlowableHelper(): FlowableTransformer<T, T> {
//        return FlowableTransformer { observable ->
//            observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//        }
//    }
//
//    /**
//     *compose判断结果,统一返回结果处理,支持背压
//     * @param <T>
//     * @return
//    </T> */
//    fun <T> handleObservableResult(): ObservableTransformer<T, T> {
//        return ObservableTransformer { observable ->
//            observable.flatMap(Function<T, Observable<T>> { bean ->
//                val baseBean = bean as BaseResponse<*>
//                if (baseBean.code == 200) {
//                    createObservable(bean)
//                } else {
//                    Observable.error(ApiException(baseBean.msg))
//                }
//            })
//        }
//    }
//
//    /**
//     *compose判断结果,统一返回结果处理,支持背压
//     * @param <T>
//     * @return
//    </T> */
//    fun <T> handleFlowableResult(): FlowableTransformer<T, T> {
//        return FlowableTransformer { flowable ->
//            flowable.flatMap(Function<T, Flowable<T>> { bean ->
//                val baseBean = bean as BaseResponse<*>
//                if (baseBean.code == 200) {
//                    createFlowable(bean)
//                } else {
//                    Flowable.error(ApiException(baseBean.msg))
//                }
//            })
//        }
//    }
//
//    /**
//     *不通过继承,通过泛型实现实体类
//     * @param <T>
//     * @return
//    </T> */
//    fun <T> handleFlowableResult2(): FlowableTransformer<BaseResponse<T>, T> {
//        return FlowableTransformer { flowable ->
//            flowable.flatMap { bean ->
//                if (bean.code == 200) {
//                    createFlowable(bean.data)
//                } else {
//                    Flowable.error(ApiException(bean.msg))
//                }
//            }
//        }
//    }
//
//    /**
//     * 生成Observable,不支持背压
//     * @param <T>
//     * @return
//    </T> */
//    private fun <T> createObservable(t: T): Observable<T> {
//        return Observable.create { emitter ->
//            try {
//                emitter.onNext(t)
//                emitter.onComplete()
//            } catch (e: Exception) {
//                emitter.onError(e)
//            }
//        }
//    }
//
//    /**
//     * 生成Flowable,支持背压
//     * @param <T>
//     * @return
//    </T> */
//    private fun <T> createFlowable(t: T): Flowable<T> {
//        return Flowable.create({ emitter ->
//            try {
//                emitter.onNext(t)
//                emitter.onComplete()
//            } catch (e: Exception) {
//                emitter.onError(e)
//            }
//        }, BackpressureStrategy.BUFFER)
//    }
//}