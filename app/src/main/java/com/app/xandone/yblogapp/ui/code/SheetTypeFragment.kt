package com.app.xandone.yblogapp.ui.code

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.app.xandone.baselib.cache.SpHelper.save2DefaultSp
import com.app.xandone.baselib.event.SimplEvent
import com.app.xandone.baselib.utils.JsonUtils.obj2Json
import com.app.xandone.widgetlib.utils.SpacesItemDecoration
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.config.AppConfig
import com.app.xandone.yblogapp.constant.OConstantKey
import com.app.xandone.yblogapp.constant.OSpKey
import com.app.xandone.yblogapp.model.bean.CodeTypeBean
import com.app.xandone.yblogapp.model.event.CodeTypeEvent
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlinx.android.synthetic.main.frag_sheet_type.*
import kotlin.collections.ArrayList

/**
 * author: Admin
 * created on: 2020/9/7 15:41
 * description:
 */
class SheetTypeFragment : BottomSheetDialogFragment() {
    private lateinit var mAdapter: DelegateMultiAdapter
    private lateinit var mRemoveAdapter: BaseQuickAdapter<CodeTypeBean?, BaseViewHolder>
    private lateinit var types: ArrayList<CodeTypeBean>
    private var removeTypes: ArrayList<CodeTypeBean?>? = null
    private var mItemHelper: ItemTouchHelper? = null

    //是否为编辑状态
    private var isEditState = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_sheet_type, container, false)
        val height = (AppConfig.SCREEN_HEIGHT * 0.8).toInt()
        val layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
        view.layoutParams = layoutParams
        return view
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        if (arguments == null) {
            return
        }
        types = ArrayList(arguments!!.getParcelableArrayList(OConstantKey.DATA))
        removeTypes = ArrayList(arguments!!.getParcelableArrayList(OConstantKey.DATA2))
        initItemTouchHelper()
        type_recycler.layoutManager = GridLayoutManager(activity, 3)
        type_recycler.addItemDecoration(SpacesItemDecoration(App.Companion.sContext!!, 10, 10, 10))
        mAdapter = DelegateMultiAdapter(types)
        mAdapter.addChildClickViewIds(R.id.type_del_iv)
        mAdapter.setOnItemChildClickListener(OnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.type_del_iv) {
                removeTypes!!.add(types!!.get(position))
                types!!.removeAt(position)
                mAdapter.notifyItemRemoved(position)
                mRemoveAdapter.notifyDataSetChanged()
            }
        })
        type_recycler.adapter = mAdapter
        initRemoveRecycler()
    }

    private fun initRemoveRecycler() {
        type_remove_recycler.layoutManager = GridLayoutManager(activity, 3)
        type_remove_recycler.addItemDecoration(
            SpacesItemDecoration(
                App.Companion.sContext!!,
                10,
                10,
                10
            )
        )
        mRemoveAdapter = object : BaseQuickAdapter<CodeTypeBean?, BaseViewHolder>(
            R.layout.item_remove_code_type,
            removeTypes
        ) {
            protected override fun convert(
                baseViewHolder: BaseViewHolder,
                bean: CodeTypeBean?
            ) {
                baseViewHolder.setText(R.id.type_tv, bean!!.typeName)
                baseViewHolder.setGone(R.id.type_del_iv, !isEditState)
            }
        }
        type_remove_recycler.adapter = mRemoveAdapter
        mRemoveAdapter.addChildClickViewIds(R.id.type_del_iv)
        mRemoveAdapter.setOnItemChildClickListener(OnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.type_del_iv) {
                removeTypes!![position]?.let { types!!.add(it) }
                removeTypes!!.removeAt(position)
                mRemoveAdapter.notifyItemRemoved(position)
                mAdapter.notifyDataSetChanged()
            }
        })
    }

    /**
     * 缓存自定义的"我的频道"
     */
    private fun save2Cache() {
        save2DefaultSp(
            App.Companion.sContext!!,
            OSpKey.CODE_TYPE_KEY,
            obj2Json(types)
        )
    }

    @OnClick(R.id.edit_tv)
    fun click(view: View) {
        when (view.id) {
            R.id.edit_tv -> {
                isEditState = !isEditState
                edit_tv!!.text = if (isEditState) "完成" else "编辑"
                mAdapter.notifyDataSetChanged()
                mRemoveAdapter.notifyDataSetChanged()
                if (!isEditState) {
                    EventBus.getDefault().post(CodeTypeEvent(types))
                    save2Cache()
                }
            }
            else -> {
            }
        }
    }

    private fun loadShakeAnim(view: View) {
        val animation = AnimationUtils.loadAnimation(
            App.Companion.sContext,
            R.anim.shake_anim
        )
        animation.interpolator = LinearInterpolator()
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.REVERSE
        view.startAnimation(animation)
    }

    private fun cancelShakeAnim(view: View) {
        view.clearAnimation()
    }

    private fun initItemTouchHelper() {
        mItemHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return if (recyclerView.layoutManager is GridLayoutManager) {
                    val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    val swipeFlags = 0
                    makeMovementFlags(
                        dragFlags,
                        swipeFlags
                    )
                } else {
                    val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                    val swipeFlags = 0
                    makeMovementFlags(
                        dragFlags,
                        swipeFlags
                    )
                }
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                //得到当拖拽的viewHolder的Position
                val fromPosition = viewHolder.adapterPosition
                //拿到当前拖拽到的item的viewHolder
                val toPosition = target.adapterPosition
                if (fromPosition < toPosition) {
                    for (i in fromPosition until toPosition) {
                        Collections.swap(types, i, i + 1)
                    }
                } else {
                    for (i in fromPosition downTo toPosition + 1) {
                        Collections.swap(types, i, i - 1)
                    }
                }
                mAdapter!!.notifyItemMoved(fromPosition, toPosition)
                return true
            }

            //拖动完成
            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
