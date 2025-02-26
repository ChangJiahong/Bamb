package cn.changjiahong.bamb.bamb.rttext

import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.fleeksoft.ksoup.nodes.Node
import kotlin.properties.ReadOnlyProperty

val Handlers.BTagHandler by tagHandler(Tag.B) { node: Node, appendChild ->
    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
        appendChild()
    }
}

val Handlers.H1TagHandler by tagHandler(Tag.H1) { node: Node, appendChild ->
    withStyle(ParagraphStyle(lineHeight = 24.sp)) {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
        ) {
            appendChild()
        }
    }
}

val Handlers.H2TagHandler by tagHandler(Tag.H2) { node: Node, appendChild ->
    withStyle(ParagraphStyle(lineHeight = 20.sp)) {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
            appendChild()
        }
    }
}


object Handlers {
    val DefaultHandlers: TagHandlers = TagHandlers()

    init {
        DefaultHandlers.addTagHandlers(
            BTagHandler,
            H1TagHandler,
            H2TagHandler
        )
    }
}

fun tagHandler(
    tag: Tag,
    nodeHandler: AnnotatedStringNodeHandler
): ReadOnlyProperty<Handlers, TagHandler> {
    return ReadOnlyProperty { _, _ -> TagHandler(tag, nodeHandler) }
}
