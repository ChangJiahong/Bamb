package cn.changjiahong.bamb.bamb.http

import cn.changjiahong.bamb.bamb.http.model.NoData
import cn.changjiahong.bamb.bamb.http.model.RestResponse
import de.jensklingenberg.ktorfit.http.GET
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable


interface TestService {
    @GET("t1")
    fun t1(): FlowResponse<NoData>
}

@Serializable
data class UU(val title:String)