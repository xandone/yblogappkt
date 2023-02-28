package com.xandone.manager2.ui.untreat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author: xiao
 * created on: 2023/2/8 15:29
 * description:
 */
class UntreatViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UntreatViewModel() as T
    }
}