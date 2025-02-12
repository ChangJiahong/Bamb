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
    val status: RestStatusCode,
    val msg: String = "",
    val data: T?
) {

    constructor(restResponse: RestResponse<T>) : this(restResponse.status,restResponse.msg,restResponse.data)


    fun isSuccess() = status == RestStatusCode.OK

    fun checkData() = data != null

    fun successRestData() = isSuccess() && checkData()

    fun requsetData() = data!!
}




@Serializable
object NoData