package com.app.xandone.yblogapp.ui.manager

import android.animation.ObjectAnimator
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import butterknife.BindView
import butterknife.OnClick
import com.app.xandone.baselib.base.BaseFrament
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
import com.app.xandone.yblogapp.model.ManagerModel
import com.app.xandone.yblogapp.model.bean.AdminBean
import com.app.xandone.yblogapp.model.event.SwitchEvent
import com.app.xandone.yblogapp.rx.IRequestCallback
import com.app.xandone.yblogapp.viewmodel.ModelProvider
import org.greenrobot.eventbus.EventBus
import kotlinx.android.synthetic.main.frag_manager_login.*

/**
 * author: Admin
 * created on: 2020/9/29 09:52
 * description:
 */
class ManagerLoginFragment : BaseFrament(), SoftKeyboardStateListener {
    private var keyboardWatcher: KeyboardWatcher? = null
    private var managerModel: ManagerModel? = null
    override fun getLayout(): Int {
        return R.layout.frag_manager_login
    }

    override fun init(view: View?) {
        keyboardWatcher =
            KeyboardWatcher(mActivity!!.findViewById(Window.ID_ANDROID_CONTENT))
        keyboardWatcher!!.addSoftKeyboardStateListener(this)
    }

    override fun initDataObserver() {
        managerModel = ModelProvider.getModel(
            mActivity,
            ManagerModel::class.java,
            App.Companion.sContext
        )
    }

    @OnClick(R.id.login_btn)
    fun click(view: View) {
        when (view.id) {
            R.id.login_btn -> login()
            else -> {
            }
        }
    }

    private fun login() {
        val name = login_account_et.text.toString()
        var psw: String? = login_psw_et.text.toString()
        if (TextUtils.isEmpty(name)) {
            showShort("请输入账户")
            return
        }
        if (TextUtils.isEmpty(psw)) {
            showShort("请输入密码")
            return
        }
        psw = MD5(psw!!)
        managerModel!!.login(
            name,
            psw,
            object : IRequestCallback<AdminBean> {
                override fun success(adminBean: AdminBean) {
                    EventBus.getDefault().post(SwitchEvent(SwitchEvent.MANAGER_DATA_RAG))
                    savaLoginInfo(adminBean)
                    showShort("登录成功")
                }

                override fun error(message: String?, statusCode: Int) {
                    showShort("登录异常$message")
                }
            })
    }

    /**
     * 缓存登录信息
     *
     * @param adminBean
     */
    private fun savaLoginInfo(adminBean: AdminBean) {
        save2DefaultSp(
            App.Companion.sContext!!,
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
        keyboardWatcher!!.removeSoftKeyboardStateListener(this)
    }
}