package com.app.xandone.widgetlib.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import java.util.*

/**
 * author: Admin
 * created on: 2020/9/4 9:21
 * description:
 */
class TextInputHelper(view: View?) : TextWatcher {
    private val mMainView: View
    private var mViewSet: MutableList<TextView>? = null
    fun addViews(vararg views: TextView) {
        if (mViewSet == null) {
            mViewSet = ArrayList(views.size - 1)
        }
        for (view in views) {
            view.addTextChangedListener(this)
            mViewSet!!.add(view)
        }
        afterTextChanged(null)
    }

    fun removeViews() {
        if (mViewSet == null) {
            return
        }
        for (view in mViewSet!!) {
            view.removeTextChangedListener(this)
        }
        mViewSet!!.clear()
        mViewSet = null
    }

    // TextWatcher
    override fun beforeTextChanged(s: CharSequence,
                                   start: Int,
                                   count: Int,
                                   after: Int) {
    }

    override fun onTextChanged(s: CharSequence,
                               start: Int,
                               before: Int,
                               count: Int) {
    }

    @Synchronized
    override fun afterTextChanged(s: Editable?) {
        if (mViewSet == null) {
            return
        }
        for (view in mViewSet!!) {
            if ("" == view.text.toString()) {
                if (mCallBack != null) {
                    mCallBack!!.onSelect(false)
                }
                return
            }
        }
        if (mCallBack != null) {
            mCallBack!!.onSelect(true)
        }
    }

    /**
     * 设置View的事件
     *
     * @param enabled 修改button背景颜色
     */
    fun setEnabled(enabled: Boolean) {
        if (enabled == mMainView.isSelected) {
            return
        }
        if (enabled) {
            mMainView.isSelected = true
            mMainView.isEnabled = true
        } else {
            mMainView.isSelected = false
            mMainView.isEnabled = false
        }
    }

    private var mCallBack: ISelectCallBack? = null
    fun setSelectCallBack(callBack: ISelectCallBack?) {
        mCallBack = callBack
    }

    init {
        requireNotNull(view) { "The view is empty" }
        mMainView = view
        mMainView.isEnabled = false
        setSelectCallBack(object : ISelectCallBack {
            override fun onSelect(isSelect: Boolean) {
                setEnabled(isSelect)
            }
        })
    }
}