package cn.changjiahong.bamb.bamb

import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

abstract class MviScreenModel : ScreenModel, KoinComponent {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    private val uiEvent = _uiEvent.asSharedFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    private val uiEffect = _uiEffect.asSharedFlow()

    private val uiEffectHandler: UiEffectHandler by lazy { get<UiEffectHandler>() }

    private var _handleEffect: (effect: UiEffect) -> Unit = { uiEffectHandler.onHandleEffect(it) }


    init {
        screenModelScope.launch {
            uiEvent.collect { value ->
                handleEvent(value)
            }
        }
        screenModelScope.launch {
            uiEffect.collect { value ->
                _handleEffect(value)
            }
        }
    }

    fun sendEvent(intent: UiEvent) {
        screenModelScope.launch {
            _uiEvent.emit(intent)
        }
    }

    fun sendEffect(intent: UiEffect) {
        screenModelScope.launch {
            _uiEffect.emit(intent)
        }
    }

    fun UiEffect.trigger() {
        sendEffect(this)
    }


    //    protected abstract fun initialState(): S
    protected abstract fun handleEvent(event: UiEvent)

    open fun handleEffect(handle: (effect: UiEffect) -> Boolean) {
        _handleEffect = { if (!handle(it)) uiEffectHandler.onHandleEffect(it) }
    }
}