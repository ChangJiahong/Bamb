package cn.changjiahong.bamb.bamb.uieffect

import androidx.compose.runtime.Composable
import cn.changjiahong.bamb.bamb.mvi.UiEffect
import com.dokar.sonner.Toast
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterDefaults
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import kotlin.time.Duration

/**
 *
 * @author ChangJiahong
 * @date 2025/2/8
 */
sealed interface IToastEffect : UiEffect

class ToastEffect(
    val message: Any,
    val icon: Any? = null,
    val action: Any? = null,
    val type: ToastType = ToastType.Normal,
    val duration: Duration = ToasterDefaults.DurationDefault
) : IToastEffect {
    val toast: Toast
        get() = Toast(
            message = message,
            icon = icon,
            action = action,
            type = type,
            duration = duration
        )
}


@Composable
fun Toaster() {
    var toaster = rememberToasterController()
    Toaster(state = toaster)
}

@Composable
fun rememberToasterController(): ToasterState {
    var toaster = rememberToasterState()
    UiEffectDispatcher.register { effect ->
        when (effect) {
            is ToastEffect -> toaster.show(effect.toast)
            else -> false
        }
        true
    }
    return toaster
}