package com.app.xandone.yblogapp.ui.code.list

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.xandone.baselib.imageload.ImageLoadHelper
import com.app.xandone.baselib.utils.JsonUtils
import com.app.xandone.widgetlib.utils.SpacesItemDecoration
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.base.BaseListFragment
import com.app.xandone.yblogapp.constant.OConstantKey
import com.app.xandone.yblogapp.databinding.FragBaseListBinding
import com.app.xandone.yblogapp.model.bean.CodeArticleBean
import com.app.xandone.yblogapp.ui.articledetails.ArticleDetailsActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.app.xandone.yblogapp.model.repository.ApiEmptyResponse
import com.app.xandone.yblogapp.model.repository.ApiErrorResponse
import com.app.xandone.yblogapp.model.repository.ApiOtherErrorResponse
import com.app.xandone.yblogapp.model.repository.HttpResult
import kotlinx.android.synthetic.main.frag_base_list.*
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

/**
 * author: Admin
 * created on: 2020/8/31 17:55
 * description:
 */
class CodeListFragment : BaseListFragment() {

    private lateinit var mAdapter: BaseQuickAdapter<CodeArticleBean, BaseViewHolder>
    private lateinit var mDatas: ArrayList<CodeArticleBean>
    private var mType = 0
    private var mPage = 1
    protected var mIsLoadedData = false

    private val codeModel by lazy {
        ViewModelProvider(this, CodeViewModelFactory()).get(
            CodeModel::class.java
        )
    }

    override fun initView(view: View) {
        super.initView(view)
        mType = arguments!!.getInt(TYPE)
        mDatas = ArrayList()
        mAdapter = object :
            BaseQuickAdapter<CodeArticleBean, BaseViewHolder>(
                R.layout.item_code_list,
                mDatas as MutableList<CodeArticleBean>?) {
            override fun convert(holder: BaseViewHolder, item: CodeArticleBean) {
                holder.setText(R.id.code_title_tv, item.title)
                holder.setText(R.id.code_type_tv, item.typeName)
                holder.setText(R.id.code_content_tv, item.content)
                holder.setText(R.id.code_date_tv, item.postTime)
                val codeCoverImg =
                    holder.getView<ImageView>(R.id.code_cover_img)
                if (TextUtils.isEmpty(item.coverImg)) {
                    codeCoverImg.visibility = View.GONE
                } else {
                    codeCoverImg.visibility = View.VISIBLE
                    ImageLoadHelper.instance
                        .display(App.sContext, item.coverImg, codeCoverImg)
                }
            }
        }
        recycler.layoutManager = LinearLayoutManager(mActivity)
        recycler.addItemDecoration(SpacesItemDecoration(App.sContext!!, 10, 10, 10))
        recycler.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, _, position ->
            startActivity(
                Intent(mActivity, ArticleDetailsActivity::class.java)
                    .putExtra(OConstantKey.ID, mDatas[position].artId)
                    .putExtra(OConstantKey.TYPE, ArticleDetailsActivity.Companion.TYPE_CODE)
                    .putExtra(OConstantKey.TITLE, mDatas[position].title)
            )
        }

        codeModel.datas.observe(this) { response ->
            if (response.result == HttpResult.SUCCESS && response.data != null) {
                if (mPage == 0) {
                    mAdapter.setList(response.data)
                    if (response.total == 0) {
                        onLoadEmpty(ApiEmptyResponse<Any>())
                        return@observe
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

                if (mPage != 0) {
                    mBinding.refreshLayout.finishLoadMore(false)
                }
            }

            finishRefresh()
        }
    }

    protected fun lazyLoadData() {
        mPage = 1
        getCodeDatas(false)
    }

    private fun getCodeDatas(isLoadMore: Boolean) {
        lifecycleScope.launch {
            codeModel.getCodeDatas(isLoadMore, mPage, ROW, mType)
        }
    }

    override fun getData() {
        mPage = 1
        getCodeDatas(false)
    }

    override fun getDataMore() {
        mPage++
        getCodeDatas(true)
    }

    override fun onResume() {
        super.onResume()
        if (!mIsLoadedData) {
            lazyLoadData()
            mIsLoadedData = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mIsLoadedData = false
    }

    companion object {
        private const val ROW = 10
        const val TYPE = "type"

        fun getInstance(type: Int): CodeListFragment {
            val fragment = CodeListFragment()
            val bundle = Bundle()
            bundle.putInt(TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }
}