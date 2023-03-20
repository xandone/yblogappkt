package com.app.xandone.yblogapp.base

import android.util.Log
import android.view.View
import androidx.annotation.CallSuper
import com.app.xandone.yblogapp.databinding.FragBaseListBinding
import com.app.xandone.yblogapp.model.repository.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.layout_empty.*

/**
 * author: Admin
 * created on: 2020/9/2 09:27
 * description:
 */
abstract class BaseListFragment<T> : BaseWallFragment<FragBaseListBinding>(), IRefreshCallback {
    protected var mPage = 1
    protected val mDatas = mutableListOf<T>()
    protected lateinit var mAdapter: BaseQuickAdapter<T, BaseViewHolder>

    override fun initVB(): FragBaseListBinding {
        return FragBaseListBinding.inflate(layoutInflater)
    }

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
                is ApiOtherErrorResponse -> {
                    onLoadSeverError(response)
                }
                else -> {
                    onLoadSeverError(ApiOtherErrorResponse<Any>())
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