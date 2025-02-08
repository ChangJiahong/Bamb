package cn.changjiahong.bamb.app.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bamb.composeapp.generated.resources.Res
import bamb.composeapp.generated.resources.app_name
import bamb.composeapp.generated.resources.compose_multiplatform
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cn.changjiahong.bamb.Greeting
import org.jetbrains.compose.resources.painterResource

object HomeScreen : Tab {
    override val options: TabOptions
        @Composable get() = TabHost(0u, Res.string.app_name, Icons.Default.Home)

    @Composable
    override fun Content() = Home()
}

@Composable
private fun HomeScreen.Home() {
    val homeScreenModel = koinScreenModel<HomeScreenModel>()
    var showContent by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { showContent = !showContent }) {
            Text("Click me!")
        }
        AnimatedVisibility(showContent) {
            val greeting = remember { Greeting().greet() }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painterResource(Res.drawable.compose_multiplatform), null, modifier = Modifier.clickable(onClick = {
                    homeScreenModel.cli()
                }))
                Text("Compose: $greeting")
            }
        }
    }
}