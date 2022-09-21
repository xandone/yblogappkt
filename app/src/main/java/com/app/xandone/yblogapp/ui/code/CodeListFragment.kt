package com.app.xandone.yblogapp.ui.code

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.xandone.baselib.imageload.ImageLoadHelper
import com.app.xandone.widgetlib.utils.SpacesItemDecoration
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.base.BaseListFragment
import com.app.xandone.yblogapp.constant.OConstantKey
import com.app.xandone.yblogapp.model.CodeModel
import com.app.xandone.yblogapp.model.bean.CodeArticleBean
import com.app.xandone.yblogapp.rx.IRequestCallback
import com.app.xandone.yblogapp.ui.articledetails.ArticleDetailsActivity
import com.app.xandone.yblogapp.viewmodel.ModelProvider
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.frag_base_list.*
import kotlin.collections.ArrayList

/**
 * author: Admin
 * created on: 2020/8/31 17:55
 * description:
 */
class CodeListFragment : BaseListFragment() {
    private var codeModel: CodeModel? = null
    private lateinit var mAdapter: BaseQuickAdapter<CodeArticleBean, BaseViewHolder>
    private lateinit var datas: ArrayList<CodeArticleBean>
    private var mType = 0
    private var mPage = 1
    protected var mIsLoadedData = false

    override fun initView(view: View) {
        super.initView(view)
        mType = arguments!!.getInt(TYPE)
        datas = ArrayList()
        mAdapter = object :
            BaseQuickAdapter<CodeArticleBean, BaseViewHolder>(
                R.layout.item_code_list,
                datas as MutableList<CodeArticleBean>?
            ) {
            override fun convert(
                baseViewHolder: BaseViewHolder,
                codeArticleBean: CodeArticleBean
            ) {
                baseViewHolder.setText(R.id.code_title_tv, codeArticleBean.title)
                baseViewHolder.setText(R.id.code_type_tv, codeArticleBean.typeName)
                baseViewHolder.setText(R.id.code_content_tv, codeArticleBean.content)
                baseViewHolder.setText(R.id.code_date_tv, codeArticleBean.postTime)
                val codeCoverImg =
                    baseViewHolder.getView<ImageView>(R.id.code_cover_img)
                if (TextUtils.isEmpty(codeArticleBean.coverImg)) {
                    codeCoverImg.visibility = View.GONE
                } else {
                    codeCoverImg.visibility = View.VISIBLE
                    ImageLoadHelper.instance
                        .display(App.sContext, codeArticleBean.coverImg, codeCoverImg)
                }
            }
        }
        recycler.layoutManager = LinearLayoutManager(mActivity)
        recycler.addItemDecoration(SpacesItemDecoration(App.sContext!!, 10, 10, 10))
        recycler.adapter = mAdapter
        mAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            startActivity(
                Intent(mActivity, ArticleDetailsActivity::class.java)
                    .putExtra(
                        OConstantKey.ID,
                        datas[position].artId
                    )
                    .putExtra(
                        OConstantKey.TYPE,
                        ArticleDetailsActivity.Companion.TYPE_CODE
                    )
                    .putExtra(
                        OConstantKey.TITLE,
                        datas[position].title
                    )
            )
        })

        codeModel = ModelProvider.getModel(
            mActivity,
            CodeModel::class.java,
            App.sContext
        )
    }

    protected fun lazyLoadData() {
        mPage = 1
        requestData()
    }

    override fun requestData() {
        getCodeDatas(false)
    }

    private fun getCodeDatas(isLoadMore: Boolean) {
        codeModel!!.getCodeDatas(
            mPage,
            ROW,
            mType,
            isLoadMore,
            object : IRequestCallback<List<CodeArticleBean>> {
                override fun success(beans: List<CodeArticleBean>) {
                    datas = beans as ArrayList<CodeArticleBean>
                    mAdapter.setList(datas)
                    onLoadFinish()
                    if (!isLoadMore) {
                        finishRefresh()
                        if (beans.isEmpty()) {
                            onLoadEmpty()
                        }
                    } else {
                        finishLoadMore()
                    }
                }

                override fun error(message: String?, statusCode: Int) {
                    onLoadStatus(statusCode)
                }
            })
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