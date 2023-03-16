package com.app.xandone.yblogapp.ui.manager.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author: xiao
 * created on: 2023/2/8 15:29
 * description:
 */
class ManagerChartModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManagerChartModel() as T
    }
}