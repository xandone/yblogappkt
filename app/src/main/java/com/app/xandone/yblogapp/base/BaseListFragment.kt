package com.app.xandone.yblogapp.base

import android.view.View
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.databinding.FragBaseListBinding
import kotlinx.android.synthetic.main.frag_base_list.*

/**
 * author: Admin
 * created on: 2020/9/2 09:27
 * description:
 */
abstract class BaseListFragment : BaseWallFragment<FragBaseListBinding>(), IRefreshCallback {
    override fun getLayout(): Int {
        return R.layout.frag_base_list
    }

    override fun initVB(): FragBaseListBinding {
        return FragBaseListBinding.inflate(layoutInflater)
    }

    override fun initView(view: View) {
        mBinding.refreshLayout.setOnRefreshListener { getData() }
        refreshLayout.setOnLoadMoreListener { getDataMore() }
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