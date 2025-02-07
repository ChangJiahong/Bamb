package cn.changjiahong.bamb.bamb

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import org.koin.core.annotation.Single

/**
 *
 * @author ChangJiahong
 * @date 2025/1/13
 */

sealed interface DefaultEffect : UiEffect {
    class Toast(val msg: String) : DefaultEffect
}

class GoEffect(val screen: Screen, val isReplace: Boolean = false) : DefaultEffect

@Single
class DefaultEffectHandler(val globalNavigator: Navigator) : UiEffectHandler {

    override fun onHandleEffect(effect: UiEffect) {
        when (effect) {
            is DefaultEffect.Toast -> {}

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