//                Toast.makeText(MainActivity.this, "拖拽完成 方向" + direction, Toast.LENGTH_SHORT).show();
            }

            //选中
            override fun onSelectedChanged(
                viewHolder: RecyclerView.ViewHolder?,
                actionState: Int
            ) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder!!.itemView.setBackgroundColor(Color.LTGRAY)
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.setBackgroundColor(0)
            }

            //重写拖拽不可用
            override fun isLongPressDragEnabled(): Boolean {
                return false
            }
        })
        mItemHelper!!.attachToRecyclerView(type_recycler)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageReceived(event: SimplEvent?) {
    }

    internal inner class DelegateMultiAdapter(datas: ArrayList<CodeTypeBean>) :
        BaseDelegateMultiAdapter<CodeTypeBean, BaseViewHolder>(datas) {
        override fun convert(helper: BaseViewHolder, bean: CodeTypeBean) {
            when (helper.itemViewType) {
                0 -> helper.setText(R.id.type_tv, bean.typeName)
                1 -> {
                    helper.setText(R.id.type_tv, bean.typeName)
                    helper.setGone(R.id.type_del_iv, !isEditState)
                    if (isEditState) {
                        loadShakeAnim(helper.itemView)
                    } else {
                        cancelShakeAnim(helper.itemView)
                    }
                    helper.getView<View>(R.id.type_tv)
                        .setOnLongClickListener {
                            mItemHelper!!.startDrag(helper)
                            if (!isEditState) {
                                isEditState = true
                                edit_tv.text = "完成"
                                mAdapter.notifyDataSetChanged()
                                mRemoveAdapter.notifyDataSetChanged()
                            }
                            true
                        }
                }
                else -> {
                }
            }
        }

        init {
            setMultiTypeDelegate(object : BaseMultiTypeDelegate<CodeTypeBean>() {
                override fun getItemType(
                    data: List<CodeTypeBean>,
                    position: Int
                ): Int {
                    return if (position == 0) 0 else 1
                }
            })
            getMultiTypeDelegate()
                ?.addItemType(0, R.layout.item_code_enable_type)
                ?.addItemType(1, R.layout.item_code_type)
        }
    }

    companion object {
        fun getInstance(
            codeTypeBeans: ArrayList<CodeTypeBean>?,
            removeBeans: ArrayList<CodeTypeBean>?
        ): SheetTypeFragment {
            val fragment = SheetTypeFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(OConstantKey.DATA, codeTypeBeans)
            bundle.putParcelableArrayList(OConstantKey.DATA2, removeBeans)
            fragment.arguments = bundle
            return fragment
        }
    }
}