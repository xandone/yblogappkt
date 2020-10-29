package com.app.xandone.yblogapp.base

import android.view.View
import com.app.xandone.yblogapp.R
import kotlinx.android.synthetic.main.frag_base_list.*

/**
 * author: Admin
 * created on: 2020/9/2 09:27
 * description:
 */
abstract class BaseListFragment : BaseWallFragment(), IRefreshCallback {
    override fun getLayout(): Int {
        return R.layout.frag_base_list
    }

    override fun init(view: View?) {
        refreshLayout.setOnRefreshListener { getData() }
        refreshLayout.setOnLoadMoreListener { getDataMore() }
    }

    override fun requestData() {}
    override fun finishRefresh() {
        refreshLayout.finishRefresh()
    }

    override fun finishLoadMore() {
        refreshLayout.finishLoadMore()
    }
}