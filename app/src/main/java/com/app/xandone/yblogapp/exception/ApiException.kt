package com.app.xandone.yblogapp.exception

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializer
import org.json.JSONException
import retrofit2.HttpException
import java.io.NotSerializableException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * author: Admin
 * created on: 2020/8/13 16:17
 * description:
 */
class ApiException : Exception {
    var code = 0
    override var message: String? = null

    constructor(msg: String?) : super(msg) {}
    constructor(msg: String?, code: Int) : super(msg) {
        this.code = code
        this.message = msg
    }

    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
        message = throwable.message
    }

    constructor(throwable: Throwable) : super(throwable) {
        code = Error.UNKNOWN
        message = throwable.message
    }

    object Error {
        /**
         * 未知错误
         */
        const val UNKNOWN = 2500

        /**
         * 解析错误
         */
        const val PARSE_ERROR =
            UNKNOWN + 1

        /**
         * 网络错误
         */
        const val NETWORD_ERROR =
            PARSE_ERROR + 1

        /**
         * 协议出错
         */
        private const val HTTP_ERROR =
            NETWORD_ERROR + 1

        /**
         * 证书出错
         */
        const val SSL_ERROR =
            HTTP_ERROR + 1

        /**
         * 连接超时
         */
        const val TIMEOUT_ERROR =
            SSL_ERROR + 1

        /**
         * 调用错误
         */
        private const val INVOKE_ERROR =
            TIMEOUT_ERROR + 1

        /**
         * 类转换错误
         */
        const val CAST_ERROR =
            INVOKE_ERROR + 1

        /**
         * 请求取消
         */
        private const val REQUEST_CANCEL =
            CAST_ERROR + 1

        /**
         * 未知主机
         */
        const val UNKNOWNHOST_ERROR =
            REQUEST_CANCEL + 1

        /**
         * 空指针
         */
        const val NULLPOINTER_EXCEPTION =
            UNKNOWNHOST_ERROR + 1
    }

    companion object {
        fun handleException(e: Throwable): ApiException {
            val ex: ApiException
            return if (e is HttpException) {
                val httpException = e
                ex = ApiException(httpException, httpException.code())
                ex.message = "数据加载失败"
                ex
            } else if (e is JsonParseException
                || e is JSONException
                || e is JsonSerializer<*>
                || e is NotSerializableException
                || e is ParseException
            ) {
                ex = ApiException(
                    e,
                    Error.PARSE_ERROR
                )
                ex.message = "数据解析出错"
                ex
            } else if (e is ClassCastException) {
                ex = ApiException(
                    e,
                    Error.CAST_ERROR
                )
                ex.message = "类型转换错误"
                ex
            } else if (e is ConnectException) {
                ex = ApiException(
                    e,
                    Error.NETWORD_ERROR
                )
                ex.message = "服务器连接失败"
                ex
            } else if (e is SSLHandshakeException) {
                ex =
                    ApiException(e, Error.SSL_ERROR)
                ex.message = "证书验证失败"
                ex
            } else if (e is SocketTimeoutException) {
                ex = ApiException(
                    e,
                    Error.TIMEOUT_ERROR
                )
                ex.message = "网络连接超时"
                ex
            } else if (e is UnknownHostException) {
                ex = ApiException(
                    e,
                    Error.UNKNOWNHOST_ERROR
                )
                ex.message = "无法解析该域名"
                ex
            } else if (e is NullPointerException) {
                ex = ApiException(
                    e,
                    Error.NULLPOINTER_EXCEPTION
                )
                ex.message = "NullPointerException"
                ex
            } else {
                ex = ApiException(e, Error.UNKNOWN)
                ex.message = "未知错误：" + e.message
                ex
            }
        }
    }
}