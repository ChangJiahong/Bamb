package cn.changjiahong.bamb.repository.impl

import cn.changjiahong.bamb.bamb.http.asData
import cn.changjiahong.bamb.bamb.http.onHeaders
import cn.changjiahong.bamb.bean.UserInfo
import cn.changjiahong.bamb.repository.ILoginRepository
import cn.changjiahong.bamb.service.ILoginService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class LoginRepositoryImpl(val loginService: ILoginService) :ILoginRepository{
    private val ACCESS_TOKEN = "access_token"
    private val REFRESH_TOKEN = "refresh_token"

    override fun login(email: String, password: String): Flow<UserInfo> {
        var accessToken = ""
        var refreshToken = ""
        return loginService.login(email, password)
            .onHeaders {
                accessToken = it[ACCESS_TOKEN] ?: ""
                refreshToken = it[REFRESH_TOKEN] ?: ""
            }
            .asData().map { value ->
                value.copy(accessToken = accessToken, refreshToken = refreshToken)
            }

    }
}