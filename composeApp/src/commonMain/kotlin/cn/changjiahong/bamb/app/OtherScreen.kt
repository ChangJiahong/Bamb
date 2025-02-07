package cn.changjiahong.bamb.app

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class OtherScreen: Screen {
    @Composable
    override fun Content() {
        Text("OtherScreen")
    }
}