package cn.changjiahong.bamb.bamb.rttext.rt

import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.sp


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

fun tags(vararg tags: String): String {
    return tags.joinToString(",")
}


object HandlerContextSingleton : HandlerContext() {
    init {
        registerNodeHandlers(defaultNodeHandler, h)
    }
}

val defaultNodeHandler = NodeHandler("") { node ->
    appendChild(node)
}

val a = NodeHandler(A) { node ->

}

val h = NodeHandler(tags(H1, H2, H3, H4, H5, H6)) { node ->

}



