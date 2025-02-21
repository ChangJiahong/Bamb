package cn.changjiahong.bamb.app.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingSourceLoadResultError
import app.cash.paging.PagingSourceLoadResultPage
import app.cash.paging.PagingState
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import bamb.composeapp.generated.resources.Res
import bamb.composeapp.generated.resources.app_name
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cn.changjiahong.bamb.bamb.file.FileStorage
import cn.changjiahong.bamb.bamb.html.HtmlText
import cn.changjiahong.bamb.bamb.html.MarkdownView
import cn.changjiahong.bamb.bamb.html.jsStringValue
import cn.changjiahong.bamb.bamb.html.loadMarkdown
import cn.changjiahong.bamb.bamb.html.markdownContent
import cn.changjiahong.bamb.bamb.html.markdownData
import cn.changjiahong.bamb.bamb.html.markdownTemplate
import cn.changjiahong.bamb.bamb.html.rememberMarkdownViewController
import cn.changjiahong.bamb.bamb.html.rememberWebViewStateWithMarkdownData
import cn.changjiahong.bamb.bamb.http.getKtorfit
import cn.changjiahong.bamb.bamb.http.model.RestResponse
import cn.changjiahong.bamb.bamb.http.status.RestError
import cn.changjiahong.bamb.bamb.http.status.RestStatusCode
import cn.changjiahong.bamb.bamb.http.status.asRestError
import cn.changjiahong.bamb.bamb.http.status.error
import cn.changjiahong.bamb.service.Api
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLFile
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Single

object HomeScreen : Tab {
    override val options: TabOptions
        @Composable get() = TabHost(0u, Res.string.app_name, Icons.Default.Home)

    @Composable
    override fun Content() = Home()
}

private val json = Json { ignoreUnknownKeys = true }

@Composable
private fun HomeScreen.Home() {
    val homeScreenModel = koinScreenModel<HomeScreenModel>()

//    RefreshDemo(homeScreenModel)

    Column (modifier = Modifier.fillMaxWidth().height(400.dp)){

//        var p = FileStorage.getSharedResourceAssetsFilePath()
//
//        p = "file://$p"

//        val state =
////            rememberWebViewState("https://juejin.cn/post/7057112301446365192")
//
////            rememberWebViewStateWithMarkdownData("")
//            rememberWebViewStateWithHTMLFile( "markdownTemplate.html")
//
//        val navigator = rememberWebViewNavigator()

//        val markdownViewController = rememberMarkdownViewController()

        var md by remember { mutableStateOf(markdownContent) }

        Button(onClick = {

        }){
            Text("Set New")
        }

        Button(onClick = {

//            val jj = json.encodeToString(markdownContent)

//            println(jj)

            md = "## HHH \n # HAAA"

//            markdownViewController.setContent(markdownContent)

        }){
            Text("Back")
        }

//        val loadingState = markdownViewController.webViewState.loadingState
//        if (loadingState is LoadingState.Loading) {
//            LinearProgressIndicator(
//                progress = loadingState.progress,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }


        MarkdownView(md)

//        val url = "https://www.jetbrains.com/lp/compose-multiplatform/"
//        val webViewState =
//            rememberSaveableWebViewState(url).apply {
//                webSettings.logSeverity = KLogSeverity.Debug
//            }
//
//        val navigator = rememberWebViewNavigator()

//        LaunchedEffect(navigator) {
//            val bundle = webViewState.viewState
//            if (bundle == null) {
//                // This is the first time load, so load the home page.
//                navigator.loadUrl(url)
//            }
//        }


//        LaunchedEffect(Unit) {
//            initWebView(state)
//        }
//        WebView(
//            state,
//            modifier = Modifier.fillMaxSize(),
//            navigator=navigator
//        )

//        val str = """  Hello <span color="#ffff0000">World</span> !! <click action="ac1"> <b>Click</b> </click>  """
//        HtmlTextDemo()

    }

}

