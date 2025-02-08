package cn.changjiahong.bamb.app.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import bamb.composeapp.generated.resources.Res
import bamb.composeapp.generated.resources.app_name
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object DevicesScreen : Tab {
    override val options: TabOptions
        @Composable get() = TabHost(1u, Res.string.app_name, Icons.Default.Person)

    @Composable
    override fun Content() = Devices()
}

@Composable
private fun DevicesScreen.Devices() {

}