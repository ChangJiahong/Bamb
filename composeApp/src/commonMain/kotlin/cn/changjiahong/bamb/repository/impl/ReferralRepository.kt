package cn.changjiahong.bamb.repository.impl

import cn.changjiahong.bamb.bamb.compose.BasePagingSource
import cn.changjiahong.bamb.bamb.compose.PageResponse
import cn.changjiahong.bamb.bamb.compose.PagingDataFlow
import cn.changjiahong.bamb.bamb.compose.createPager
import cn.changjiahong.bamb.bean.Post
import cn.changjiahong.bamb.repository.IReferralRepository
import cn.changjiahong.bamb.service.IPostService
import org.koin.core.annotation.Factory

/**
 *
 * @author ChangJiahong
 * @date 2025/1/26
 */

@Factory
class ReferralRepository(val postService: IPostService) : IReferralRepository {


    override fun createPostsPager(): PagingDataFlow<Post> {
        return createPager { PostSource(postService) }.flow
    }

}


class PostSource(val postService: IPostService) :
    BasePagingSource<Post>() {
    override suspend fun fetchData(
        page: Int,
        limit: Int
    ): PageResponse<Post> {
        val p = if (page > 1) page - 1 else 0
        return postService.getPostReferral(p * limit + 1, limit)
    }
}