package cn.changjiahong.bamb.bamb

interface UiEvent {
    fun sendTo(viewModel: MviScreenModel) {
        viewModel.sendEvent(this)
    }
}

interface UiState
interface UiEffect

interface UiEffectHandler {
    fun onHandleEffect(effect: UiEffect)
}

