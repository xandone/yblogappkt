package com.app.xandone.yblogapp.model.repository

/**
 * @author: xiao
 * created on: 2023/2/20 10:17
 * description:
 */
open class ApiResponse<T>(
    val code: Int = 0,
    val message: String? = null,
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

class ApiErrorResponse<T> : ApiResponse<T>()

/**
 * data有默认值null。这里构造函数可以不用传参
 */
data class ApiOtherErrorResponse<T>(
    val throwable: Throwable,
    val errorCode: Int = -1,
    val errorMessage: String = "加载失败"
) : ApiResponse<T>()

class ApiEmptyResponse<T> : ApiResponse<T>()

enum class HttpResult {
    SUCCESS,
    FAIL
}