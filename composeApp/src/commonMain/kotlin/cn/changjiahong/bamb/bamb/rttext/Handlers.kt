package cn.changjiahong.bamb.bamb.rttext

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import cn.changjiahong.bamb.bamb.html.traverseChild
import com.fleeksoft.ksoup.nodes.Node
import kotlin.properties.ReadOnlyProperty

val Handlers.AssembleHandler by tagHandler(Tag.Assemble) { node: Node, parserConfig: ParserConfig ->
    val (spanStyle, paragraphStyle) = node.parseStyle()

    val appendChild = { appendChild(node, parserConfig) }

    val block1 = if (spanStyle != null) {
        {
            withStyle(style = spanStyle) {
                appendChild()
            }
        }
    } else {
        { appendChild() }
    }

    val block2 = if (paragraphStyle != null) {
        {
            withStyle(paragraphStyle) {
                block1()
            }
        }
    } else {
        { block1() }
    }

    block2()
}

val Handlers.BTagHandler by tagHandler(Tag.B) { node: Node, parserConfig: ParserConfig ->
    val (spanStyle, paragraphStyle) = node.parseStyle()
    val appendChild = { appendChild(node, parserConfig) }

    withStyle(style = SpanStyle().merge(spanStyle)) {
        appendChild()
    }
}
val Handlers.HTagHandler by tagHandler(Tag.H) { node: Node, parserConfig: ParserConfig ->
    val appendChild = { appendChild(node, parserConfig) }

    val (spanStyle, paragraphStyle) = node.parseStyle()

    withStyle(ParagraphStyle().merge(paragraphStyle)) {
        withStyle(SpanStyle().merge(spanStyle)) {
            appendChild()
        }
    }
}

val Handlers.ATagHandler by tagHandler(Tag.A) { node: Node, parserConfig: ParserConfig ->

    val appendChild = { appendChild(node, parserConfig) }

    val (spanStyle, paragraphStyle) = node.parseStyle()

    withLink(
        LinkAnnotation.Clickable(
            "action",
            linkInteractionListener = { parserConfig.linkAction("Accc") })
    ) {
        appendChild()
    }


}

val Handlers.PTagHandler by tagHandler(Tag.P) { node: Node, parserConfig: ParserConfig ->
    val appendChild = { appendChild(node, parserConfig) }

    withStyle(ParagraphStyle()) {
        withStyle(style = SpanStyle()) {
            appendChild()
        }
    }
}


val Handlers.CodeTagHandler by tagHandler(Tag.Code) { node: Node, parserConfig: ParserConfig ->
    val appendChild = { appendChild(node, parserConfig) }

    withStyle(ParagraphStyle()) {
        withStyle(style = SpanStyle(background = Color.Yellow)) {
            appendChild()
        }
    }
}

object Handlers {
    val DefaultHandlers: TagHandlers = TagHandlers()

    init {
        DefaultHandlers.addTagHandlers(
            AssembleHandler,
            ATagHandler,
            HTagHandler,
            BTagHandler,
            PTagHandler,
            CodeTagHandler
        )
    }
}

fun tagHandler(
    tag: Tag,
    nodeHandler: AnnotatedStringNodeHandler
): ReadOnlyProperty<Handlers, TagHandler> {
    return ReadOnlyProperty { _, _ -> TagHandler(tag, nodeHandler) }
}
