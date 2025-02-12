package cn.changjiahong.bamb.bamb.http.status

enum class StatusMsg(val code: Int) {

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

//    fun exception(): CustomException {
//        return CustomException(this)
//    }
//
//    fun rest():RestResponse = RestResponse.value(this)

    companion object{

        fun valueOfId(statusId:String): StatusMsg {
            return values().find { it.id == statusId }?: InternalServerError
        }

        fun valueOfCode(code: Int):StatusMsg{
            return values().find { it.code==code }?:OtherError
        }
    }

}