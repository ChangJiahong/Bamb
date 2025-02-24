package cn.changjiahong.bamb.app

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class PostScreen: Screen {
    @Composable
    override fun Content() {
        Text("OtherScreen")

        val navigator = LocalNavigator.currentOrThrow

        Button(onClick = {
            navigator.pop()
        }){
            Text("Pop")
        }
    }
}