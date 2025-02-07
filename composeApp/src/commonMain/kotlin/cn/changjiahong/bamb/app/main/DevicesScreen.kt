package cn.changjiahong.bamb.app.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import bamb.composeapp.generated.resources.Res
import bamb.composeapp.generated.resources.app_name

object DevicesScreen :TabHost(1u, Res.string.app_name, Icons.Default.Person) {
    @Composable
    override fun Content() = Devices()
}

@Composable
private fun DevicesScreen.Devices(){

}