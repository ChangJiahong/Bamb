package cn.changjiahong.bamb.bean

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val email: String,
    val username: String,
    val avatar: String,
    val nimAccount: String,
    val nimToken: String,
    val accessToken: String = "",
    val refreshToken: String = "",
    val nona: String
)
