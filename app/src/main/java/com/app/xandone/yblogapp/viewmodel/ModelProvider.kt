package com.app.xandone.yblogapp.viewmodel

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory

/**
 * author: Admin
 * created on: 2020/8/12 14:40
 * description:
 */
object ModelProvider {
    fun <T : BaseViewModel> getModel(
        activity: FragmentActivity,
        viewModel: Class<T>,
        application: Application?
    ): T {
        return ViewModelProvider(activity, AndroidViewModelFactory(application!!))[viewModel]
            .attachLifecycleOwner(activity)
    }
}