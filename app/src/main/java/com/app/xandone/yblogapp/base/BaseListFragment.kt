package com.app.xandone.yblogapp.base

import android.view.View
import androidx.annotation.CallSuper
import com.app.xandone.yblogapp.databinding.FragBaseListBinding
import com.app.xandone.yblogapp.model.repository.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * author: Admin
 * created on: 2020/9/2 09:27
 * description:
 */
abstract class BaseListFragment<T> : BaseWallFragment<FragBaseListBinding>(FragBaseListBinding::inflate), IRefreshCallback {
    protected var mPage = 1
    protected val mDatas = mutableListOf<T>()
    protected lateinit var mAdapter: BaseQuickAdapter<T, BaseViewHolder>

    @CallSuper
    override fun initView(view: View) {
        mBinding.refreshLayout.setOnRefreshListener { getData() }
        mBinding.refreshLayout.setOnLoadMoreListener { getDataMore() }
    }

    /**
     * 统一处理接口列表数据
     */
    protected open fun handleDate(response: ApiResponse<List<T>>) {
        val isMore = mPage != 1
        if (response.result == HttpResult.SUCCESS && response.data != null) {
            if (!isMore) {
                mAdapter.setList(response.data)
                if (response.data.isEmpty() || response.total == 0) {
                    onLoadEmpty(ApiEmptyResponse<Any>(empty = "不毛之地~"))
                    return
                }
            } else {
                mAdapter.addData(response.data)
            }
            if (response.total <= mDatas.size) {
                finishLoadNoMoreData()
            } else {
                finishLoadMore()
            }

            onLoadFinish()
        } else {
            when (response) {
                is ApiEmptyResponse -> {
                    onLoadEmpty(response)
                }
                is ApiErrorResponse -> {
                    onLoadSeverError(response)
                }
                is ExceptionResponse -> {
                    onLoadSeverError(response)
                }
                else -> {
                    onLoadSeverError(ExceptionResponse<Any>())
                }
            }

            if (isMore) {
                finishLoadMore(false)
            }
        }

        finishRefresh()
    }


    override fun finishRefresh() {
        mBinding.refreshLayout.finishRefresh()
    }

    override fun finishLoadMore(success: Boolean) {
        mBinding.refreshLayout.finishLoadMore()
    }

    override fun finishLoadNoMoreData() {
        mBinding.refreshLayout.finishLoadMoreWithNoMoreData()
    }

    override fun unableLoadMore() {
        mBinding.refreshLayout.setEnableLoadMore(false)
    }

    override fun reload(tag: Any?) {
        getData()
    }

}