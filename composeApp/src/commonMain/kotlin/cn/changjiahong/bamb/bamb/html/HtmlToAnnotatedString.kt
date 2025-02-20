package cn.changjiahong.bamb.bamb.html

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode

object Html {
    fun fromHtml(html: String, vararg nodeHandlers: NodeHandler): AnnotatedString {
        val handlers = NodeHandlers.DefaultHandlers.apply {
            nodeHandlers.forEach {
                addNodeHandler(it)
            }
        }
        val doc = Ksoup.parse(html)
        return buildAnnotatedString {
            traverseHtml(doc, handlers)
        }
    }
}


fun Document.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString {
        traverseHtml(this@toAnnotatedString)
    }
}

private fun AnnotatedString.Builder.traverseHtml(
    node: Node,
    handlers: NodeHandlers = NodeHandlers.DefaultHandlers
) {
    when {
        node is TextNode -> {
            append(node.text())
        }
        else -> handlers(this, node)
    }
}


fun AnnotatedString.Builder.traverseChild(node: Node) {
    node.childNodes().forEach {
        traverseHtml(it)
    }
}


