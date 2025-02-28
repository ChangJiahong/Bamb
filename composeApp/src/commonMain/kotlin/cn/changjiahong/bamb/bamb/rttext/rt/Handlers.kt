package cn.changjiahong.bamb.bamb.rttext.rt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.changjiahong.bamb.bamb.rttext.parseStyle
import com.fleeksoft.ksoup.nodes.Node

const val H1 = "h1"
const val H2 = "h2"
const val H3 = "h3"
const val H4 = "h4"
const val H5 = "h5"
const val H6 = "h6"
const val P = "p"
const val A = "a"
const val DIV = "div"
const val SPAN = "span"
const val IMG = "img"
const val UL = "ul"
const val OL = "ol"
const val LI = "li"
const val TABLE = "table"
const val TR = "tr"
const val TD = "td"
const val TH = "th"
const val THEAD = "thead"
const val TBODY = "tbody"
const val FORM = "form"
const val INPUT = "input"
const val BUTTON = "button"
const val LABEL = "label"
const val SELECT = "select"
const val OPTION = "option"
const val TEXTAREA = "textarea"
const val B = "b"
const val STRONG = "strong"
const val CODE = "code"

fun tags(vararg tags: String): String {
    return tags.joinToString(",")
}

object HandlerContextSingleton : HandlerContext() {
    init {
        registerNodeHandlers(
            defaultNodeHandler,
            h,
            p,
            strong,
            assemble,
            code
        )
    }
}

val defaultNodeHandler = NodeHandler("") { node ->
    appendChild(node)
}

val a = NodeHandler(A) { node ->

}

val h = NodeHandler(tags(H1, H2, H3, H4, H5, H6)) { node ->
    val (spanStyle, paragraphStyle) = node.parseStyle()
    withStyle(ParagraphStyle().merge(paragraphStyle)) {
        withStyle(SpanStyle().merge(spanStyle)) {
            appendChild(node)
        }
    }
}

val p = NodeHandler(P) { node ->
    withStyle(ParagraphStyle()) {
        withStyle(style = SpanStyle()) {
            appendChild(node)
        }
    }
}

val strong = NodeHandler(tags(B, STRONG)) { node: Node ->
    val (spanStyle, paragraphStyle) = node.parseStyle()
    withStyle(style = SpanStyle().merge(spanStyle)) {
        appendChild(node)
    }
}

val assemble = NodeHandler("em,kbd,ol,ul,hr,blockquote") { node ->
    val (spanStyle, paragraphStyle) = node.parseStyle()
    val paragraphIndex = paragraphStyle?.let {
        pushStyle(paragraphStyle)
    }

    val spanStyleIndex = spanStyle?.let {
        pushStyle(spanStyle)
    }

    appendChild(node)

    spanStyleIndex?.let {
        pop(spanStyleIndex)
    }

    paragraphIndex?.let {
        pop(paragraphIndex)
    }
}

val code = InlineNodeProcessor(CODE, Placeholder(1000.sp, 100.sp, PlaceholderVerticalAlign.Center)) {
    Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color.Yellow)){
        Text(it.replace("\\n","\n"), modifier = Modifier.background(Color.Cyan))
    }
}
