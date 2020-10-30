package com.app.xandone.yblogapp.ui.splash

import android.content.Intent
import com.app.xandone.baselib.base.BaseSimpleActivity
import com.app.xandone.yblogapp.MainActivity
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.config.IMyPermission
import com.app.xandone.yblogapp.rx.RxHelper
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
import pub.devrel.easypermissions.PermissionRequest
import java.util.concurrent.TimeUnit

/**
 * author: Admin
 * created on: 2020/9/1 14:05
 * description:
 */
class SplashActivity : BaseSimpleActivity(), PermissionCallbacks {
    private var disposable: Disposable? = null
    override fun getLayout(): Int {
        return R.layout.act_splash
    }

    override fun init() {
        writeAndReadTask()
    }

    private fun go2NextPage() {
        disposable = Flowable.timer(2000, TimeUnit.MILLISECONDS)
            .compose(RxHelper.handleIO())
            .subscribe {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
    }

    private fun hasWriteAndReadPermissions(): Boolean {
        return EasyPermissions.hasPermissions(this, *IMyPermission.WRITE_AND_READ_PERMS)
    }

    @AfterPermissionGranted(IMyPermission.RC_WRITE_AND_READ_PERM_CODE)
    fun writeAndReadTask() {
        if (hasWriteAndReadPermissions()) {
            // Have permissions, do the thing!
            go2NextPage()
        } else {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(
                    this,
                    IMyPermission.RC_WRITE_AND_READ_PERM_CODE,
                    *IMyPermission.WRITE_AND_READ_PERMS)
                    .setRationale("需要以下权限")
                    .build()
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        go2NextPage()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
        //        AppManager.newInstance().finishAllActivity();
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}