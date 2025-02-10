package cn.changjiahong.bamb.app.main

import cafe.adriel.voyager.core.model.screenModelScope
import cn.changjiahong.bamb.bamb.mvi.MviScreenModel
import cn.changjiahong.bamb.bamb.uieffect.ToastEffect
import cn.changjiahong.bamb.bamb.mvi.UiEvent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
class HomeScreenModel: MviScreenModel() {
    override fun handleEvent(event: UiEvent) {

    }

    val httpClient by lazy { HttpClient(){
        install(ContentNegotiation) {
            json()
        }
    } }

    fun cli() {
        screenModelScope.launch {
            val g = httpClient.get("https://apifoxmock.com/m1/5740445-5423178-default/referralpost")

//            val map:Map<String,Any> = g.body()
            println(g.bodyAsText())

        }
    }


}