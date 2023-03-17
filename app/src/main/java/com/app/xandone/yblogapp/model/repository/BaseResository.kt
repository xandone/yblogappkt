package com.app.xandone.yblogapp.model.repository

import android.net.ParseException
import android.util.Log
import com.app.xandone.baselib.utils.JsonUtils
import com.app.xandone.baselib.utils.ToastUtils
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.io.NotSerializableException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * @author: xiao
 * created on: 2023/2/20 10:14
 * description:
 */
open class BaseResository {
    suspend fun <T> excuteHttp(block: suspend () -> ApiResponse<T>): ApiResponse<T> {
        kotlin.runCatching {
            block.invoke()
        }.onSuccess { resp: ApiResponse<T> ->
            return handleApiSuccess(resp)
        }.onFailure { t: Throwable ->
            return handleApiOtherError(t)
        }
        return ApiEmptyResponse()
    }

    private fun <T> handleApiSuccess(resp: ApiResponse<T>): ApiResponse<T> {
        if (resp.code == 200) {
            resp.result = HttpResult.SUCCESS
            return resp
        } else {
            return handleApiError(resp)
        }
    }

    private fun <T> handleApiError(resp: ApiResponse<T>): ApiErrorResponse<T> {
        when (resp.code) {
            201 -> {
            }
        }
        return ApiErrorResponse(resp)
    }

    private fun <T> handleApiOtherError(t: Throwable): ApiOtherErrorResponse<T> {
        var code = -1000
        var message = ""
        when (t) {
            is HttpException -> {
                message = "网络异常"
                code = 1001
            }
            is ConnectException -> {
                message = "服务器连接失败"
                code = 1002
            }
            is SocketTimeoutException -> {
                message = "网络连接超时"
                code = 1003
            }
            is JsonParseException,
            is JSONException,
            is NotSerializableException,
            is ParseException -> {
                message = "数据解析出错"
                code = 1004
            }
            is SSLHandshakeException -> {
                message = "证书验证失败"
                code = 1005
            }
            is UnknownHostException -> {
                message = "无法解析该域名"
                code = 1007
            }
            is NullPointerException -> {
                message = "NullPointerException"
                code = 1008
            }
            else -> {
                message = "未知错误"
            }
        }
        ToastUtils.showShort(message)
        return ApiOtherErrorResponse(t, code, message)
    }


}