package cn.changjiahong.bamb

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.internal.BackHandler
import cn.changjiahong.bamb.app.MainScreen
import cn.changjiahong.bamb.app.SplashScreen
import cn.changjiahong.bamb.app.main.HomeScreenModel
import cn.changjiahong.bamb.bamb.uieffect.Toaster
import cn.changjiahong.bamb.bamb.uieffect.UiEffectDispatcher
import cn.changjiahong.bamb.bamb.uieffect.GoEffect
import cn.changjiahong.bamb.bamb.uieffect.NavigatorEffectRegister
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.KoinContext
import org.koin.compose.currentKoinScope
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.koin.ksp.generated.module

val GlobalNavigator: ProvidableCompositionLocal<Navigator> =
    staticCompositionLocalOf { error("GlobalNavigator not initialized") }


val appModules: Module
    get() = module {

    }

@OptIn(KoinExperimentalAPI::class, InternalVoyagerApi::class)
@Composable
@Preview
fun App() {
    Navigator(SplashScreen) { globalNavigator ->
        CompositionLocalProvider(GlobalNavigator providesDefault globalNavigator) {

            BackHandler(globalNavigator.lastItem is MainScreen) {
                getPlatform().moveTaskToBack(true)
            }

            NavigatorEffectRegister(globalNavigator)

            KoinContext {
                rememberKoinModules(modules = { listOf(AppKoin.module) })
                AppTheme {
                    CurrentScreen()
                }
            }
        }
    }
    Toaster()
}

@Composable
inline fun <reified T : ScreenModel> koinNavigatorScreenModel(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null
): T {
    return GlobalNavigator.current.koinNavigatorScreenModel<T>(qualifier, scope, parameters)
}