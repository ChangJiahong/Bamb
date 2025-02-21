package cn.changjiahong.bamb.service

import cn.changjiahong.bamb.bamb.compose.PageResponse
import cn.changjiahong.bamb.bamb.http.getKtorfit
import cn.changjiahong.bamb.bean.Post
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import org.koin.core.annotation.Single

/**
 *
 * @author ChangJiahong
 * @date 2025/1/26
 */


@Single
fun postService() = Api.getKtorfit().createIPostService()


interface IPostService {
    @GET(Api.REFERRAL_POST)
    suspend fun getPostReferral(
        @Query(value = "offset") offset: Int = 0,
        @Query("size") size: Int = 10
    ): PageResponse<Post>
}