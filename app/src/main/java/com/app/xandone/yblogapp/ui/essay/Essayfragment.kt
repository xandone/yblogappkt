package com.app.xandone.yblogapp.ui.essay

import android.content.Intent
import android.util.Log
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
import com.app.xandone.widgetlib.utils.SizeUtils.dp2px
import com.app.xandone.widgetlib.utils.SpacesItemDecoration
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.base.BaseListFragment
import com.app.xandone.yblogapp.constant.OConstantKey
import com.app.xandone.yblogapp.model.bean.BannerBean
import com.app.xandone.yblogapp.model.bean.EssayArticleBean
import com.app.xandone.yblogapp.model.repository.ApiEmptyResponse
import com.app.xandone.yblogapp.model.repository.ApiErrorResponse
import com.app.xandone.yblogapp.model.repository.ApiOtherErrorResponse
import com.app.xandone.yblogapp.model.repository.HttpResult
import com.app.xandone.yblogapp.ui.articledetails.ArticleDetailsActivity
import com.app.xandone.yblogapp.view.statelayout.StateLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.frag_base_list.*
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

/**
 * author: Admin
 * created on: 2020/9/3 16:20
 * description:
 */
class Essayfragment : BaseListFragment<EssayArticleBean>() {

    private lateinit var bannerAdapter: BannerImageAdapter<BannerBean>

    private val bannerList: ArrayList<BannerBean> by lazy {
        ArrayList()
    }

    private val essayModel by lazy {
        ViewModelProvider(this, EssayModelFactory()).get(
            EssayModel::class.java
        )
    }

    override fun initView(view: View) {
        super.initView(view)
        onLoading()

        initAdapter()
        initBanner()

        essayModel.datas.observe(this) { response ->
            handleDate(response)
        }

        essayModel.datas2.observe(this) {
            if (it.result == HttpResult.SUCCESS && !it.data.isNullOrEmpty()) {
                bannerList.clear()
                bannerList.addAll(it.data)
                bannerAdapter.notifyDataSetChanged()
            }
        }

        getData()
    }

    private fun initAdapter() {
        mAdapter = object :
            BaseQuickAdapter<EssayArticleBean, BaseViewHolder>(R.layout.item_essay_list, mDatas) {
            override fun convert(holder: BaseViewHolder,
                                 item: EssayArticleBean) {
                holder.setText(R.id.essay_title_tv, item.title)
                holder.setText(R.id.essay_content_tv, item.content)
                holder.setText(R.id.essay_date_tv, item.postTime)
                val codeCoverImg = holder.getView<ImageView>(R.id.essay_cover_img)
                val imgRecycler = holder.getView<RecyclerView>(R.id.img_recycler)
                val coverData = json2List<String>(
                    item.coverImg,
                    object : TypeToken<List<String?>?>() {}.type
                )
                if (coverData.isNullOrEmpty()) {
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

        recycler.layoutManager = LinearLayoutManager(mActivity)
        recycler.addItemDecoration(SpacesItemDecoration(App.sContext, 10, 10, 10))
        recycler.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, _, position ->
            startActivity(
                Intent(mActivity, ArticleDetailsActivity::class.java)
                    .putExtra(OConstantKey.ID, mDatas[position].essayId)
                    .putExtra(OConstantKey.TYPE, ArticleDetailsActivity.TYPE_ESSAY)
                    .putExtra(OConstantKey.TITLE, mDatas[position].title)
            )
        }
    }

    private fun initBanner() {
        val inflater = LayoutInflater.from(App.sContext)
        val view: View = inflater.inflate(R.layout.v_essay_banner_layout, null)
        val banner = view.findViewById<Banner<*, *>>(R.id.banner)
        val params = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp2px(App.sContext, 200f)
        )
        banner.layoutParams = params

        bannerAdapter = object : BannerImageAdapter<BannerBean>(bannerList) {
            override fun onBindView(
                holder: BannerImageHolder,
                data: BannerBean,
                position: Int,
                size: Int) {
                ImageLoadHelper.instance.display(mActivity,
                    bannerList[position].imgUrl,
                    holder.imageView)
            }
        }
        banner.adapter = bannerAdapter
        banner.addBannerLifecycleObserver(this).indicator = CircleIndicator(mActivity)
        mAdapter.addHeaderView(banner)
        bannerAdapter.setOnBannerListener { _, position ->
            startActivity(
                Intent(mActivity, ArticleDetailsActivity::class.java)
                    .putExtra(OConstantKey.ID, bannerList[position].articelId)
                    .putExtra(OConstantKey.TYPE, ArticleDetailsActivity.TYPE_ESSAY)
                    .putExtra(OConstantKey.TITLE, bannerList[position].title)
            )
        }
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
        mPage = 1
        getCodeDatas(mPage, false)
        getBannerDatas()
    }

    override fun getDataMore() {
        mPage = mDatas.size / ROW + 1
        getCodeDatas(mPage, true)
    }

    override fun isStatusBarEnabled(): Boolean {
        return true
    }

    override fun getTitleView(): View {
        return mStateLayout
    }

    companion object {
        private const val ROW = 10
        val instance: Essayfragment = Essayfragment()
    }

}