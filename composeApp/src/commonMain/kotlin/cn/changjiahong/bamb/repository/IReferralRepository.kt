package cn.changjiahong.bamb.repository

import cn.changjiahong.bamb.bamb.compose.PagingDataFlow
import cn.changjiahong.bamb.bean.Post

/**
 *
 * @author ChangJiahong
 * @date 2025/1/26
 */
interface IReferralRepository {

    fun createPostsPager(): PagingDataFlow<Post>
}