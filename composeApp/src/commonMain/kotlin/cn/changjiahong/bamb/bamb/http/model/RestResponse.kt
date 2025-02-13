package cn.changjiahong.bamb.bamb.http.model

import cn.changjiahong.bamb.bamb.http.status.RestStatusCode
import kotlinx.serialization.Serializable

/**
 *
 * @author ChangJiahong
 * @date 2022/6/29
 */
@Serializable
data class RestResponse<T>(
    val status: RestStatusCode = RestStatusCode.NullStatusError,
    val msg: String = "",
    val data: T? = null
) {

    fun isSuccess() = status == RestStatusCode.OK

    fun checkData() = data != null

    fun successRestData() = isSuccess() && checkData()

    fun requsetData() = data!!
}


@Serializable
object NoData