fun initWebView(webViewState: WebViewState) {
    webViewState.webSettings.apply {
        zoomLevel = 1.0
        isJavaScriptEnabled = true
        logSeverity = KLogSeverity.Debug
        allowFileAccessFromFileURLs = true
        allowUniversalAccessFromFileURLs = true
        androidWebSettings.apply {
            isAlgorithmicDarkeningAllowed = true
            safeBrowsingEnabled = true
            allowFileAccess = true
        }
    }
}

@Composable
private fun HtmlTextDemo() {
    val str =
        "Don’t have an account? <click action=\"action1\"><span color=\"#ff000000\"><b>Sign up!</b></span></click>"

    HtmlText(str, color = Color(0xff333333)) { action: String ->
        println(action)
    }
}

@Composable
private fun RefreshDemo(homeScreenModel: HomeScreenModel) {
    val pagingItems = homeScreenModel.pagingFlow.collectAsLazyPagingItems()

    var refreshing by remember { mutableStateOf(false) }
    // 用协程模拟一个耗时加载
    val scope = rememberCoroutineScope()

    RefreshLazyColumn(
        pagingItems, modifier = Modifier.fillMaxSize(),
        isRefreshing = refreshing,
        onRefresh = {
            scope.launch {
                refreshing = true
                delay(2000)
                refreshing = false
            }
            println("refreshhss")
        },
        refreshError = { Text(it.msg) },
        appendLoading = { Text("加载中。。。。。。", modifier = Modifier.height(20.dp)) }) {
        itemsIndexed(pagingItems) { index, item ->
            Row {
                Text("$index")
                Spacer(modifier = Modifier.width(8.dp)) // 添加间距
                Text(item?.title ?: "NONONO")
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> RefreshLazyColumn(
    pagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    refreshError: @Composable LazyItemScope.(RestError) -> Unit = {},
    appendError: @Composable LazyItemScope.(RestError) -> Unit = {},
    appendLoading: @Composable LazyItemScope.() -> Unit = {},
    content: LazyListScope.() -> Unit
) {

    PullToRefreshBox(isRefreshing, modifier = modifier, onRefresh = onRefresh) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            content()
            pagingItems.loadState.apply {
                when {
//                refresh is LoadStateLoading -> {}
//                refresh is LoadStateNotLoading -> {}
                    refresh is LoadStateError -> {
                        item {
                            refreshError((refresh as LoadStateError).error.asRestError())
                        }
                    }

//                append is LoadStateNotLoading -> {}

                    append is LoadStateLoading -> {
                        item {
                            appendLoading()
                        }
                    }

                    append is LoadStateError -> {
                        item {
                            appendError((append as LoadStateError).error.asRestError())
                        }
                    }
                }
            }
        }
    }
}

inline fun <T : Any> LazyListScope.items(
    items: LazyPagingItems<T>,
    noinline key: ((item: T?) -> Any)? = null,
    noinline contentType: (item: T?) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: T?) -> Unit
) = items(
    count = items.itemCount,
    key = if (key != null) { index: Int -> key(items[index]) } else null,
    contentType = { index: Int -> contentType(items[index]) }
) {
    itemContent(items[it])
}

inline fun <T : Any> LazyListScope.itemsIndexed(
    items: LazyPagingItems<T>,
    noinline key: ((index: Int, item: T?) -> Any)? = null,
    crossinline contentType: (index: Int, item: T?) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T?) -> Unit
) = items(
    count = items.itemCount,
    key = if (key != null) { index: Int -> key(index, items[index]) } else null,
    contentType = { index -> contentType(index, items[index]) }
) {
    itemContent(it, items[it])
}

@Serializable
data class Post(val title: String)

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
            PagingSourceLoadResultPage(
                data = page.data,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = (currentPage + 1).takeIf { page.total > currentPage * limit }
            )
        } catch (e: Exception) {
            PagingSourceLoadResultError(e.asRestError())
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
