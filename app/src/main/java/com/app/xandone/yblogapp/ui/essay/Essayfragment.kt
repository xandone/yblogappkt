package com.app.xandone.yblogapp.ui.essay

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.xandone.baselib.imageload.ImageLoadHelper
import com.app.xandone.baselib.utils.JsonUtils.json2List
import com.app.xandone.baselib.utils.SimpleUtils
import com.app.xandone.widgetlib.utils.SizeUtils.dp2px
import com.app.xandone.widgetlib.utils.SpacesItemDecoration
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.base.BaseListFragment
import com.app.xandone.yblogapp.constant.OConstantKey
import com.app.xandone.yblogapp.model.base.BaseResponse
import com.app.xandone.yblogapp.model.bean.BannerBean
import com.app.xandone.yblogapp.model.bean.EssayArticleBean
import com.app.xandone.yblogapp.model.repository.ApiEmptyResponse
import com.app.xandone.yblogapp.model.repository.ApiErrorResponse
import com.app.xandone.yblogapp.model.repository.ApiOtherErrorResponse
import com.app.xandone.yblogapp.model.repository.HttpResult
import com.app.xandone.yblogapp.ui.articledetails.ArticleDetailsActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.gson.reflect.TypeToken
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener
import kotlinx.android.synthetic.main.frag_base_list.*
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

/**
 * author: Admin
 * created on: 2020/9/3 16:20
 * description:
 */
class Essayfragment : BaseListFragment() {

    private lateinit var mAdapter: BaseQuickAdapter<EssayArticleBean, BaseViewHolder>
    private lateinit var mDatas: ArrayList<EssayArticleBean>
    private lateinit var bannerAdapter: BannerImageAdapter<BannerBean>
    private lateinit var bannerList: ArrayList<BannerBean>

    private var mPage = 1

    private val essayModel by lazy {
        ViewModelProvider(this, EssayModelFactory()).get(
            EssayModel::class.java
        )
    }


