package cn.changjiahong.bamb.app.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import app.cash.paging.compose.collectAsLazyPagingItems
import bamb.composeapp.generated.resources.Res
import bamb.composeapp.generated.resources.app_name
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cn.changjiahong.bamb.GlobalNavigator
import cn.changjiahong.bamb.bamb.compose.refresh.RefreshLazyColumn
import cn.changjiahong.bamb.bamb.compose.refresh.itemsIndexed
import cn.changjiahong.bamb.bamb.compose.refresh.rememberLazyListState
import cn.changjiahong.bamb.bamb.html.HtmlText
import cn.changjiahong.bamb.bamb.html.MarkdownView
import cn.changjiahong.bamb.bamb.html.markdownContent
import cn.changjiahong.bamb.bamb.rttext.demo
import cn.changjiahong.bamb.bean.Post
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.web.WebViewState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.serialization.json.Json

object HomeScreen : Tab {
    override val options: TabOptions
        @Composable get() = TabHost(0u, Res.string.app_name, Icons.Default.Home)

    @Composable
    override fun Content() = Home()
}

@Composable
private fun HomeScreen.Home() {
    val homeScreenModel = //koinScreenModel<HomeScreenModel>()
        GlobalNavigator.current.koinNavigatorScreenModel<HomeScreenModel>()

    demo()

//    RefreshDemo(homeScreenModel)

//    Column(modifier = Modifier.fillMaxSize()) {


//        MarkdownViewDemo()


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

//    }

}

@Composable
private fun MarkdownViewDemo() {
    var md by remember { mutableStateOf(markdownContent) }

    Button(onClick = {

        md = "## HHH \n # HAAA"

    }) {
        Text("Back")
    }

//        val loadingState = markdownViewController.webViewState.loadingState
//        if (loadingState is LoadingState.Loading) {
//            LinearProgressIndicator(
//                progress = loadingState.progress,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }

    val sb = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(state = sb)) {
        Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.Red)) {
            Text("标题布局")
        }
        MarkdownView(md, modifier = Modifier.fillMaxSize())
        Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.Red)) {
            Text("Footer布局")
        }
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
    val pagingItems = homeScreenModel.postsDataFlow.collectAsLazyPagingItems()

    val isRefreshing by homeScreenModel.isRefreshingState.collectAsState()

    val lazyListState = homeScreenModel.lazyListState

    homeScreenModel.handleEffect { effect ->
        when (effect) {
            is HomeUiEffect.RefreshEffect -> {
                pagingItems.refresh()
                true
            }

            else -> false
        }
    }

    RefreshLazyColumn(
        pagingItems,
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        isRefreshing = isRefreshing,
        onRefresh = {
            HomeUiEvent.RefreshEvent.sendTo(homeScreenModel)
//            println("--开始刷新--")
        },
//        refreshing = {
//            println("--正在刷新--")
//        },
        refreshFinish = {
            HomeUiEvent.FinishRefreshEvent.sendTo(homeScreenModel)
//            println("--刷新完成--")
        },
//        loadingMore = {
//            println("--正在加载更多--")
//        },
//        loadMoreFinish = {
//            println("--加载更多完成--")
//        },
        refreshError = { Text(it.msg) },
        appendLoading = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("加载中...", modifier = Modifier.height(20.dp))
            }
        }
    ) {

        itemsIndexed(pagingItems) { index, item ->
            item?.let { post ->
                PostItem(post) {
                    HomeUiEvent.OnPostItemClickEvent(it).sendTo(homeScreenModel)
                }
            }
        }

    }
}


@Composable
fun PostItem(postItem: Post, onItemClick: (Post) -> Unit = {}) {
    Card(onClick = { onItemClick(postItem) }) {
        ConstraintLayout(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {

            val (titleRef, authorRef, introRef, coverRef, avatarRef) = createRefs()

            Text(
                postItem.title,
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(coverRef.start, 5.dp)
                    width = Dimension.fillToConstraints
                }, style = MaterialTheme.typography.titleMedium,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )


            CoilImage(
                imageModel = { postItem.avatar }, // loading a network image or local resource using an URL.
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = Modifier
                    .constrainAs(avatarRef) {
                        top.linkTo(titleRef.bottom, 5.dp)
                        start.linkTo(parent.start)
                    }
                    .clip(RoundedCornerShape(21.dp))
                    .size(21.dp)
            )


            Text(
                postItem.author, modifier = Modifier
                    .constrainAs(authorRef) {
                        top.linkTo(avatarRef.top)
                        bottom.linkTo(avatarRef.bottom)
                        start.linkTo(avatarRef.end, 5.dp)
                        end.linkTo(coverRef.start, 5.dp)
                        width = Dimension.fillToConstraints
                    }, style = MaterialTheme.typography.labelMedium
            )


            Text(postItem.intro, modifier = Modifier.constrainAs(introRef) {
                top.linkTo(avatarRef.bottom, 5.dp)
                start.linkTo(parent.start)
                end.linkTo(coverRef.start, 5.dp)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }, maxLines = 2, minLines = 2, overflow = TextOverflow.Ellipsis)

            CoilImage(
                imageModel = { postItem.cover }, // loading a network image or local resource using an URL.
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = Modifier
                    .constrainAs(coverRef) {
                        top.linkTo(titleRef.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(introRef.bottom)
                        height = Dimension.fillToConstraints
                    }
                    .aspectRatio(4 / 3f)
                    .clip(RoundedCornerShape(5.dp))
            )

        }

    }
}







