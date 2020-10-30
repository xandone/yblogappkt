package com.app.xandone.widgetlib.dialog.bottom

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.app.xandone.widgetlib.R

abstract class BaseBottomDialog : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, styleRes)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(cancelOutside)
        val v = inflater.inflate(layoutRes, container, false)
        bindView(v)
        return v
    }

    @get:LayoutRes
    abstract val layoutRes: Int
    abstract fun bindView(v: View?)
    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        val params = window!!.attributes
        params.dimAmount = dimAmount
        params.width = width
        if (height > 0) {
            params.height = height
        } else {
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        params.gravity = gravity
        window.attributes = params
    }

    open val height = -1

    open val cancelOutside = true

    open val fragmentTag = TAG

    /**
     * 获取主题
     *
     * @return
     */
    @get:StyleRes
    val styleRes = R.style.bottomDialog_style

    /**
     * 获取对齐方式
     *
     * @return
     */
    val gravity = Gravity.BOTTOM

    /**
     * 对话框宽度
     *
     * @return
     */
    val width = WindowManager.LayoutParams.MATCH_PARENT

    fun show(fragmentManager: FragmentManager?) {
        show(fragmentManager!!, fragmentTag)
    }

    companion object {
        private const val TAG = "base_bottom_dialog"
        const val dimAmount = 0.2f
    }
}