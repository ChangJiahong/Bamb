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
        return annotatedStringBuild(doc.body(), parserConfig)
    }

    private fun markdownParse(content: String): AnnotatedString {
        return buildAnnotatedString { append(content) }
    }
}

fun annotatedStringBuild(
    body: Node,
    parserConfig: ParserConfig,
): AnnotatedString {
    return buildAnnotatedString { appendNode(body, parserConfig) }
}

fun AnnotatedString.Builder.appendNode(node: Node, parserConfig: ParserConfig) {
    when {
        node is TextNode -> {
            append(node.text())
        }

        else -> parserConfig.tagHandlers(this, node, parserConfig)
    }
}

fun AnnotatedString.Builder.appendChild(node: Node, parserConfig: ParserConfig) {
    node.childNodes().forEach {
        appendNode(it, parserConfig)
    }
}


sealed interface ParserType {
    data object Html : ParserType
    data object Markdown : ParserType
}


class ParserConfig(
    var linkAction: LinkAction = {},
    val tagHandlers: TagHandlers = Handlers.DefaultHandlers,
    var cssStyle: CssStyle? = null,
)