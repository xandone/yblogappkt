package com.app.xandone.yblogapp.ui.code.list

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.xandone.baselib.imageload.ImageLoadHelper
import com.app.xandone.widgetlib.utils.SpacesItemDecoration
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.base.BaseListFragment
import com.app.xandone.yblogapp.constant.OConstantKey
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
class CodeListFragment : BaseListFragment<CodeArticleBean>() {
    private var mType = 0
    protected var mIsLoadedData = false

    private val codeModel by lazy {
        ViewModelProvider(this, CodeViewModelFactory()).get(
            CodeModel::class.java
        )
    }

    override fun initView(view: View) {
        super.initView(view)
        mType = arguments!!.getInt(TYPE)
        mAdapter = object :
            BaseQuickAdapter<CodeArticleBean, BaseViewHolder>(R.layout.item_code_list, mDatas) {
            override fun convert(holder: BaseViewHolder, item: CodeArticleBean) {
                holder.setText(R.id.code_title_tv, item.title)
                holder.setText(R.id.code_type_tv, item.typeName)
                holder.setText(R.id.code_content_tv, item.content)
                holder.setText(R.id.code_date_tv, item.postTime)
                val codeCoverImg = holder.getView<ImageView>(R.id.code_cover_img)
                if (TextUtils.isEmpty(item.coverImg)) {
                    codeCoverImg.visibility = View.GONE
                } else {
                    codeCoverImg.visibility = View.VISIBLE
                    ImageLoadHelper.instance.display(App.sContext, item.coverImg, codeCoverImg)
                }
            }
        }
        recycler.layoutManager = LinearLayoutManager(mActivity)
        recycler.addItemDecoration(SpacesItemDecoration(App.sContext, 10, 10, 10))
        recycler.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, _, position ->
            startActivity(
                Intent(mActivity, ArticleDetailsActivity::class.java)
                    .putExtra(OConstantKey.ID, mDatas[position].artId)
                    .putExtra(OConstantKey.TYPE, ArticleDetailsActivity.TYPE_CODE)
                    .putExtra(OConstantKey.TITLE, mDatas[position].title)
            )
        }

        codeModel.datas.observe(this) { response ->
            handleDate(response)
        }
    }

    protected fun lazyLoadData() {
        mPage = 1
        getCodeDatas()
    }

    private fun getCodeDatas() {
        lifecycleScope.launch {
            codeModel.getCodeDatas(mPage, ROW, mType)
        }
    }

    override fun getData() {
        mPage = 1
        getCodeDatas()
    }

    override fun getDataMore() {
        mPage = mDatas.size / ROW + 1
        getCodeDatas()
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