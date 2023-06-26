package com.app.xandone.yblogapp.ui.code.list

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import kotlinx.coroutines.*

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
        mBinding.recycler.layoutManager = LinearLayoutManager(mActivity)
        mBinding.recycler.addItemDecoration(SpacesItemDecoration(App.sContext, 10, 10, 10))
        mBinding.recycler.adapter = mAdapter
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
            val l1 = System.currentTimeMillis()

            //个人总结
            //1.launch和async都可以达到异步的效果,但是async有await挂起函数，这样可以等子协程结束后再执行接下来下面的代码
            //2.这里如果创建子协程，那么所有代码同步执行，适合场景：等A接口结束后拿到参数再执行B接口
            //3.launch适用场景：一个界面多个接口同时请求且互不干扰的情况
            //4.async适用场景：一个界面多个接口同时请求，但是必须等待所有接口成功才能显示的情况
            val result1 = launch {
                codeModel.getCodeDatas(mPage, ROW, mType)
            }
            val l2 = System.currentTimeMillis()
            val result2 = async {
                codeModel.getCodeDatas(mPage, 100, mType)
            }
            result2.await()
            val l3 = System.currentTimeMillis()
            Log.e("gfdgdfgdfgd", "${l2 - l1},${l3 - l2}")
//            Log.e("gfdgdfgdfgd", "await=${result1.await()},${result2.await()}")

            val l4 = System.currentTimeMillis()
            Log.e("gfdgdfgdfgd", "${l4 - l3}")
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