package com.app.xandone.yblogapp.model.repository

import android.net.ParseException
import com.app.xandone.baselib.utils.SimpleUtils
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
    suspend fun <T> excuteHttp(
        isShowError: Boolean = true,
        block: suspend () -> ApiResponse<T>
    ): ApiResponse<T> {
        kotlin.runCatching {
            block.invoke()
        }.onSuccess { resp: ApiResponse<T> ->
            return handleApiSuccess(isShowError, resp)
        }.onFailure { t: Throwable ->
            return handleException(isShowError, t)
        }
        return ApiEmptyResponse()
    }

    private fun <T> handleApiSuccess(isShowError: Boolean, resp: ApiResponse<T>): ApiResponse<T> {
        return if (resp.code == 200) {
            resp.result = HttpResult.SUCCESS
            resp
        } else {
            handleApiError(isShowError, resp)
        }
    }

    private fun <T> handleApiError(
        isShowError: Boolean,
        resp: ApiResponse<T>
    ): ApiErrorResponse<T> {
        when (resp.code) {
            201 -> {
            }
        }
        if (isShowError && !SimpleUtils.isEmpty(resp.msg)) {
            ToastUtils.showShort(resp.msg!!)
        }
        return ApiErrorResponse(resp)
    }

    private fun <T> handleException(
        isShowError: Boolean,
        t: Throwable
    ): ExceptionResponse<T> {
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
        if (isShowError) {
            ToastUtils.showShort(message)
        }
        return ExceptionResponse(t, code, message)
    }


}