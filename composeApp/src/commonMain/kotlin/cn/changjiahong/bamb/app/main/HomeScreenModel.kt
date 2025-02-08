package cn.changjiahong.bamb.app.main

import cn.changjiahong.bamb.bamb.DefaultEffect
import cn.changjiahong.bamb.bamb.MviScreenModel
import cn.changjiahong.bamb.bamb.UiEvent
import org.koin.core.annotation.Factory

@Factory
class HomeScreenModel: MviScreenModel() {
    override fun handleEvent(event: UiEvent) {

    }

    fun cli() {
        DefaultEffect.Toast("111").trigger()
    }


}