package cn.changjiahong.bamb.app.login

import cn.changjiahong.bamb.bamb.mvi.MviScreenModel
import cn.changjiahong.bamb.bamb.mvi.UiEvent
import cn.changjiahong.bamb.bamb.mvi.UiState
import org.koin.core.annotation.Factory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.*


data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val reEnterPassword: String = ""
) : UiState

sealed interface RegisterUiEvent : UiEvent {
    data class EnterUsername(val username: String): RegisterUiEvent
    data class EnterEmail(val email: String): RegisterUiEvent
    data class EnterPassword(val password: String): RegisterUiEvent
    data class EnterRePassword(val reEnterPassword: String): RegisterUiEvent
}

@Factory
class RegisterViewModel : MviScreenModel() {


    private val _uiState by lazy { MutableStateFlow(RegisterUiState()) }
    val uiState = _uiState.asStateFlow()

    override fun handleEvent(event: UiEvent) {
        if (event !is RegisterUiEvent){
            return
        }
        when(event){
            is RegisterUiEvent.EnterEmail -> _uiState.update { it.copy(email = event.email) }
            is RegisterUiEvent.EnterPassword -> _uiState.update { it.copy(password = event.password) }
            is RegisterUiEvent.EnterRePassword -> _uiState.update { it.copy(reEnterPassword = event.reEnterPassword) }
            is RegisterUiEvent.EnterUsername -> _uiState.update { it.copy(username = event.username) }
        }
    }
}
