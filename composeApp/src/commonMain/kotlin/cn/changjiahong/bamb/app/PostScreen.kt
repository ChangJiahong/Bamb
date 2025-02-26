package cn.changjiahong.bamb.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cn.changjiahong.bamb.GlobalNavigator
import cn.changjiahong.bamb.bean.Post
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronLeft
import compose.icons.feathericons.MoreHorizontal

class PostScreen(val post: Post) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        Scaffold(
            topBar = { AppActionBar() }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {

                Text(post.title, modifier = Modifier, style = MaterialTheme.typography.titleLarge)

                Row {
                    CoilImage(
                        imageModel = { post.avatar }, // loading a network image or local resource using an URL.
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(48.dp))
                            .size(48.dp)
                    )

                }


            }
        }

    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun AppActionBar() {
        TopAppBar(
            title = {},
            navigationIcon = {
                val globalNavigator = GlobalNavigator.current
                IconButton(
                    onClick = { if (globalNavigator.canPop) globalNavigator.pop() }
                ) {
                    Icon(
                        FeatherIcons.ChevronLeft,
                        ""
                    )
                }
            },
            actions = { IconButton(onClick = {}) { Icon(FeatherIcons.MoreHorizontal, "") } },
            expandedHeight = 50.dp,
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )
    }
}