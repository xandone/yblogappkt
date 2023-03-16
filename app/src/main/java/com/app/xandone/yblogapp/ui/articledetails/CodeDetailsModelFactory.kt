package com.app.xandone.yblogapp.ui.articledetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author: xiao
 * created on: 2023/2/8 15:29
 * description:
 */
class CodeDetailsModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CodeDetailsModel() as T
    }
}