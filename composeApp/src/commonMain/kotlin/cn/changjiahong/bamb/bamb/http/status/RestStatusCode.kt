package cn.changjiahong.bamb.bamb.http.status

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = RestStatusCodeSerializer::class)
enum class RestStatusCode(val code: Int) {

    //http请求错误
    HttpError(-1),

    // Json 解析错误
    JsonParseError(-2),

    // 反序列化错误
    DeserializationError(-3),
    NULLResponseError(-4),

    // 客户端未知错误
    UnknownError(-500),
    // 小于0 的错误，代表客户端产生的错误 不显示msg消息
    //--------------------------------------------
    // 大于0 的,代表服务端返回的错误 默认显示msg

    // 未登录
    NotLoggedIn(0),

    OK(200),

    NoPermission(300),
    TokenInvalid(301),
    TokenExpires(302),

    NotFoundResource(404),


    // 服务端内部错误
    InternalServerError(500),
    OtherError(501),


    MissingImportantParameters(600),


    UsernameIsEmpty(10001),
    UsernameNotFound(10002),
    PasswordError(10003),
    PasswordEmpty(10004),


    /**
     * file io error
     */

    FileUploadFailed(20001),


    ;

    private var msgId: String

    val id: String
        get() = msgId

    init {
        val tempName = name.replace("[A-Z]".toRegex(), "_$0")
        msgId = tempName.drop(1).lowercase()
    }

    companion object {

        fun valueOfId(statusId: String): RestStatusCode {
            return entries.find { it.id == statusId } ?: InternalServerError
        }

        fun valueOfCode(code: Int): RestStatusCode {
            return entries.find { it.code == code } ?: OtherError
        }
    }

}


fun RestStatusCode.error(msg: String = "", data: Any? = null): RestError{
    return RestError(this,msg,data)
}

class RestStatusCodeSerializer : KSerializer<RestStatusCode> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("status", PrimitiveKind.INT)

    override fun serialize(
        encoder: Encoder,
        value: RestStatusCode
    ) {
        encoder.encodeInt(value.code)
    }

    override fun deserialize(decoder: Decoder): RestStatusCode {
        return RestStatusCode.valueOfCode(decoder.decodeInt())
    }
}