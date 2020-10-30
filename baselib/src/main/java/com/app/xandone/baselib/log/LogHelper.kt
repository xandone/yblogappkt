package com.app.xandone.baselib.log

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 * author: Admin
 * created on: 2020/8/11 11:32
 * description:
 */
class LogHelper private constructor() {
    val instance: LogHelper = Build.logHelper

    private object Build {
        val logHelper = LogHelper()
    }

    companion object {
        const val ENGINE_LOGGER = 1
        private var logEngine = ENGINE_LOGGER
        fun init(engineType: Int, isLoggable: Boolean) {
            logEngine = engineType
            if (logEngine == ENGINE_LOGGER) {
                Logger.addLogAdapter(object : AndroidLogAdapter() {
                    override fun isLoggable(priority: Int, tag: String?): Boolean {
                        return isLoggable
                    }
                })
            }
        }

        fun d(log: Any?) {
            if (logEngine == ENGINE_LOGGER) {
                Logger.d(log)
            }
        }

        fun d(msg: String?, vararg args: Any?) {
            if (logEngine == ENGINE_LOGGER) {
                Logger.d(msg!!, *args)
            }
        }

        fun e(msg: String?, vararg args: Any?) {
            if (logEngine == ENGINE_LOGGER) {
                Logger.e(msg!!, *args)
            }
        }

        fun w(msg: String?) {
            if (logEngine == ENGINE_LOGGER) {
                Logger.w(msg!!)
            }
        }

        fun v(msg: String?) {
            if (logEngine == ENGINE_LOGGER) {
                Logger.v(msg!!)
            }
        }

        fun i(msg: String?) {
            if (logEngine == ENGINE_LOGGER) {
                Logger.i(msg!!)
            }
        }
    }
}