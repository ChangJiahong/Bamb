package cn.changjiahong.bamb

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cn.changjiahong.bamb.app.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

val GlobalNavigator: ProvidableCompositionLocal<Navigator> =
    staticCompositionLocalOf { error("GlobalNavigator not initialized") }


private val appModules: Module
    get() = module {

    }

@Composable
@Preview
fun App() {
    Navigator(SplashScreen) { globalNavigator ->
        CompositionLocalProvider(GlobalNavigator providesDefault globalNavigator) {
            KoinApplication(application = {
                modules(
                    module { single<Navigator> { globalNavigator } },
                    AppKoin.module
                )
            }) {
                AppTheme {
                    CurrentScreen()
                }
            }
        }
    }
}