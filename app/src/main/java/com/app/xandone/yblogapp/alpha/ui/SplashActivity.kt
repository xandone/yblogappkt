package com.xandone.manager2.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.xandone.manager2.MainActivity
import com.xandone.manager2.databinding.ActSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author: xiao
 * created on: 2023/2/7 11:02
 * description:
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var mBinding: ActSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActSplashBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        mBinding.tv.text = "121321321"


        lifecycleScope.launch {
            delay(2000)

            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
    }
}