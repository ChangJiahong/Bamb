package cn.changjiahong.bamb.app

import cn.changjiahong.bamb.app.login.LoginState
import cn.changjiahong.bamb.bamb.mvi.MviScreenModel
import cn.changjiahong.bamb.bamb.mvi.UiEvent
import cn.changjiahong.bamb.bamb.uieffect.GoEffect
import cn.changjiahong.bamb.service.ILoginService
import org.koin.core.annotation.Factory

@Factory
class SplashScreenModel() : MviScreenModel() {

    override fun handleEvent(event: UiEvent) {

    }


    // 尝试自动登录
    fun tryAutoLogin() {
        if (LoginState.isLoggedIn()) {
            val userInfo = LoginState.getUserInfo()!!
            GoEffect(RR.MAIN,false).trigger()

//            nimLoginRepository.login(userInfo.nimAccount, userInfo.nimToken).catch { cause ->
//                DefaultEffect.Go(RR.LOGIN, true).trigger()
//            }.collectIn(viewModelScope) { value ->
//                DefaultEffect.Go(RR.MAIN_SCREEN, true).trigger()
//            }
        } else {
            GoEffect(RR.LOGIN,true).trigger()
        }
    }
}