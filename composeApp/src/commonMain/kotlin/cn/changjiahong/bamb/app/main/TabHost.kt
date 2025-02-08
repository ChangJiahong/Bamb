package cn.changjiahong.bamb.app.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TabHost(
    index: UShort,
    title: StringResource,
    icon: ImageVector
): TabOptions {
    val title0 = stringResource(title)
    val icon0 = rememberVectorPainter(icon)
    return remember {
        TabOptions(
            index = index,
            title = title0,
            icon = icon0
        )
    }
}