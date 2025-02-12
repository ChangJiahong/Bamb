package cn.changjiahong.bamb.bamb.http

import cn.changjiahong.bamb.bamb.http.status.RestStatusCode
import cn.changjiahong.bamb.bamb.http.status.error
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


public class FlowConverterFactory : Converter.Factory {
    override fun responseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit,
    ): Converter.ResponseConverter<HttpResponse, *>? {
        if (typeData.typeInfo.type == Flow::class) {
            return object : Converter.ResponseConverter<HttpResponse, Flow<Any?>> {
                override fun convert(getResponse: suspend () -> HttpResponse): Flow<Any?> {
                    val requestType = typeData.typeArgs.first()
                    return flow {
                        val response = getResponse()
                        if (requestType.typeInfo.type == HttpResponse::class) {
                            emit(response)
                        } else {
                            if (response.status == HttpStatusCode.OK) {
                                val convertedBody =
                                    ktorfit.nextSuspendResponseConverter(
                                        this@FlowConverterFactory,
                                        typeData.typeArgs.first(),
                                    )?.convert(KtorfitResult.Success(response))
                                        ?: response.body(typeData.typeArgs.first().typeInfo)
                                emit(convertedBody)
                            } else {
                                throw RestStatusCode.HttpError.error(
                                    response.status.toString(),
                                    response
                                )
                            }
                        }
                    }
                }
            }
        }
        return null
    }
}
