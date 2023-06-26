package com.app.xandone.yblogapp.ui.articledetails

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.xandone.baselib.cache.ImageCache.getImageCache
import com.app.xandone.baselib.log.LogHelper
import com.app.xandone.baselib.utils.ImageUtils.saveFile2SdCard
import com.app.xandone.widgetlib.dialog.bottom.BottomDialog
import com.app.xandone.widgetlib.utils.SizeUtils.dp2px
import com.app.xandone.widgetlib.utils.SizeUtils.px2dp
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.base.BaseWallActivity
import com.app.xandone.yblogapp.config.AppConfig
import com.app.xandone.yblogapp.constant.OConstantKey
import com.app.xandone.yblogapp.databinding.ActArticleDetailsBinding
import com.hitomi.tilibrary.transfer.Transferee
import com.liulishuo.okdownload.DownloadListener
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import java.io.File
import java.util.*
import kotlinx.coroutines.launch

/**
 * author: Admin
 * created on: 2020/9/4 10:20
 * description:
 */
class ArticleDetailsActivity :
    BaseWallActivity<ActArticleDetailsBinding>(ActArticleDetailsBinding::inflate) {

    private var mId: String? = null
    private var mType = 0
    private var mTitle: String? = null
    private var transfer: Transferee? = null
    private var urls: MutableList<String>? = null
    private var task: DownloadTask? = null
    private lateinit var mDownloadListener: DownloadListener
    private lateinit var downloadDialog: BottomDialog

    private val detailsModel by lazy {
        ViewModelProvider(this, CodeDetailsModelFactory()).get(
            CodeDetailsModel::class.java
        )
    }

    override fun initView() {
        super.initView()
        mId = intent.getStringExtra(OConstantKey.ID)
        mType = intent.getIntExtra(
            OConstantKey.TYPE,
            TYPE_CODE
        )
        mTitle = intent.getStringExtra(OConstantKey.TITLE)
        setToolBar(mTitle)
        transfer = Transferee.getDefault(this)
        urls = ArrayList()
        initWebView()
        initDownloadListener()

        detailsModel.datas.observe(this) {
            val html = it.data?.get(0)?.contentHtml!!.replace(
                "<pre",
                "<pre style=\"overflow: auto;background-color: #F3F5F8;padding:10px;\""
            )
            mBinding.webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
            onLoadFinish()
        }
        detailsModel.datas2.observe(this) {
            val html = it.data?.get(0)?.contentHtml!!.replace(
                "<pre",
                "<pre style=\"overflow: auto;background-color: #F3F5F8;padding:10px;\""
            )
            mBinding.webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
            onLoadFinish()
        }


        requestData()
    }

    override fun requestData() {
        lifecycleScope.launch {
            if (mType == TYPE_CODE) {
                detailsModel.getCodeDetails(mId)
            } else {
                detailsModel.getEssayDetails(mId)
            }
        }

    }

    override fun reload(tag: Any?) {
        requestData()
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun initWebView() {
        val ws = mBinding.webView.settings
        //        // 网页内容的宽度是否可大于WebView控件的宽度
//        ws.setLoadWithOverviewMode(false);
//        // 是否应该支持使用其屏幕缩放控件和手势缩放
//        ws.setSupportZoom(true);
//        ws.setBuiltInZoomControls(true);
//        ws.setDisplayZoomControls(false);
//        // 设置此属性，可任意比例缩放。
//        ws.setUseWideViewPort(false);
//        //  页面加载好以后，再放开图片
//        ws.setBlockNetworkImage(false);
//        // 排版适应屏幕
//        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        ws.javaScriptEnabled = true
        mBinding.webView.addJavascriptInterface(this, "imgClick")

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        //修改图片大小
        val screenWidth = AppConfig.SCREEN_WIDTH
        val width =
            (px2dp(App.sContext, screenWidth.toFloat()) - 20).toString()
        //        int fonsSize = SizeUtils.sp2px(App.sContext, 14);
        mBinding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                val javascript = "javascript:function ResizeImages() {" +
                        "var myimg,oldwidth;" +
                        "var maxwidth = document.body.clientWidth;" +
                        "for(i=0;i <document.images.length;i++){" +
                        "myimg = document.images[i];" +
                        "if(myimg.width > " + width + "){" +
                        "oldwidth = myimg.width;" +
                        "myimg.width =" + width + ";" +
                        "}" +
                        "}" +
                        "}"
                view.loadUrl(javascript)
                view.loadUrl("javascript:ResizeImages();")
                view.loadUrl(
                    "javascript:function modifyTextColor(){" +
                            "document.getElementsByTagName('body')[0].style.webkitTextFillColor='#333';" +
                            "document.getElementsByTagName('body')[0].style.background='white';" +
                            "};modifyTextColor();"
                )
                view.loadUrl(
                    "javascript:function addImgClickEvent() {" +
                            "        var objs = document.getElementsByTagName(\"img\");" +
                            "        for (var i = 0; i < objs.length; i++) {" +
                            "            objs[i].index = i;" +
                            "            objs[i].onclick = function() {" +
                            "              imgClick.showImg(this.src,this.index);" +
                            "            }" +
                            "        }" +
                            "    }" +
                            "    addImgClickEvent();"
                )
            }
        }
    }

    @SuppressLint("CheckResult")
    @JavascriptInterface
    fun showImg(url: String, position: Int) {
//        Observable.just(url)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe { s ->
//                urls!!.clear()
//                urls!!.add(s)
//                transfer!!.apply(
//                    TransferConfig.build()
//                        .setImageLoader(UniversalImageLoader.with(applicationContext))
//                        .setSourceUrlList(urls)
//                        .setOnLongClickListener { imageView, imageUri, pos ->
//                            showDownloadDialog(
//                                imageUri
//                            )
//                        }
//                        .create()
//                ).show()
//            }
    }

    private fun showDownloadDialog(imageUri: String) {
        downloadDialog = BottomDialog.create(supportFragmentManager)
            .setViewListener(object :
                BottomDialog.ViewListener {
                override fun bindView(v: View?) {
                    val listener =
                        View.OnClickListener { v ->
                            when (v.id) {
                                R.id.save_img_tv -> downloadImg(imageUri)
                                else -> {
                                }
                            }
                            downloadDialog.dismiss()
                        }
                    v!!.findViewById<View>(R.id.save_img_tv).setOnClickListener(listener)
                    v.findViewById<View>(R.id.cache_img_tv)
                        .setOnClickListener(listener)
                }
            })
            .setLayoutRes(R.layout.dialog_download_img)
            .setDimAmount(0.6f)
            .setHeight(dp2px(App.sContext, 200f))
            .setTag("BottomDialog")
        downloadDialog.show()
    }

    private fun downloadImg(url: String) {
        task = DownloadTask.Builder(
            url,
            File(getImageCache(App.sContext))
        )
            .setFilename(
                System.currentTimeMillis().toString() + ".jpg"
            ) // the minimal interval millisecond for callback progress
            .setMinIntervalMillisCallbackProcess(30) // do re-download even if the task has already been completed in the past.
            .setPassIfAlreadyCompleted(false)
            .build()
        task?.enqueue(mDownloadListener)
    }

    private fun initDownloadListener() {
        mDownloadListener = object : DownloadListener {
            override fun taskStart(task: DownloadTask) {}
            override fun connectTrialStart(
                task: DownloadTask,
                requestHeaderFields: Map<String, List<String>>
            ) {
            }

            override fun connectTrialEnd(
                task: DownloadTask,
                responseCode: Int,
                responseHeaderFields: Map<String, List<String>>
            ) {
            }

            override fun downloadFromBeginning(
                task: DownloadTask,
                info: BreakpointInfo,
                cause: ResumeFailedCause
            ) {
            }

            override fun downloadFromBreakpoint(
                task: DownloadTask,
                info: BreakpointInfo
            ) {
            }

            override fun connectStart(
                task: DownloadTask,
                blockIndex: Int,
                requestHeaderFields: Map<String, List<String>>
            ) {
            }

            override fun connectEnd(
                task: DownloadTask,
                blockIndex: Int,
                responseCode: Int,
                responseHeaderFields: Map<String, List<String>>
            ) {
            }

            override fun fetchStart(
                task: DownloadTask,
                blockIndex: Int,
                contentLength: Long
            ) {
            }

            override fun fetchProgress(
                task: DownloadTask,
                blockIndex: Int,
                increaseBytes: Long
            ) {
            }

            override fun fetchEnd(
                task: DownloadTask,
                blockIndex: Int,
                contentLength: Long
            ) {
            }

            override fun taskEnd(
                task: DownloadTask,
                cause: EndCause,
                realCause: Exception?
            ) {
                LogHelper.d("image download taskEnd fileName=" + task.filename)
                saveFile2SdCard(
                    App.sContext,
                    task.file!!,
                    "yblog"
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (transfer != null) {
            transfer!!.destroy()
        }
        if (task != null) {
            task!!.cancel()
        }
    }

    companion object {
        const val TYPE_CODE = 1
        const val TYPE_ESSAY = 2
    }
}