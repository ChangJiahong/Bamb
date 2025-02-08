package cn.changjiahong.bamb.app.main

import cn.changjiahong.bamb.bamb.mvi.MviScreenModel
import cn.changjiahong.bamb.bamb.uieffect.ToastEffect
import cn.changjiahong.bamb.bamb.mvi.UiEvent
import org.koin.core.annotation.Factory

@Factory
class HomeScreenModel: MviScreenModel() {
    override fun handleEvent(event: UiEvent) {

    }

    fun cli() {
        ToastEffect("111").trigger()
    }


}