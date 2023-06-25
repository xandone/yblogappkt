package com.app.xandone.baselib.utils

import android.os.Build
import android.util.*
import androidx.collection.SimpleArrayMap
import java.lang.reflect.Array

/**
 * author: Admin
 * created on: 2020/10/22 10:06
 * description:
 */
object SimpleUtils {
    fun isEmpty(obj: Any?): Boolean {
        return if (obj == null) {
            true
        } else if (obj.javaClass.isArray && Array.getLength(obj) == 0) {
            true
        } else if (obj is CharSequence && obj.toString().isEmpty()) {
            true
        } else if (obj is Collection<*> && obj.isEmpty()) {
            true
        } else if (obj is Map<*, *> && obj.isEmpty()) {
            true
        } else if (obj is SimpleArrayMap<*, *> && obj.isEmpty) {
            true
        } else if (obj is SparseArray<*> && obj.size() == 0) {
            true
        } else if (obj is SparseBooleanArray && obj.size() == 0) {
            true
        } else if (obj is SparseIntArray && obj.size() == 0) {
            true
        } else if (obj is SparseLongArray && obj.size() == 0) {
            true
        } else if (obj is LongSparseArray<*> && obj.size() == 0) {
            true
        } else {
            obj is LongSparseArray<*> && obj.size() == 0
        }
    }
}