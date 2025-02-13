package cn.changjiahong.bamb.app.login

import cn.changjiahong.bamb.bamb.datastore.DataStores
import cn.changjiahong.bamb.bamb.datastore.dataStore
import cn.changjiahong.bamb.bamb.datastore.sync
import cn.changjiahong.bamb.bean.UserInfo
import kotlinx.serialization.Serializable

@Serializable
data class UserStore(
    val userInfo: UserInfo? = null,
    val isSaveInfo: Boolean = false,
    val username: String = "",
    val password: String = ""
)

val DataStores.userStore by dataStore(UserStore())
val userStore by DataStores.userStore.sync()


object LoginState {

    fun isLoggedIn(): Boolean {
        val userInfo = getUserInfo()
        return userInfo != null
                && userInfo.accessToken.isNotEmpty()
                && userInfo.refreshToken.isNotEmpty()
                && userInfo.nimAccount.isNotEmpty()
                && userInfo.nimToken.isNotEmpty()
    }

//    fun saveUserInfo(userInfo: UserInfo) {
//        var userStore by DataStores.userStore.sync()
//        userStore = userStore.copy(userInfo = userInfo)
//    }

    fun getUserInfo(): UserInfo? {
        val userStore by DataStores.userStore.sync()
        return userStore.userInfo
    }

}