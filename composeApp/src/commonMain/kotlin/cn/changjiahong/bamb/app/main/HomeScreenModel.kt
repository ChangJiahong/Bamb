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
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory

@Factory
class HomeScreenModel : MviScreenModel() {
    override fun handleEvent(event: UiEvent) {

    }

    val ktorfit by lazy {
        Ktorfit.Builder()
            .httpClient {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
            }
            .converterFactories(FlowConverterFactory())
            .converterFactories(ResponseConverterFactory())
            .baseUrl("https://apifoxmock.com/m1/5740445-5423178-default/")
            .build()
    }

    fun cli() {


        val api = ktorfit.createLoginService()

        screenModelScope.launch {

//             val g = httpClient.get("https://apifoxmock.com/m1/5740445-5423178-default/referralpost")

//            val map:Map<String,Any> = g.body()

            api.login().collect { value ->
                println(value.data?.get(0))
            }

        }
    }


}

interface LoginService {
    @GET("referralpost")
    fun login(): Flow<RestResponse<Array<UU>>>
}

@Serializable
data class UU(val title:String)

typealias FlowResponse<T> = Flow<Response<T>>
typealias FlowRestResponse<T> = Flow<RestResponse<T>>