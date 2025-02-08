package cn.changjiahong.bamb.bamb.uieffect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cn.changjiahong.bamb.bamb.mvi.UiEffect
import cn.changjiahong.bamb.bamb.mvi.UiEffectHandler

/**
 *
 * @author ChangJiahong
 * @date 2025/2/8
 */
object UiEffectDispatcher {
    private val handlers: MutableSet<UiEffectHandler> = mutableSetOf()

    @Composable
    fun register(priority: Int = Int.MIN_VALUE, handler: UiEffectHandler) {
        LaunchedEffect(handler) {
            registerHandler(priority, handler)
        }
    }

    // 注册处理器
    fun registerHandler(priority: Int = Int.MIN_VALUE, handler: UiEffectHandler) {
        handlers.add(handler)
    }


    // 注销处理器
    fun unRegisterHandler(handler: UiEffectHandler) {
        handlers.remove(handler)
    }

    // 事件分发
    fun dispatchEffect(effect: UiEffect) {
        handlers.forEach { handler ->
            if (handler(effect)) {
                return
            }
        }
    }
}