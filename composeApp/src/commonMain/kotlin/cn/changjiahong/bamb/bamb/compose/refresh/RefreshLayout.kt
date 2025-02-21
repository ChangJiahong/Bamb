package cn.changjiahong.bamb.bamb.compose.refresh

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.LoadStateNotLoading
import app.cash.paging.compose.LazyPagingItems
import cn.changjiahong.bamb.bamb.http.status.RestError
import cn.changjiahong.bamb.bamb.http.status.asRestError

/**
 *
 * @author ChangJiahong
 * @date 2025/2/21
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> RefreshLazyColumn(
    pagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    refreshing: () -> Unit = {}, // 正在刷新
    refreshFinish: () -> Unit = {}, // 刷新完成
    loadingMore: () -> Unit = {}, // 正在加载更多
    loadMoreFinish: () -> Unit = {}, // 加载更多完成
    refreshError: @Composable LazyItemScope.(RestError) -> Unit = {},
    appendError: @Composable LazyItemScope.(RestError) -> Unit = {},
    appendLoading: @Composable LazyItemScope.() -> Unit = {},
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    content: LazyListScope.() -> Unit
) {

    PullToRefreshBox(isRefreshing, modifier = modifier, onRefresh = onRefresh) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement
        ) {
            content()
            pagingItems.loadState.apply {
                when {
                    isRefreshing -> {
                        when (refresh) {
                            is LoadStateLoading -> {
                                refreshing()
                            }

                            is LoadStateNotLoading -> {
                                refreshFinish()
                            }

                            is LoadStateError -> {
                                item {
                                    (refresh as LoadStateError).error.printStackTrace()
                                    refreshError((refresh as LoadStateError).error.asRestError())
                                }
                            }
                        }
                    }

                    else -> {
                        when (append) {
                            is LoadStateNotLoading -> {
                                loadMoreFinish()
                            }

                            is LoadStateLoading -> {
                                loadingMore()
                                item {
                                    appendLoading()
                                }
                            }

                            is LoadStateError -> {
                                item {
                                    (refresh as LoadStateError).error.printStackTrace()
                                    appendError((append as LoadStateError).error.asRestError())
                                }
                            }
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