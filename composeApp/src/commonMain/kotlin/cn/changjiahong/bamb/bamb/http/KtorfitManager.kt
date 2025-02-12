package cn.changjiahong.bamb.bamb.http

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.ResponseConverterFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


/**
 * @description
 * @author ChangJiahong
 * @date 2021/9/12
 */
object KtorfitManager {

    private val ktorfitMap = HashMap<String, Ktorfit>()

    fun getKtorfit(url: String): Ktorfit {
        return ktorfitMap[url] ?: createKtorfit(url)
    }

    private fun createKtorfit(url: String): Ktorfit {
        val ktorfit = Ktorfit.Builder()
            .httpClient {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
            }
            .converterFactories(FlowConverterFactory())
            .converterFactories(ResponseConverterFactory())
            .baseUrl(url)
            .build()
        registerKtorfit(url, ktorfit)
        return ktorfit
    }

    fun registerKtorfit(url: String) {
        if (!ktorfitMap.containsKey(url)) {
            createKtorfit(url)
        }
    }

    fun registerKtorfit(url: String, ktorfit: Ktorfit) {
        ktorfitMap[url] = ktorfit
    }

}
