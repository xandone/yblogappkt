package com.app.xandone.yblogapp.ui.code.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author: xiao
 * created on: 2023/2/8 15:29
 * description:
 */
class CodeViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CodeModel() as T
    }
}