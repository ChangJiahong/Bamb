package cn.changjiahong.bamb.app.main

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.cachedIn
import cafe.adriel.voyager.core.model.screenModelScope
import cn.changjiahong.bamb.app.RR
import cn.changjiahong.bamb.bamb.datastore.DataStores
import cn.changjiahong.bamb.bamb.datastore.dataStore
import cn.changjiahong.bamb.bamb.http.asData
import cn.changjiahong.bamb.bamb.http.collectIn
import cn.changjiahong.bamb.bamb.http.status.RestError
import cn.changjiahong.bamb.bamb.mvi.MviScreenModel
import cn.changjiahong.bamb.bamb.mvi.UiEvent
import cn.changjiahong.bamb.bamb.uieffect.GoEffect
import cn.changjiahong.bamb.bamb.uieffect.ToastEffect
import cn.changjiahong.bamb.service.TestService
import cn.changjiahong.bamb.service.UU
import kotlinx.coroutines.flow.catch
import org.koin.core.annotation.Factory

val DataStores.uu by dataStore(UU("aaa"))

@Factory
class HomeScreenModel(val testService: TestService,val dSource: DSource) : MviScreenModel() {


    val pagingFlow = Pager(
        config = PagingConfig(
            pageSize = 10, // 每页大小
            enablePlaceholders = false, // 是否启用占位符
            prefetchDistance = 2 // 预加载 2 页
        ),
        pagingSourceFactory = { dSource }
    ).flow
        .cachedIn(screenModelScope) // 绑定生命周期，防止流失效

    override fun handleEvent(event: UiEvent) {

    }


    fun cli() {

        GoEffect(RR.OTHER).trigger()

//        testService.t1().catch { cause ->
//            when(cause){
//                is RestError -> ToastEffect(cause.restStatusCode.id).trigger()
//            }
//        }.collectIn(screenModelScope) { value ->
//            println("success=$value")
//        }

    }


}
