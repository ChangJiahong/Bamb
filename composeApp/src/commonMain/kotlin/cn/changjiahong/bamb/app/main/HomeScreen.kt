package cn.changjiahong.bamb.app.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingSourceLoadResultError
import app.cash.paging.PagingSourceLoadResultPage
import app.cash.paging.PagingState
import app.cash.paging.compose.collectAsLazyPagingItems
import bamb.composeapp.generated.resources.Res
import bamb.composeapp.generated.resources.app_name
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cn.changjiahong.bamb.bamb.http.getKtorfit
import cn.changjiahong.bamb.bamb.http.model.RestResponse
import cn.changjiahong.bamb.bamb.http.status.RestStatusCode
import cn.changjiahong.bamb.bamb.http.status.error
import cn.changjiahong.bamb.service.Api
import cn.changjiahong.bamb.service.createILoginService
import de.jensklingenberg.ktorfit.http.Field
import de.jensklingenberg.ktorfit.http.FormUrlEncoded
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Single

object HomeScreen : Tab {
    override val options: TabOptions
        @Composable get() = TabHost(0u, Res.string.app_name, Icons.Default.Home)

    @Composable
    override fun Content() = Home()
}

@Composable
private fun HomeScreen.Home() {
    val homeScreenModel = koinScreenModel<HomeScreenModel>()

    val pagingItems = homeScreenModel.pagingFlow.collectAsLazyPagingItems()

    LazyColumn(modifier = Modifier.height(400.dp)){
        items(pagingItems.itemCount){
            Row {
                Text("$it")
                Spacer(modifier = Modifier.width(8.dp)) // 添加间距
                Text(pagingItems[it]?.title ?: "NONONO")
            }
        }

        pagingItems.loadState.apply {
            when{
                refresh is LoadStateError -> {
                    item{
                        Text((refresh as LoadStateError).error.message?:"")
                    }
                }

                append is LoadStateLoading ->{
                    item{
                        Text("加载中。。。。。。", modifier = Modifier.height(20.dp))
                    }
                }
            }
        }

        item{
            Text("REERERERERRRRRRRRRRRRRRRRR")
        }

    }
}

@Serializable
data class Post(val title: String)

@Serializable
data class Page<T>(val offset: Int, val size: Int, val total: Int, val data: List<T>)

abstract class BasePagingSource<Value : Any> : PagingSource<Int, Value>() {

    protected abstract suspend fun fetchData(page: Int, limit: Int): PageResponse<Value>

    private fun transformPagingData(restResponse:PageResponse<Value>): Page<Value>{
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

            println("-----------__ $currentPage _------------------------")
            PagingSourceLoadResultPage(
                data = page.data,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = (currentPage + 1).takeIf { page.data.lastIndex >= currentPage }
            )


        } catch (e: Exception) {
            PagingSourceLoadResultError(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        return state.anchorPosition
    }

}

@Factory
class DSource(val postService: IPostService) : BasePagingSource<Post>() {
    override suspend fun fetchData(
        page: Int,
        limit: Int
    ): PageResponse<Post> {
        return postService.referralPost()
    }
}

typealias PageResponse<T> = RestResponse<Page<T>>



@Single
fun postService() = Api.getKtorfit().createIPostService()

interface IPostService {
    @GET(Api.REFERRAL_POST)
    suspend fun referralPost(
        @Query(value = "offset") offset: Int = 0,
        @Query("size") size: Int = 10
    ): PageResponse<Post>
}
