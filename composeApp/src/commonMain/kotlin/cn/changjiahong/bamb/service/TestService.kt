package cn.changjiahong.bamb.service

import cn.changjiahong.bamb.bamb.http.FlowRResponse
import cn.changjiahong.bamb.bamb.http.FlowResponse
import cn.changjiahong.bamb.bamb.http.getKtorfit
import de.jensklingenberg.ktorfit.http.GET
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Single

@Single
fun testService() = Api.getKtorfit().createTestService()

interface TestService {
    @GET("t1")
    fun t1(): FlowResponse<UU>

    @GET("referralpost")
    fun t2(): FlowRResponse<List<UU>>
}

@Serializable
data class UU(val title:String)