    override fun initView(view: View) {
        super.initView(view)
        mDatas = ArrayList()
        bannerList = ArrayList()
        mAdapter = object :
            BaseQuickAdapter<EssayArticleBean, BaseViewHolder>(R.layout.item_essay_list, mDatas) {
            override fun convert(baseViewHolder: BaseViewHolder,
                                 essayArticleBean: EssayArticleBean) {
                baseViewHolder.setText(R.id.essay_title_tv, essayArticleBean.title)
                baseViewHolder.setText(R.id.essay_content_tv, essayArticleBean.content)
                baseViewHolder.setText(R.id.essay_date_tv, essayArticleBean.postTime)
                val codeCoverImg = baseViewHolder.getView<ImageView>(R.id.essay_cover_img)
                val imgRecycler = baseViewHolder.getView<RecyclerView>(R.id.img_recycler)
                val coverData = json2List<String>(
                    essayArticleBean.coverImg,
                    object : TypeToken<List<String?>?>() {}.type
                )
                if (coverData == null || coverData.isEmpty()) {
                    codeCoverImg.visibility = View.GONE
                    imgRecycler.visibility = View.GONE
                } else if (coverData.size == 1) {
                    codeCoverImg.visibility = View.VISIBLE
                    imgRecycler.visibility = View.GONE
                    ImageLoadHelper.instance.display(App.sContext, coverData[0], codeCoverImg)
                } else {
                    codeCoverImg.visibility = View.GONE
                    imgRecycler.visibility = View.VISIBLE
                    val imgAdapter: BaseQuickAdapter<String, BaseViewHolder> = object :
                        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_img, coverData) {
                        override fun convert(baseViewHolder: BaseViewHolder, s: String) {
                            val img = baseViewHolder.getView<ImageView>(R.id.item_essay_cover_img)
                            ImageLoadHelper.instance.display(App.sContext, s, img)
                        }
                    }
                    imgRecycler.adapter = imgAdapter
                    imgRecycler.layoutManager = LinearLayoutManager(
                        mActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                }
            }
        }
        initBanner()
        recycler.layoutManager = LinearLayoutManager(mActivity)
        recycler.addItemDecoration(SpacesItemDecoration(App.sContext!!, 10, 10, 10))
        recycler.adapter = mAdapter
        mAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            startActivity(
                Intent(mActivity, ArticleDetailsActivity::class.java)
                    .putExtra(OConstantKey.ID, mDatas.get(position)!!.essayId)
                    .putExtra(
                        OConstantKey.TYPE,
                        ArticleDetailsActivity.TYPE_ESSAY
                    )
                    .putExtra(OConstantKey.TITLE, mDatas.get(position).title)
            )
        })



        essayModel.datas.observe(this) { response ->
            if (response.result == HttpResult.SUCCESS && response.data != null) {
                if (mPage == 0) {
                    mAdapter.setList(response.data)
                    if (response.total == 0) {
                        mStateLayout.showEmpty(
                            ApiEmptyResponse<Any>()
                        )
                        return@observe
                    }
                } else {
                    mAdapter.addData(response.data)
                }
                if (response.total <= mDatas?.size!!) {
                    mBinding.refreshLayout.finishLoadMoreWithNoMoreData()
                } else {
                    mBinding.refreshLayout.finishLoadMore()
                }

                mStateLayout.showContent()
            } else {
                when (response) {
                    is ApiEmptyResponse -> {
                        mStateLayout.showEmpty(response)
                    }
                    is ApiErrorResponse -> {
                        mStateLayout.showError(response)
                    }
                    is ApiOtherErrorResponse -> {
                        mStateLayout.showError(response)
                    }
                    else -> {
                        mStateLayout.showError(
                            ApiOtherErrorResponse<Any>(
                                Exception(),
                                -1000,
                                "未知异常"
                            )
                        )
                    }
                }

                if (mPage != 0) {
                    mBinding.refreshLayout.finishLoadMore(false)
                }
            }

            mBinding.refreshLayout.finishRefresh()
        }

        essayModel.datas2.observe(this) {
            bannerList.clear()
            bannerList.addAll(it.data!!)
            bannerAdapter.notifyDataSetChanged()
        }

        requestData()
    }

    private fun initBanner() {
        val inflater = LayoutInflater.from(App.sContext)
        val view: View = inflater.inflate(R.layout.v_essay_banner_layout, null)
        val banner = view.findViewById<Banner<*, *>>(R.id.banner)
        val params = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp2px(App.sContext!!, 200f)
        )
        banner.layoutParams = params
        bannerAdapter = object : BannerImageAdapter<BannerBean>(bannerList) {
            override fun onBindView(
                holder: BannerImageHolder,
                data: BannerBean,
                position: Int,
                size: Int
            ) {
                ImageLoadHelper.instance.display(
                    mActivity,
                    bannerList[position].imgUrl,
                    holder.imageView
                )
            }
        }
        banner.adapter = bannerAdapter
        banner.addBannerLifecycleObserver(this).indicator = CircleIndicator(mActivity)
        mAdapter.addHeaderView(banner)
        bannerAdapter.setOnBannerListener(OnBannerListener<BannerBean?> { bannerBean, position ->
            startActivity(
                Intent(mActivity, ArticleDetailsActivity::class.java)
                    .putExtra(OConstantKey.ID, bannerList[position].articelId)
                    .putExtra(
                        OConstantKey.TYPE,
                        ArticleDetailsActivity.TYPE_ESSAY
                    )
                    .putExtra(OConstantKey.TITLE, bannerList[position].title)
            )
        })
    }

    fun requestData() {
        getCodeDatas(1, false)
        getBannerDatas()
    }

    private fun getBannerDatas() {
        lifecycleScope.launch {
            essayModel.getBannerDatas()
        }
    }

    private fun getCodeDatas(page: Int, isLoadMore: Boolean) {
        lifecycleScope.launch {
            essayModel.getEssayDatas(isLoadMore, page, ROW)
        }
    }

    override fun getData() {
        getCodeDatas(1, false)
        getBannerDatas()
    }

    override fun getDataMore() {
        getCodeDatas(mDatas.size / ROW + 1, true)
    }

    companion object {
        private const val ROW = 10
        val instance: Essayfragment = Essayfragment()
    }
}