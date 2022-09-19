package com.app.xandone.yblogapp.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.app.xandone.yblogapp.rx.IManagerDisposable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * author: Admin
 * created on: 2020/8/12 11:39
 * description:
 */
abstract class BaseViewModel : ViewModel(), LifecycleOwner, IManagerDisposable {
    private var mLifecycleOwner: LifecycleOwner? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    protected abstract fun onCreate(owner: LifecycleOwner?)

    fun <T : BaseViewModel> attachLifecycleOwner(lifecycleOwner: LifecycleOwner?): T {
        val current = this as T
        if (mLifecycleOwner != null) {
            return current
        }
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mLifecycleOwner = lifecycleOwner
        onCreate(lifecycleOwner)
        return current
    }

    override fun addSubscrible(disposable: Disposable?) {
        if (disposable == null) {
            return
        }
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable!!.add(disposable)
    }

    override fun clearSubscrible() {
        if (mCompositeDisposable == null) {
            return
        }
        mCompositeDisposable!!.clear()
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleOwner!!.lifecycle
    }

    override fun onCleared() {
        super.onCleared()
        clearSubscrible()
    }
}