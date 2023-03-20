package com.app.xandone.yblogapp.ui.code

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.xandone.baselib.cache.SpHelper.getDefaultString
import com.app.xandone.baselib.cache.SpHelper.save2DefaultSp
import com.app.xandone.baselib.utils.JsonUtils
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.base.BaseWallFragment
import com.app.xandone.yblogapp.constant.OSpKey
import com.app.xandone.yblogapp.databinding.FragCodeBinding
import com.app.xandone.yblogapp.model.bean.CodeTypeBean
import com.app.xandone.yblogapp.model.event.CodeTypeEvent
import com.app.xandone.yblogapp.ui.code.list.CodeListFragment
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.frag_code.*
import kotlinx.coroutines.launch
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

/**
 * author: Admin
 * created on: 2020/9/3 09:56
 * description:
 */
class CodeFragment : BaseWallFragment<FragCodeBinding>(), View.OnClickListener {

    private lateinit var fragments: ArrayList<Fragment>
    private var mSheetTypeFragment: SheetTypeFragment? = null
    private lateinit var codeTypeList: ArrayList<CodeTypeBean>
    private lateinit var apiTypeList: ArrayList<CodeTypeBean>
    private var removeTypes: ArrayList<CodeTypeBean>? = null
    private var mTabLayoutAdapter: CommonNavigatorAdapter? = null

    //by lazy 仅在第一次使用的时候初始化
    private val vpAdapter: MyViewPagerAdapter by lazy {
        MyViewPagerAdapter(
            childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
    }

    private val codeTypeModel by lazy {
        ViewModelProvider(this, CodeTypeModelFactory()).get(
            CodeTypeModel::class.java
        )
    }

    companion object {
        val instance = CodeFragment()
    }

    override fun initView(view: View) {
        apiTypeList = ArrayList()
        codeTypeList = ArrayList()
        removeTypes = ArrayList()

        mBinding.addTypeIv.setOnClickListener(this)

        codeTypeModel.datas.observe(this) { response ->
            if (response?.data != null) {
                initType(response.data)
                return@observe
            }
        }

        lifecycleScope.launch {
            codeTypeModel.getCodeTypeDatas()
        }

    }

    fun initType(codeTypeBeans: List<CodeTypeBean>) {
        apiTypeList.addAll(codeTypeBeans)
        dealCacheType()
        initTabLayout()
        fragments = ArrayList()
        for (i in codeTypeList.indices) {
            fragments.add(CodeListFragment.getInstance(codeTypeList[i].type))
        }
        mBinding.viewPager.adapter = vpAdapter
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
                val titleView = ColorTransitionPagerTitleView(context)
                titleView.normalColor = Color.GRAY
                titleView.selectedColor = ContextCompat.getColor(
                    mActivity,
                    R.color.colorPrimary
                )
                titleView.text = codeTypeList[index].typeName
                titleView.setOnClickListener {
                    mBinding.viewPager.currentItem = index
                }
                return titleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                indicator.setColors(ContextCompat.getColor(mActivity, R.color.colorPrimary))
                return indicator
            }
        }
        commonNavigator.adapter = mTabLayoutAdapter
        mBinding.magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(mBinding.magicIndicator, mBinding.viewPager)
    }

    fun showDialogFrag() {
        mSheetTypeFragment = SheetTypeFragment.getInstance(codeTypeList, removeTypes)
        mSheetTypeFragment!!.show(childFragmentManager, "demoBottom")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_type_iv -> showDialogFrag()
            else -> {
            }
        }
    }


    private fun dealCacheType() {
        val codeStr =
            getDefaultString(App.sContext, OSpKey.CODE_TYPE_KEY)

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
        fm: FragmentManager, behavior: Int
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
            fragments.add(CodeListFragment.getInstance(codeTypeList[i].type))
        }
        mTabLayoutAdapter!!.notifyDataSetChanged()
        vpAdapter.notifyDataSetChanged()
    }

    override fun isRegistEventBus(): Boolean {
        return true
    }

    override fun isStatusBarEnabled(): Boolean {
        return true
    }

    override fun getTitleView(): View {
        return mBinding.magicIndicatorCl
    }

    override fun statusBarConfig(): ImmersionBar {
        return ImmersionBar.with(this)
            // 默认状态栏字体颜色为黑色
            .statusBarDarkFont(true)
            .statusBarColor(R.color.app_bg_color)
            // 指定导航栏背景颜色
//                .navigationBarColor(android.R.color.white)
            // 状态栏字体和导航栏内容自动变色，必须指定状态栏颜色和导航栏颜色才可以自动变色
            .autoDarkModeEnable(true, 0.2f);
    }

    override fun initVB(): FragCodeBinding {
        return FragCodeBinding.inflate(layoutInflater)
    }
}