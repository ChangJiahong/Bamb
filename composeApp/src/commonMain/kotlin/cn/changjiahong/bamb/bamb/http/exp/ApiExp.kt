package cn.changjiahong.bamb.bamb.http.exp

/**
 *
 * @author ChangJiahong
 * @date 2022/6/29
 */
class ApiExp(val code: StatusMsg, val msg: String = "", val data: Any? = null) : RuntimeException(msg) {


}