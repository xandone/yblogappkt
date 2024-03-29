package com.app.xandone.yblogapp.model.repository

/**
 * @author: xiao
 * created on: 2023/2/20 10:17
 * description:
 */
open class ApiResponse<T>(
    val code: Int = 0,
    val msg: String? = null,
    val data: T? = null,
    val total: Int = 0,
    var result: HttpResult = HttpResult.FAIL
)

data class ListBean<T>(
    val items: MutableList<T>,
    val total_page: Int,
    val total_record: Int
)

class ApiSuccessResponse<T> : ApiResponse<T>()

/**
 * Api空值
 */
class ApiEmptyResponse<T>(var empty: String = "暂无数据") : ApiResponse<T>()

/**
 * Api非200情况
 */
class ApiErrorResponse<T> constructor(var origin: ApiResponse<T>) : ApiResponse<T>()

/**
 * data有默认值null。这里构造函数可以不用传参
 */
class ExceptionResponse<T>(
    val throwable: Throwable? = null,
    val errorCode: Int = -1000,
    val errorMessage: String = "加载失败"
) : ApiResponse<T>()

enum class HttpResult {
    SUCCESS,
    FAIL
}