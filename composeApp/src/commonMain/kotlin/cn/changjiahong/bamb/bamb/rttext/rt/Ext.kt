package cn.changjiahong.bamb.bamb.rttext.rt

import androidx.compose.runtime.Composable

@Composable
fun withIf(
    condition: Boolean,
    modifier: @Composable (@Composable () -> Unit) -> Unit = { it() },
    content: @Composable () -> Unit
) {
    if (condition) {
        modifier(content)
    } else {
        content()
    }
}
