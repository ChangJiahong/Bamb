package cn.changjiahong.bamb.app.main

import cafe.adriel.voyager.core.model.screenModelScope
import cn.changjiahong.bamb.bamb.http.asData
import cn.changjiahong.bamb.bamb.http.collectIn
import cn.changjiahong.bamb.bamb.http.status.RestError
import cn.changjiahong.bamb.bamb.mvi.MviScreenModel
import cn.changjiahong.bamb.bamb.mvi.UiEvent
import cn.changjiahong.bamb.bamb.uieffect.ToastEffect
import cn.changjiahong.bamb.service.TestService
import kotlinx.coroutines.flow.catch
import org.koin.core.annotation.Factory

@Factory
class HomeScreenModel(val testService: TestService) : MviScreenModel() {
    override fun handleEvent(event: UiEvent) {

    }

    fun cli() {


        testService.t1().catch { cause ->
            when(cause){
                is RestError -> ToastEffect(cause.restStatusCode.id).trigger()
            }
        }.collectIn(screenModelScope) { value ->
            println("success=$value")
        }

    }


}
