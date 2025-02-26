package cn.changjiahong.bamb.app.main

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import app.cash.paging.cachedIn
import cafe.adriel.voyager.core.model.screenModelScope
import cn.changjiahong.bamb.app.RR
import cn.changjiahong.bamb.app.main.HomeUiEffect.RefreshEffect
import cn.changjiahong.bamb.bamb.mvi.MviScreenModel
import cn.changjiahong.bamb.bamb.mvi.UiEffect
import cn.changjiahong.bamb.bamb.mvi.UiEvent
import cn.changjiahong.bamb.bamb.uieffect.GoEffect
import cn.changjiahong.bamb.bamb.uieffect.ToastEffect
import cn.changjiahong.bamb.bean.Post
import cn.changjiahong.bamb.repository.IReferralRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Factory
import org.koin.core.component.get

//val DataStores.uu by dataStore(UU("aaa"))

sealed class HomeUiEvent : UiEvent {
    object RefreshEvent : HomeUiEvent()
    object FinishRefreshEvent : HomeUiEvent()
    class OnPostItemClickEvent(val post: Post) : HomeUiEvent()
}

sealed interface HomeUiEffect : UiEffect {
    object RefreshEffect : HomeUiEffect
}

@Factory
class HomeScreenModel(val referralRepository: IReferralRepository) :
    MviScreenModel() {

    val postsDataFlow by lazy {
        referralRepository.createPostsPager().cachedIn(screenModelScope) // 绑定生命周期，防止流失效
    }

    val lazyListState: LazyListState by lazy {
        LazyListState()
    }

    private val _refreshState = MutableStateFlow(true)
    val isRefreshingState = _refreshState.asStateFlow()

    override fun handleEvent(event: UiEvent) {
        if (event !is HomeUiEvent) {
            return
        }
        when (event) {
            is HomeUiEvent.RefreshEvent -> {
                _refreshState.value = true
                RefreshEffect.trigger()
            }

            is HomeUiEvent.FinishRefreshEvent -> {
                _refreshState.value = false
            }

            is HomeUiEvent.OnPostItemClickEvent -> {
                GoEffect(RR.POST(event.post)).trigger()
            }
        }
    }


    fun cli() {


//        GoEffect(RR.OTHER).trigger()

//        testService.t1().catch { cause ->
//            when(cause){
//                is RestError -> ToastEffect(cause.restStatusCode.id).trigger()
//            }
//        }.collectIn(screenModelScope) { value ->
//            println("success=$value")
//        }

    }

}
