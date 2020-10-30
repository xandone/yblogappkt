package com.app.xandone.widgetlib.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.app.xandone.widgetlib.R

/**
 * author: Admin
 * created on: 2020/8/13 16:29
 * description:
 */
class LoadingLayout @JvmOverloads constructor(private val mContext: Context,
                                              attrs: AttributeSet? = null,
                                              defStyleAttr: Int = 0) :
    LinearLayout(mContext, attrs, defStyleAttr) {

    private lateinit var img_tip_logo: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var tv_tips: TextView
    private lateinit var bt_operate: TextView
    private lateinit var onReloadListener: OnReloadListener
    private var errorMsg: String? = ""
    var nullPic = 0
    fun initView(context: Context?) {
        View.inflate(context, R.layout.loading_tip_layout, this)
        img_tip_logo = findViewById(R.id.img_tip_logo)
        progressBar = findViewById(R.id.progress)
        tv_tips = findViewById(R.id.tv_tips)
        bt_operate = findViewById(R.id.bt_operate)
        visibility = View.GONE
        bt_operate.setOnClickListener(OnClickListener {
            onReloadListener.reLoadData()
        })
    }

    fun setTips(tips: String?) {
        tv_tips.text = tips
    }

    fun setLoadingStatus(loadStatus: Int) {
        when (loadStatus) {
            ILoadingStatus.NET_ERROR -> {
                visibility = View.VISIBLE
                img_tip_logo.visibility = View.VISIBLE
                stopProgress(mContext)
                bt_operate.visibility = View.VISIBLE
                if (TextUtils.isEmpty(errorMsg)) {
                    tv_tips.text = "数据加载失败"
                } else {
                    tv_tips.text = errorMsg
                }
                img_tip_logo.setImageResource(R.mipmap.icon_net_error)
            }
            ILoadingStatus.SERVER_ERROR -> {
                visibility = View.VISIBLE
                img_tip_logo.visibility = View.VISIBLE
                stopProgress(mContext)
                bt_operate.visibility = View.VISIBLE
                if (TextUtils.isEmpty(errorMsg)) {
                    tv_tips.text = "服务器异常"
                } else {
                    tv_tips.text = errorMsg
                }
                img_tip_logo.setImageResource(R.mipmap.icon_server_error)
            }
            ILoadingStatus.EMPTY -> {
                visibility = View.VISIBLE
                img_tip_logo.visibility = View.VISIBLE
                stopProgress(mContext)
                bt_operate.visibility = View.VISIBLE
                tv_tips.text = "暂无数据"
                if (nullPic <= 0) {
                    img_tip_logo.setImageResource(
                        R.mipmap.icon_net_nodata
                    )
                } else {
                    img_tip_logo.setImageResource(nullPic)
                }
            }
            ILoadingStatus.LOADING -> {
                visibility = View.VISIBLE
                img_tip_logo.visibility = View.GONE
                startProgress(mContext)
                bt_operate.visibility = View.GONE
                tv_tips.text = "加载.."
            }
            ILoadingStatus.FINISH -> visibility = View.GONE
            else -> {
            }
        }
    }

    private fun startProgress(context: Context) {
        progressBar.visibility = View.VISIBLE
        //        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(context,
//                R.drawable.loading_progressbar));
//        progressBar.setProgressDrawable(ContextCompat.getDrawable(context,
//                R.drawable.loading_progressbar));
    }

    private fun stopProgress(context: Context) {
        progressBar.visibility = View.GONE
        //        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(context,
//                R.mipmap.loading_progress));
//        progressBar.setProgressDrawable(ContextCompat.getDrawable(context,
//                R.mipmap.loading_progress));
    }

    interface OnReloadListener {
        fun reLoadData()
    }

    fun setOnReloadListener(onReloadListener: OnReloadListener) {
        this.onReloadListener = onReloadListener
    }

    interface ILoadingStatus {
        companion object {
            const val NET_ERROR = 1
            const val SERVER_ERROR = 2
            const val EMPTY = 3
            const val LOADING = 4
            const val FINISH = 5
        }
    }

    init {
        initView(mContext)
    }
}