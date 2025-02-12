package cn.changjiahong.bamb.bamb.http

import cn.changjiahong.bamb.bamb.http.model.RestResponse
import de.jensklingenberg.ktorfit.Response
import kotlinx.coroutines.flow.Flow


typealias FlowResponse<T> = Flow<Response<T>>
typealias FlowRestResponse<T> = Flow<RestResponse<T>>

//
//inline fun <reified T> Flow<RestResponse<T>>.data(): Flow<T> =
//    this.transform { restResponse ->
//        if (restResponse.status == StatusMsg.OK.code) {
//            if (restResponse.data != null) {
//                emit(restResponse.data)
//            } else {
//                when (T::class) {
//                    NoData::class -> emit(NoData as T)
//                    else -> {}
//                }
//            }
//        } else {
//            throw ApiExp(
//                StatusMsg.valueOfCode(restResponse.status),
//                restResponse.msg,
//                restResponse.data
//            )
//        }
//    }
