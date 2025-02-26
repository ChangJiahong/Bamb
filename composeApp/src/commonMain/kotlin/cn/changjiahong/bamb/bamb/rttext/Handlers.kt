package cn.changjiahong.bamb.bamb.rttext

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.fleeksoft.ksoup.nodes.Node
import kotlin.properties.ReadOnlyProperty

val Handlers.AssembleHandler by tagHandler(Tag.Assemble) { node: Node, appendChild ->
    val (spanStyle, paragraphStyle) = node.parseStyle()

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

val Handlers.BTagHandler by tagHandler(Tag.B) { node: Node, appendChild ->
    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
        appendChild()
    }
}

val Handlers.H1TagHandler by tagHandler(Tag.H1) { node: Node, appendChild ->

    val (spanStyle, paragraphStyle) = node.parseStyle()

//    withStyle(paragraphStyle) {
//        withStyle(style = spanStyle) {
//            appendChild()
//        }
//    }
}

val Handlers.H2TagHandler by tagHandler(Tag.H2) { node: Node, appendChild ->
//    withStyle(ParagraphStyle()) {
//        withStyle(style = SpanStyle()) {
//            appendChild()
//        }
//    }
}

val Handlers.PTagHandler by tagHandler(Tag.P) { node: Node, appendChild ->
    withStyle(ParagraphStyle()) {
        withStyle(style = SpanStyle()) {
            appendChild()
        }
    }
}


val Handlers.CodeTagHandler by tagHandler(Tag.Code) { node: Node, appendChild ->
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
