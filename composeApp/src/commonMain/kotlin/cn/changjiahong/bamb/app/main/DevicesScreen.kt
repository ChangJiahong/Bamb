package cn.changjiahong.bamb.app.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import com.alorma.compose.settings.ui.SettingsCheckbox
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsRadioButton
import com.alorma.compose.settings.ui.SettingsSwitch
import com.alorma.compose.settings.ui.SettingsTriStateCheckbox
import compose.icons.FeatherIcons
import compose.icons.feathericons.Feather
import compose.icons.feathericons.Server
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
        Row(verticalAlignment = Alignment.CenterVertically){
            IconButton(onClick = {
                scope.launch {
                    sheetState.show()
                }
            }) { Icon(FeatherIcons.Server, "") }

            Text("Device001")
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight, // 这里可以换成其他图标
                contentDescription = "",
            )

        }


        SettingsMenuLink(
            title = { Text(text = "Setting title") },
            subtitle = { Text(text = "Setting subtitle") },
            modifier = Modifier,
            enabled =  true,
            icon = { Icon(Icons.Default.Person,"") },
            action = { IconButton(onClick = {}){Icon(Icons.Default.AddCircle,"")} },
            onClick = {  },
        )
        SettingsGroup(
            modifier = Modifier,
            enabled = true,
            title = { Text(text = "SettingsGroup") },
            contentPadding = PaddingValues(16.dp),
        ) {
            SettingsCheckbox(
                state = true,
                title = { Text(text = "Setting title") },
                subtitle = { Text(text = "Setting subtitle") },
                modifier = Modifier,
                enabled = true,
                icon = { Icon(Icons.Default.Person, "") },
                onCheckedChange = { newState: Boolean -> },
            )

            SettingsTriStateCheckbox(
                state = null,
                title = { Text(text = "Setting title") },
                subtitle = { Text(text = "Setting subtitle") },
                modifier = Modifier,
                enabled = true,
                icon = { Icon(Icons.Default.Person, "") },
                onCheckedChange = { newState: Boolean -> },
            )
        }

        SettingsRadioButton(
            state = true,
            title = { Text(text = "Setting title") },
            subtitle = { Text(text = "Setting subtitle") },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.Person,"") },
            onClick = { },
        )

        SettingsSwitch(
            state =  true,
            title = { Text(text = "Setting title") },
            subtitle = { Text(text = "Setting subtitle") },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.Person,"") },
            onCheckedChange = { newState: Boolean -> },
        )

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
            modifier = modifier,
            sheetState = sheetState,
            sheetMaxWidth = sheetMaxWidth,
            shape = shape,
            containerColor = containerColor,
            contentColor = contentColor,
            tonalElevation = tonalElevation,
            scrimColor = scrimColor,
            dragHandle = dragHandle,
            contentWindowInsets = contentWindowInsets,
            properties = properties,
            content = content
        )
    }
}