package cn.changjiahong.bamb.app.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

abstract class TabHost(
    val index: UShort,
    val title: StringResource,
    val icon: ImageVector
) : Tab {
    override val options: TabOptions
        @Composable get() {
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
}