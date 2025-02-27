package cn.changjiahong.bamb.bamb.rttext.rt

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode

val LocalHandlerContext: ProvidableCompositionLocal<HandlerContext> =
    staticCompositionLocalOf { error("HandlerContext not initialized") }


@Composable
fun RtHtml(node: Node) {
    val annotatedString = buildAnnotatedString(node)
    Text(annotatedString, inlineContent = HandlerContextSingleton.inlineTextContents)
}


@Composable
fun buildAnnotatedString(node: Node) = buildAnnotatedString {
    appendNode(node)
}


@Composable
fun AnnotatedString.Builder.appendNode(node: Node) {
    when {
        node is TextNode -> {
            append(node.text())
        }

        else -> HandlerContextSingleton.handler(this, node)
    }
}


@Composable
fun AnnotatedString.Builder.appendChild(node: Node) {
    node.childNodes().forEach {
        appendNode(it)
    }
}