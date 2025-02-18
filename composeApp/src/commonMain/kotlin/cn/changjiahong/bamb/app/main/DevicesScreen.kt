package cn.changjiahong.bamb.app.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bamb.composeapp.generated.resources.Res
import bamb.composeapp.generated.resources.app_name
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

object DevicesScreen : Tab {
    override val options: TabOptions
        @Composable get() = TabHost(1u, Res.string.app_name, Icons.Default.Person)

    @Composable
    override fun Content() = Devices()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun DevicesScreen.Devices() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()


    Column {
        Row {
            IconButton(onClick = {
                scope.launch {
                    sheetState.show()

                }
            }) { Icon(Icons.Default.MoreVert, "") }

        }
    }

    ModalBottomSheetPlus(sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    scope.launch { sheetState.hide() }
                }
            ) {
                Text("点我展开")
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetPlus(
    onDismissRequest: () -> Unit = {},
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    content: @Composable ColumnScope.() -> Unit,
) {
    var isFirstInitializer by remember { mutableStateOf(true) }
    var isShow by rememberSaveable(sheetState.currentValue, sheetState.targetValue) {
        mutableStateOf(sheetState.currentValue != SheetValue.Hidden || sheetState.targetValue != SheetValue.Hidden)
    }
    if (isShow || isFirstInitializer) {
        LaunchedEffect(Unit) {
            isFirstInitializer = false
        }
        ModalBottomSheet(
            onDismissRequest = {
                isShow = false
                onDismissRequest()
            },
            sheetState = sheetState,
            content = content
        )
    }
}