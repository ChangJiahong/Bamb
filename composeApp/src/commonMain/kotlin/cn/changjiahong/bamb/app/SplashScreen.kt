package cn.changjiahong.bamb.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bamb.composeapp.generated.resources.Res
import bamb.composeapp.generated.resources.compose_multiplatform
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cn.changjiahong.bamb.GlobalNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import org.jetbrains.compose.resources.painterResource

object SplashScreen : Screen {
    @Composable
    override fun Content() = Splash()
}

@Composable
fun SplashScreen.Splash() {
    val splashScreenModel = koinScreenModel<SplashScreenModel>()

    Box(contentAlignment = Alignment.Center) {
        val navigator = GlobalNavigator.current
        var count by remember { mutableIntStateOf(1) }
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painterResource(Res.drawable.compose_multiplatform), null)
            Text(count.toString())
        }

        CountDowner(count, loop = { count = it }) {
            splashScreenModel.tryAutoLogin()
        }


    }
}

@Composable
fun CountDowner(
    count: Int,
    interval: Long = 1000,
    loop: (time: Int) -> Unit = {},
    end: () -> Unit
) {
    LaunchedEffect(Unit) {
        flow {
            (count downTo 0).forEach {
                delay(interval)
                emit(it)
            }
        }.onCompletion {
            end()
        }.collect {
            loop(it)
        }
    }
}