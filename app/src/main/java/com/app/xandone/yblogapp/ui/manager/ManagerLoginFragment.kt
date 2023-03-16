package com.app.xandone.yblogapp.ui.manager

import android.animation.ObjectAnimator
import android.view.View
import android.view.Window
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.xandone.baselib.base.BaseFrament
import com.app.xandone.baselib.base.setClickAction
import com.app.xandone.baselib.cache.SpHelper.save2DefaultSp
import com.app.xandone.baselib.utils.JsonUtils.obj2Json
import com.app.xandone.baselib.utils.MD5Util.MD5
import com.app.xandone.baselib.utils.ToastUtils.showShort
import com.app.xandone.widgetlib.utils.AnimUtils.zoomIn
import com.app.xandone.widgetlib.utils.AnimUtils.zoomOut
import com.app.xandone.widgetlib.utils.KeyboardWatcher
import com.app.xandone.widgetlib.utils.KeyboardWatcher.SoftKeyboardStateListener
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.config.AppConfig
import com.app.xandone.yblogapp.constant.OSpKey
import com.app.xandone.yblogapp.databinding.FragManagerLoginBinding
import com.app.xandone.yblogapp.model.bean.AdminBean
import com.app.xandone.yblogapp.model.event.SwitchEvent
import org.greenrobot.eventbus.EventBus
import kotlinx.android.synthetic.main.frag_manager_login.*
import kotlinx.coroutines.launch

/**
 * author: Admin
 * created on: 2020/9/29 09:52
 * description:
 */
class ManagerLoginFragment : BaseFrament<FragManagerLoginBinding>(), SoftKeyboardStateListener {
    private lateinit var keyboardWatcher: KeyboardWatcher
    override fun getLayout(): Int {
        return R.layout.frag_manager_login
    }

    private val managerModel by lazy {
        ViewModelProvider(this, ManagerModelFactory()).get(
            ManagerModel::class.java
        )
    }

    override fun initView(view: View) {
        keyboardWatcher = KeyboardWatcher(mActivity.findViewById(Window.ID_ANDROID_CONTENT))
        keyboardWatcher.addSoftKeyboardStateListener(this)

        setClickAction(login_btn) {
            when (this) {
                login_btn -> login()
                else -> {

                }
            }
        }

        managerModel.datas.observe(this) {
            EventBus.getDefault().post(SwitchEvent(SwitchEvent.MANAGER_DATA_RAG))
            it.data?.get(0)?.let { it1 -> savaLoginInfo(it1) }
            showShort("登录成功")
        }
    }

    private fun login() {
        val name: String = login_account_et.text.toString()
        var psw: String? = login_psw_et.text.toString()
        if (name.isEmpty()) {
            showShort("请输入账户")
            return
        }
        if (psw == null) {
            showShort("请输入密码")
            return
        }
        if (psw.isEmpty()) {
            showShort("请输入密码")
            return
        }
        psw = MD5(psw)

        lifecycleScope.launch {
            psw?.let { managerModel.login(name, it) }

        }
    }

    /**
     * 缓存登录信息
     *
     * @param adminBean
     */
    private fun savaLoginInfo(adminBean: AdminBean) {
        save2DefaultSp(
            App.sContext!!,
            OSpKey.ADMIN_INFO_KEY,
            obj2Json(adminBean)
        )
    }

    override fun onSoftKeyboardOpened(keyboardSize: Int) {
        val location = IntArray(2)
        //获取body在屏幕中的坐标,控件左上角
        et_body_cl.getLocationOnScreen(location)
        val x = location[0]
        val y = location[1]
        val bottom = AppConfig.SCREEN_HEIGHT - (y + et_body_cl.height)
        if (keyboardSize > bottom) {
            val mAnimatorTranslateY = ObjectAnimator.ofFloat(
                et_body_cl,
                "translationY",
                0.0f,
                -(keyboardSize - bottom).toFloat()
            )
            mAnimatorTranslateY.duration = 300
            mAnimatorTranslateY.interpolator = AccelerateDecelerateInterpolator()
            mAnimatorTranslateY.start()
            zoomIn(lock_iv, keyboardSize - bottom.toFloat(), 0.8f)
        }
    }

    override fun onSoftKeyboardClosed() {
        val mAnimatorTranslateY =
            ObjectAnimator.ofFloat(et_body_cl, "translationY", et_body_cl.translationY, 0f)
        mAnimatorTranslateY.duration = 300
        mAnimatorTranslateY.interpolator = AccelerateDecelerateInterpolator()
        mAnimatorTranslateY.start()
        zoomOut(lock_iv, 0.8f)
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboardWatcher.removeSoftKeyboardStateListener(this)
    }

    override fun initVB(): FragManagerLoginBinding {
        return FragManagerLoginBinding.inflate(layoutInflater)
    }
}