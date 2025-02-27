package cn.changjiahong.bamb.bamb.rttext.rt

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.sp
import com.fleeksoft.ksoup.nodes.Node

typealias AnnotatedStringBuilder = AnnotatedString.Builder

typealias NodeProcessor = @Composable AnnotatedStringBuilder.(node: Node) -> Unit

typealias InlineContent = Pair<String, InlineTextContent>

interface AnnotatedStringBuilderHandler {
    @Composable
    fun handler(builder: AnnotatedStringBuilder, node: Node)
}

open class NodeHandler(val name: String, val nodeProcessor: NodeProcessor) :
    AnnotatedStringBuilderHandler {

    @Composable
    override fun handler(builder: AnnotatedStringBuilder, node: Node) {
        builder.nodeProcessor(node)
    }

}

open class InlineNodeProcessor(
    name: String,
    placeholder: Placeholder,
    composeFun: @Composable (contentText: String) -> Unit
) :
    NodeHandler(name, { node ->
        appendInlineContent(name, node.outerHtml())
    }) {

    val inlineContent = name to InlineTextContent(placeholder = placeholder, composeFun)

}


