package cn.changjiahong.bamb.service

import cn.changjiahong.bamb.bamb.http.FlowRResponse
import cn.changjiahong.bamb.bamb.http.FlowRestResponse
import cn.changjiahong.bamb.bamb.http.getKtorfit
import cn.changjiahong.bamb.bamb.http.model.NoData
import cn.changjiahong.bamb.bean.UserInfo
import de.jensklingenberg.ktorfit.http.Field
import de.jensklingenberg.ktorfit.http.FormUrlEncoded
import de.jensklingenberg.ktorfit.http.POST
import org.koin.core.annotation.Single


@Single
fun loginService() = Api.getKtorfit().createILoginService()


interface ILoginService {

    @POST(Api.LOGIN)
    @FormUrlEncoded
    fun login(
        @Field(value = "email") email: String,
        @Field("password") password: String
    ): FlowRResponse<UserInfo>

}
