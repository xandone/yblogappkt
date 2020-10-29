package com.app.xandone.baselib.utils

import com.google.gson.Gson
import java.lang.reflect.Type
import java.util.*

/**
 * author: Admin
 * created on: 2020/8/11 16:06
 * description:
 */
object JsonUtils {
    private val GSON = Gson()
    fun <T> json2Obj(json: String?, clazz: Class<T>?): T {
        return GSON.fromJson(json, clazz)
    }

    fun obj2Json(o: Any?): String {
        return GSON.toJson(o)
    }

    fun <E> json2List(
        json: String?,
        type: Type?
    ): MutableList<E>? {
        return try {
            GSON.fromJson(json, type)
        } catch (e: Exception) {
            null
        }
    }

    fun <E> json2List(json: String?, clazz: Class<Array<E>>): List<E>? {
        try {
            val arr: Array<E> = GSON.fromJson(json, clazz)
            return listOf(*arr)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}