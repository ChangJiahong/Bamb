package cn.changjiahong.bamb.app.main

import cafe.adriel.voyager.core.model.screenModelScope
import cn.changjiahong.bamb.bamb.http.model.RestResponse
import cn.changjiahong.bamb.bamb.mvi.MviScreenModel
import cn.changjiahong.bamb.bamb.uieffect.ToastEffect
import cn.changjiahong.bamb.bamb.mvi.UiEvent
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.converter.FlowConverterFactory
import de.jensklingenberg.ktorfit.converter.ResponseConverterFactory
import de.jensklingenberg.ktorfit.http.GET
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
class HomeScreenModel : MviScreenModel() {
    override fun handleEvent(event: UiEvent) {

    }


    fun cli() {
        val ktorfit =
            Ktorfit.Builder()
                .httpClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
                .converterFactories(FlowConverterFactory())
                .converterFactories(ResponseConverterFactory())
                .baseUrl("https://apifoxmock.com/m1/5740445-5423178-default/")
                .build()

        val api = ktorfit.createLoginService()

        screenModelScope.launch {

            // val g = httpClient.get("https://apifoxmock.com/m1/5740445-5423178-default/referralpost")

//            val map:Map<String,Any> = g.body()

            api.login().collect { value ->
                println(value)
            }

        }
    }


}

interface LoginService {
    @GET("referralpost")
    fun login(): Flow<Response<RestResponse>>
}
