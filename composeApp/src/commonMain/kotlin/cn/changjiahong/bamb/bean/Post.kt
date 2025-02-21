package cn.changjiahong.bamb.bean

import kotlinx.serialization.Serializable

/**
 *
 * @author ChangJiahong
 * @date 2025/1/26
 */
@Serializable
data class Post(
    val title: String,
    val author: String="",
    val intro: String="",
    val avatar: String="",
    val releaseDate: String="",
    val category: String="",
    val cover: String=""
)
