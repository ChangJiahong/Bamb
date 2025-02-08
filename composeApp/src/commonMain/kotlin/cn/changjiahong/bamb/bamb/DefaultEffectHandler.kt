package cn.changjiahong.bamb.bamb

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.dokar.sonner.Toast
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterDefaults
import com.dokar.sonner.ToasterState
import org.koin.core.annotation.Single
import kotlin.time.Duration

/**
 *
 * @author ChangJiahong
 * @date 2025/1/13
 */

sealed interface DefaultEffect : UiEffect {
    class Toast(
        val message: Any,
        val icon: Any? = null,
        val action: Any? = null,
        val type: ToastType = ToastType.Normal,
        val duration: Duration = ToasterDefaults.DurationDefault
    ) : DefaultEffect
}

fun DefaultEffect.Toast.to(): Toast {
    return Toast(message = message, icon = icon, action = action, type = type, duration = duration)
}

class GoEffect(val screen: Screen, val isReplace: Boolean = false) : DefaultEffect

@Single
class DefaultEffectHandler(val globalNavigator: Navigator, val toasterState: ToasterState) :
    UiEffectHandler {

    override fun onHandleEffect(effect: UiEffect) {
        when (effect) {
            is DefaultEffect.Toast -> {
                toasterState.show(effect.to())
            }

            is GoEffect -> {
                if (effect.isReplace) {
                    globalNavigator.replace(effect.screen)
                } else {
                    globalNavigator += effect.screen
                }
            }

        }
    }
}