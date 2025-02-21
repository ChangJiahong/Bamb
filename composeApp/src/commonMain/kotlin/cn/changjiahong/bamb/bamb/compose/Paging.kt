package cn.changjiahong.bamb.bamb.compose

import androidx.paging.PagingConfig
import app.cash.paging.Pager
import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingState
import app.cash.paging.cachedIn
import cafe.adriel.voyager.core.model.screenModelScope
import cn.changjiahong.bamb.bamb.http.model.RestResponse
import cn.changjiahong.bamb.bamb.http.status.RestStatusCode
import cn.changjiahong.bamb.bamb.http.status.asRestError
import cn.changjiahong.bamb.bamb.http.status.error
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

/**
 *
 * @author ChangJiahong
 * @date 2025/2/21
 */

typealias PageResponse<T> = RestResponse<Page<T>>

typealias IntPager<T> = Pager<Int, T>

typealias PagingDataFlow<T> = Flow<PagingData<T>>

fun <T : Any> createPager(
    pageSize: Int = 20, // 每页大小
    enablePlaceholders: Boolean = false,// 是否启用占位符
    prefetchDistance: Int = 2, // 预加载 2 页
    pagingSourceFactory:()-> BasePagingSource<T>
): IntPager<T> {
    return Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = enablePlaceholders,
            prefetchDistance = prefetchDistance
        ),
        pagingSourceFactory = pagingSourceFactory
    )
}

@Serializable
data class Page<T>(val offset: Int, val size: Int, val total: Int, val data: List<T>)

abstract class BasePagingSource<Value : Any> : PagingSource<Int, Value>() {

    protected abstract suspend fun fetchData(page: Int, limit: Int): PageResponse<Value>

    private fun transformPagingData(restResponse: PageResponse<Value>): Page<Value> {
        if (restResponse.status == RestStatusCode.OK) {
            if (restResponse.data != null) {
                return (restResponse.data)
            }
            throw RestStatusCode.NoDataError.error()
        } else {
            throw restResponse.status.error(
                if (restResponse.msg.isNotEmpty()) restResponse.msg else restResponse.status.id,
                restResponse.data
            )
        }
    }

    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, Value> {
        val currentPage = params.key ?: 1
        val limit = params.loadSize
        return try {
            val page = transformPagingData(fetchData(currentPage, limit))
            LoadResult.Page(
                data = page.data,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = (currentPage + 1).takeIf { page.total > currentPage * limit }
            )
        } catch (e: Exception) {
            LoadResult.Error(e.asRestError())
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        // 返回当前显示的数据项所在的页码或 ID
        return state.anchorPosition?.let { position ->
            // 从当前页面位置计算出应该从哪个位置开始刷新
            state.closestPageToPosition(position)?.prevKey?.plus(1) ?: state.closestPageToPosition(
                position
            )?.nextKey?.minus(1)
        }
    }

}