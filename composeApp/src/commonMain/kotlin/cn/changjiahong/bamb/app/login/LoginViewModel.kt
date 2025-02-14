package cn.changjiahong.bamb.app.login

import androidx.compose.runtime.Stable
import cafe.adriel.voyager.core.model.screenModelScope
import cn.changjiahong.bamb.app.RR
import cn.changjiahong.bamb.bamb.datastore.DataStores
import cn.changjiahong.bamb.bamb.datastore.sync
import cn.changjiahong.bamb.bamb.http.collectIn
import cn.changjiahong.bamb.bamb.http.status.RestError
import cn.changjiahong.bamb.bamb.mvi.MviScreenModel
import cn.changjiahong.bamb.bamb.mvi.UiEvent
import cn.changjiahong.bamb.bamb.mvi.UiState
import cn.changjiahong.bamb.bamb.uieffect.GoEffect
import cn.changjiahong.bamb.bamb.uieffect.ToastEffect
import cn.changjiahong.bamb.repository.ILoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Factory


@Stable
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String = "",
    val pwdError: String = ""
) : UiState

sealed interface LoginUiEvent : UiEvent {
    data class EnterEmail(val email: String) : LoginUiEvent
    data class EnterPassword(val password: String) : LoginUiEvent
    data class SubmitLogin(val isSaveInfo: Boolean) : LoginUiEvent
}


@Factory
class LoginViewModel(val loginRepository: ILoginRepository) : MviScreenModel() {


    private val _uiState by lazy { MutableStateFlow(LoginUiState()) }
    val uiState = _uiState.asStateFlow()

    private var userStore by DataStores.userStore.sync()

    init {
        _uiState.update { state ->
            state.copy(
                email = userStore.username,
                password = userStore.password
            )
        }
    }


    override fun handleEvent(event: UiEvent) {
        if (event !is LoginUiEvent) {
            return
        }
        when (event) {
            is LoginUiEvent.EnterEmail -> _uiState.update { it.copy(email = event.email) }
            is LoginUiEvent.EnterPassword -> _uiState.update { it.copy(password = event.password) }
            is LoginUiEvent.SubmitLogin -> login(event.isSaveInfo)
        }

    }


    fun login(isSaveInfo: Boolean) {

        val username = uiState.value.email
        val password = uiState.value.password

        if (!checkEmail() || !checkPwd()) {
            return
        }
        if (isSaveInfo) {
            userStore =
                userStore.copy(isSaveInfo = true, username = username, password = password)
        }
        loginRepository.login(username, password)
            .catch { cause ->
                if (cause is RestError) {
                    ToastEffect(cause.msg).trigger()
                }
            }.collectIn(screenModelScope) { value ->
                userStore = userStore.copy(userInfo = value)
                GoEffect(RR.MAIN, true).trigger()
            }

    }


    fun checkEmail(): Boolean {
        val username = uiState.value.email
        var msg = ""
        if (username.isEmpty()) {
            msg = "邮箱不能为空"
        } else if (!isValidEmail(username)) {
            msg = "邮箱格式不正确"
        }
        _uiState.update { it.copy(emailError = msg) }
        return msg.isEmpty()
    }

    // Validate email with a regular expression
    fun isValidEmail(email: String): Boolean {
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(emailPattern)
    }


    fun checkPwd(): Boolean {
        var msg = ""
        if (uiState.value.password.isEmpty()) {
            msg = "密码不能为空"
        }
        _uiState.update { it.copy(pwdError = msg) }
        return msg.isEmpty()
    }

    fun goRegsiter() {
        GoEffect(RR.REGISTER).trigger()
    }

}