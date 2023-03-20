package com.app.xandone.yblogapp.ui.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.app.xandone.baselib.base.BaseSimpleActivity
import com.app.xandone.yblogapp.MainActivity
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.config.IMyPermission
import com.app.xandone.yblogapp.databinding.ActSplashBinding
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
import pub.devrel.easypermissions.PermissionRequest

/**
 * author: Admin
 * created on: 2020/9/1 14:05
 * description:
 */
class SplashActivity : BaseSimpleActivity<ActSplashBinding>(), PermissionCallbacks {

    override fun initVB(): ActSplashBinding {
        return ActSplashBinding.inflate(layoutInflater)
    }

    override fun initView() {
        writeAndReadTask()
    }

    private fun go2NextPage() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 1000)
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

}