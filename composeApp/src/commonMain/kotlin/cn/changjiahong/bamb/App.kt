package cn.changjiahong.bamb

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cn.changjiahong.bamb.app.SplashScreen
import cn.changjiahong.bamb.bamb.DefaultEffectHandler
import com.dokar.sonner.Toaster
import com.dokar.sonner.rememberToasterState
import kotlinx.coroutines.delay
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
    var toaster = rememberToasterState()
    Navigator(SplashScreen) { globalNavigator ->
        CompositionLocalProvider(GlobalNavigator providesDefault globalNavigator) {
            val defaultEffectHandler = rememberSaveable { DefaultEffectHandler(globalNavigator,toaster) }
            KoinApplication(application = {
                modules(
                    module {
                        single { globalNavigator }
                        single { defaultEffectHandler }
                    },
                    AppKoin.module
                )
            }) {
                AppTheme {
                    CurrentScreen()
                }
            }
        }
    }
    Toaster(state = toaster)
}