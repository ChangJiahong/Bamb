package cn.changjiahong.bamb.bamb.http.status

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 *
 * @author ChangJiahong
 * @date 2022/6/29
 */
class RestError(val restStatusCode: RestStatusCode, val msg: String = "", val data: Any? = null) :
    RuntimeException(msg)

fun Throwable.asRestError(): RestError {
    return when (this) {
        is RestError -> this
        else -> RestStatusCode.UnknownError.error(message ?: "")
    }
}
