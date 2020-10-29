package com.app.xandone.yblogapp.utils

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter

/**
 * author: Admin
 * created on: 2020/10/22 10:37
 * description:
 */
class LineValueFormatter(
    private val mLabels: List<Int>,
    private val xAxisValues: List<Int>?
) : ValueFormatter() {
    override fun getPointLabel(entry: Entry): String {
        return mLabels[getIndex(entry.x)].toString() + ""
    }

    private fun getIndex(x: Float): Int {
        if (xAxisValues == null) {
            return -1
        }
        for (i in xAxisValues.indices) {
            if (x.toInt() == xAxisValues[i]) {
                return i
            }
        }
        return 0
    }

}