package cn.changjiahong.bamb.bamb.http

import cn.changjiahong.bamb.bamb.http.model.NoData
import cn.changjiahong.bamb.bamb.http.model.RestResponse
import cn.changjiahong.bamb.bamb.http.status.RestError
import cn.changjiahong.bamb.bamb.http.status.RestStatusCode
import cn.changjiahong.bamb.bamb.http.status.error
import de.jensklingenberg.ktorfit.Response
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch


typealias FlowResponse<T> = Flow<Response<T>>
typealias FlowRResponse<T> = FlowResponse<RestResponse<T>>
typealias FlowRestResponse<T> = Flow<RestResponse<T>>

inline fun <reified T> FlowResponse<T>.onHeaders(crossinline callFun: (headers: Headers) -> Unit): FlowResponse<T> =
    this.map { response ->
        val headers = response.headers
        callFun(headers)
        return@map response
    }

inline fun <reified T> FlowResponse<T>.body(): Flow<T> = this.map { response ->
    try {
        if (response.status == HttpStatusCode.OK) {
            val body = response.body()
            if (body == null) {
                throw RestStatusCode.NULLResponseError.error()
            } else {
                return@map body
            }
        } else {
            throw RestStatusCode.HttpError.error(response.status.toString(), response)
        }
    } catch (e: Exception) {
        throw RestStatusCode.UnknownError.error(e.message ?: "")
    }
}

inline fun <reified T> FlowRResponse<T>.asData(): Flow<T> = body().data()

inline fun <reified T> FlowRestResponse<T>.data(): Flow<T> =
    this.transform { restResponse ->
        if (restResponse.status == RestStatusCode.OK) {
            if (restResponse.data != null) {
                emit(restResponse.data)
            } else {
                when (T::class) {
                    NoData::class -> emit(NoData as T)
                    else -> {}
                }
            }
        } else {
            throw restResponse.status.error(restResponse.msg, restResponse.data)
        }
    }


public fun <T> Flow<T>.collectIn(
    scope: CoroutineScope, collector: FlowCollector<T>
) = scope.launch {
    collect(collector)
}

/**
 * 一定有数据，传入默认保底数据
 */
public fun <T> Flow<T>.mustCollectIn(
    scope: CoroutineScope, default: T, collector: FlowCollector<T>
) = scope.launch {
    onEmpty {
        emit(default)
    }.collect(collector)
}