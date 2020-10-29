package com.app.xandone.baselib.cache

import android.content.Context
import android.content.SharedPreferences
import com.app.xandone.baselib.config.BaseConfig

/**
 * author: Admin
 * created on: 2020/8/11 15:40
 * description:
 */
object SpHelper {
    fun getDefaultSp(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            BaseConfig.appName,
            Context.MODE_PRIVATE
        )
    }

    fun getSpByName(context: Context, spName: String?): SharedPreferences {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE)
    }

    /**
     * 保存到默认的sp
     *
     * @param context
     * @param key
     * @param msg
     */
    fun save2DefaultSp(
        context: Context,
        key: String,
        msg: String?
    ) {
        getDefaultSp(context).edit().putString(key, msg).apply()
    }

    /**
     * 保存到指定sp
     *
     * @param context
     * @param spName
     * @param key
     * @param msg
     */
    fun save2Sp(
        context: Context,
        spName: String,
        key: String,
        msg: String?
    ) {
        getSpByName(context, spName).edit().putString(key, msg).apply()
    }

    /**
     * 获取默认的sp下的String
     *
     * @param context
     * @param key
     * @return
     */
    fun getDefaultString(context: Context, key: String): String? {
        return getDefaultString(context, key, "")
    }

    fun getDefaultString(
        context: Context,
        key: String,
        defaultValue: String
    ): String? {
        return getDefaultSp(context).getString(key, defaultValue)
    }

    /**
     * 获取指定的sp下的String
     *
     * @param context
     * @param spName
     * @param key
     * @return
     */
    fun getStringByName(
        context: Context,
        spName: String,
        key: String
    ): String? {
        return getStringByName(context, spName, key, "")
    }

    fun getStringByName(
        context: Context,
        spName: String,
        key: String,
        defaultValue: String
    ): String? {
        return getSpByName(context, spName).getString(key, defaultValue)
    }

    /**
     * 清除默认sp下的某个key值
     *
     * @param context
     * @param keys
     */
    fun clearDefaultSp(context: Context, vararg keys: String?) {
        for (key in keys) {
            getDefaultSp(context).edit().remove(key).apply()
        }
    }

    /**
     * 清除默认的sp下的所有值
     *
     * @param context
     */
    fun clearDefaultSp(context: Context) {
        getDefaultSp(context).edit().clear().apply()
    }

    /**
     * 清除默认的sp下的某个key值
     *
     * @param context
     */
    fun clearDefaultSp(context: Context, key: String?) {
        getDefaultSp(context).edit().remove(key).apply()
    }

    /**
     * 清除指定的sp下的所有值
     *
     * @param context
     * @param names
     */
    fun clearSp(context: Context, vararg names: String?) {
        for (name in names) {
            getSpByName(context, name).edit().clear().apply()
        }
    }

    /**
     * 清除全部sp
     *
     * @param context
     * @param names
     */
    fun clearAllSp(context: Context, vararg names: String?) {
        clearDefaultSp(context)
        if (names.isNotEmpty()) {
            clearSp(context, *names)
        }
    }
}