package cn.changjiahong.bamb.bamb.rttext

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode


object AnnotatedStringParser {

    fun parse(
        content: String,
        type: ParserType = ParserType.Html,
        parserConfig: ParserConfig
    ): AnnotatedString {
        return when (type) {
            ParserType.Html -> htmlParse(content, parserConfig)
            ParserType.Markdown -> markdownParse(content)
        }
    }

    private fun htmlParse(
        content: String,
        parserConfig: ParserConfig
    ): AnnotatedString {
        val doc = Ksoup.parse(content)
        parserConfig.cssStyle?.let { doc.addCssStyle(it) }
        return AnnotatedStringBuilder(doc.body(), parserConfig.tagHandlers).build()
    }

    private fun markdownParse(content: String): AnnotatedString {
        return buildAnnotatedString { append(content) }
    }
}

class AnnotatedStringBuilder(
    val body: Node,
    val tagHandlers: TagHandlers
) {

    private fun AnnotatedString.Builder.appendNode(node: Node) {
        when {
            node is TextNode -> {
                append(node.text())
            }

            else -> tagHandlers(this, node) { appendChild(node) }
        }
    }

    private fun AnnotatedString.Builder.appendChild(node: Node) {
        node.childNodes().forEach {
            appendNode(it)
        }
    }

    fun build(): AnnotatedString {
        return buildAnnotatedString { appendNode(body) }
    }
}


sealed interface ParserType {
    data object Html : ParserType
    data object Markdown : ParserType
}


class ParserConfig(
    val tagHandlers: TagHandlers = Handlers.DefaultHandlers,
    var cssStyle: CssStyle? = null
)