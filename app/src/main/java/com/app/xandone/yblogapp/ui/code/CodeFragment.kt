package com.app.xandone.yblogapp.ui.code

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import butterknife.OnClick
import com.app.xandone.baselib.cache.SpHelper.getDefaultString
import com.app.xandone.baselib.cache.SpHelper.save2DefaultSp
import com.app.xandone.baselib.utils.JsonUtils
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.base.BaseWallFragment
import com.app.xandone.yblogapp.constant.OSpKey
import com.app.xandone.yblogapp.model.CodeTypeModel
import com.app.xandone.yblogapp.model.bean.CodeTypeBean
import com.app.xandone.yblogapp.model.event.CodeTypeEvent
import com.app.xandone.yblogapp.rx.IRequestCallback
import com.app.xandone.yblogapp.viewmodel.ModelProvider
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

import kotlinx.android.synthetic.main.frag_code.*

/**
 * author: Admin
 * created on: 2020/9/3 09:56
 * description:
 */
class CodeFragment : BaseWallFragment() {
    private lateinit var fragments: ArrayList<Fragment>
    private var mSheetTypeFragment: SheetTypeFragment? = null
    private lateinit var mCodeTypeModel: CodeTypeModel
    private lateinit var codeTypeList: ArrayList<CodeTypeBean>
    private var apiTypeList = mutableListOf<CodeTypeBean>()
    private var removeTypes: ArrayList<CodeTypeBean>? = null
    private var mTabLayoutAdapter: CommonNavigatorAdapter? = null
    private var vpAdapter: MyViewPagerAdapter? = null
    override fun getLayout(): Int {
        return R.layout.frag_code
    }

    override fun init(view: View?) {
        apiTypeList = ArrayList()
        codeTypeList = ArrayList()
        removeTypes = ArrayList()
    }

    override fun initDataObserver() {
        mCodeTypeModel = ModelProvider.getModel(
            mActivity,
            CodeTypeModel::class.java,
            App.sContext
        )
        requestData()
    }

    override fun requestData() {
        mCodeTypeModel.getCodeTypeDatas(object :
            IRequestCallback<List<CodeTypeBean>> {
            override fun success(codeTypeBeans: List<CodeTypeBean>) {
                initType(codeTypeBeans)
                onLoadFinish()
            }

            override fun error(message: String?, statusCode: Int) {
                onLoadStatus(statusCode)
            }
        })
    }

    fun initType(codeTypeBeans: List<CodeTypeBean>) {
        apiTypeList.addAll(codeTypeBeans)
        dealCacheType()
        initTabLayout()
        fragments = ArrayList()
        for (i in codeTypeList.indices) {
            fragments.add(CodeListFragment.getInstance(codeTypeList[i].type))
        }
        vpAdapter = MyViewPagerAdapter(
            childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewPager.adapter = vpAdapter
    }

    private fun initTabLayout() {
        val commonNavigator = CommonNavigator(mActivity)
        mTabLayoutAdapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return codeTypeList.size
            }

            override fun getTitleView(
                context: Context,
                index: Int
            ): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor = Color.GRAY
                colorTransitionPagerTitleView.selectedColor = ContextCompat.getColor(
                    mActivity,
                    R.color.colorPrimary
                )
                colorTransitionPagerTitleView.text = codeTypeList[index].typeName
                colorTransitionPagerTitleView.setOnClickListener {
                    viewPager!!.currentItem = index
                }
                return colorTransitionPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                indicator.setColors(ContextCompat.getColor(mActivity, R.color.colorPrimary))
                return indicator
            }
        }
        commonNavigator.adapter = mTabLayoutAdapter
        magic_indicator.navigator = commonNavigator
        ViewPagerHelper.bind(magic_indicator, viewPager)
    }

    fun showDialogFrag() {
        mSheetTypeFragment = SheetTypeFragment.getInstance(codeTypeList, removeTypes)
        mSheetTypeFragment!!.show(childFragmentManager, "demoBottom")
    }

    @OnClick(R.id.add_type_iv)
    fun click(view: View) {
        showDialogFrag()
    }

    private fun dealCacheType() {
        val codeStr =
            getDefaultString(App.sContext!!, OSpKey.CODE_TYPE_KEY)

        if (codeStr.isNullOrEmpty()) {
            codeTypeList.addAll(apiTypeList)
            return
        }

        val cacheList: List<CodeTypeBean>? = JsonUtils.json2List(
            codeStr,
            Array<CodeTypeBean>::class.java
        )
        if (cacheList!!.isEmpty()) {
            codeTypeList.addAll(apiTypeList)
            return
        }

        //找到缓存的类型和接口返回的类型相同的type部分
        for (bean in cacheList) {
            for (bean2 in apiTypeList) {
                if (bean.type == bean2.type) {
                    codeTypeList.add(bean)
                    break
                }
            }
        }
        //找到不存在当前tabLayout的type值，此为被移除的类型。
        for (bean in apiTypeList) {
            var hasId = false
            for (bean2 in codeTypeList) {
                if (bean.type == bean2.type) {
                    hasId = true
                    break
                }
            }
            if (!hasId) {
                removeTypes!!.add(bean)
            }
        }
        save2DefaultSp(
            App.sContext!!,
            OSpKey.CODE_TYPE_KEY,
            JsonUtils.obj2Json(codeTypeList)
        )
    }

    internal inner class MyViewPagerAdapter(
        fm: FragmentManager,
        behavior: Int
    ) : FragmentStatePagerAdapter(fm, behavior) {
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCodeTypeEvent(event: CodeTypeEvent) {
        codeTypeList.clear()
        codeTypeList.addAll(event.list)
        removeTypes!!.clear()
        //找到不存在当前tabLayout的type值，此为被移除的类型。
        for (bean in apiTypeList) {
            var hasId = false
            for (bean2 in codeTypeList) {
                if (bean.type == bean2.type) {
                    hasId = true
                    break
                }
            }
            if (!hasId) {
                removeTypes!!.add(bean)
            }
        }
        fragments.clear()
        for (i in codeTypeList.indices) {
            fragments.add(CodeListFragment.Companion.getInstance(codeTypeList[i].type))
        }
        mTabLayoutAdapter!!.notifyDataSetChanged()
        vpAdapter!!.notifyDataSetChanged()
    }
}