package cn.changjiahong.bamb.repository

import cn.changjiahong.bamb.bean.UserInfo
import kotlinx.coroutines.flow.Flow

interface ILoginRepository {
    fun login(email: String, password: String): Flow<UserInfo>

}