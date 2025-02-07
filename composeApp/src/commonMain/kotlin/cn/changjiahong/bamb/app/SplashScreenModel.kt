package cn.changjiahong.bamb.app

import cn.changjiahong.bamb.bamb.GoEffect
import cn.changjiahong.bamb.bamb.MviScreenModel
import cn.changjiahong.bamb.bamb.UiEvent
import org.koin.core.annotation.Factory

@Factory
class SplashScreenModel : MviScreenModel() {

    override fun handleEvent(event: UiEvent) {

    }


    // 尝试自动登录
    fun tryAutoLogin() {
        GoEffect(MainScreen,true).trigger()
    }
}