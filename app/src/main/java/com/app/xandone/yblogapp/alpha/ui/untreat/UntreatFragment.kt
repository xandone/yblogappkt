package com.xandone.manager2.ui.untreat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.databinding.RefreshRvLayoutBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xandone.manager2.model.bean.UntreateBean
import com.xandone.manager2.model.repository.ApiEmptyResponse
import com.xandone.manager2.model.repository.ApiErrorResponse
import com.xandone.manager2.model.repository.ApiOtherErrorResponse
import com.xandone.manager2.model.repository.HttpResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * @author: xiao
 * created on: 2023/1/31 15:53
 * description:
 */
class UntreatFragment : Fragment() {

    private var datas: MutableList<UntreateBean>? = null

    private lateinit var mBinding: RefreshRvLayoutBinding

    private var mAdapter: BaseQuickAdapter<UntreateBean, BaseViewHolder>? = null

    private val viewModel by lazy {
        ViewModelProvider(this, UntreatViewModelFactory()).get(
            UntreatViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = RefreshRvLayoutBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        viewModel.datas.observe(this) { response ->
            if (response.result == HttpResult.SUCCESS && response.data != null) {
                mAdapter?.setList(response.data.items)
            } else {
                when (response) {
                    is ApiErrorResponse -> Log.d("gdfgdf", "API异常error")
                    is ApiOtherErrorResponse -> Log.d("gdfgdf", "加载异常error")
                    is ApiEmptyResponse -> Log.d("gdfgdf", "空数据")
                }
            }

            mBinding.swipeRefresh.isRefreshing = false
        }

        loadData()

    }

    private fun init() {
        datas = mutableListOf()

        mBinding.swipeRefresh.setOnRefreshListener {
            loadData()
        }

        mAdapter = object : BaseQuickAdapter<UntreateBean, BaseViewHolder>(
            R.layout.item_message_layout, datas
        ) {
            override fun convert(holder: BaseViewHolder, item: UntreateBean) {
                holder.setText(R.id.title, "这是标题${item.content}")
            }
        }

        mBinding.recycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.recycler.adapter = mAdapter

    }

    private fun loadData() {
        lifecycleScope.launch {

            flow {
                emit(viewModel.getList(148))
            }
                .onStart { }
                .onCompletion { }
                .collect {}

        }

    }

    companion object {
        var count = 0
        fun getInstance(count: Int): UntreatFragment {
            this.count = count
            return UntreatFragment()
        }
    }
}