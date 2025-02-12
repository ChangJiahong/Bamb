package cn.changjiahong.bamb.bamb.http.model

import kotlinx.serialization.Serializable

/**
 *
 * @author ChangJiahong
 * @date 2022/6/29
 */
@Serializable
data class RestResponse<T>(
    val status: Int,
    val msg: String = "",
    val data: T?
) {
    fun isSuccess() = status == 200

    fun checkData() = data != null

    fun successRestData() = isSuccess() && checkData()

    fun requsetData() = data!!
}

object NoData