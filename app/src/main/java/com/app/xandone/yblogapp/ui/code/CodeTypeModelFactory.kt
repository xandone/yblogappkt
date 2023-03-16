package com.app.xandone.yblogapp.ui.code

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author: xiao
 * created on: 2023/2/8 15:29
 * description:
 */
class CodeTypeModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CodeTypeModel() as T
    }
}