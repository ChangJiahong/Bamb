package cn.changjiahong.bamb.bamb.rttext

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import cn.changjiahong.bamb.bamb.html.NodeHandlers
import cn.changjiahong.bamb.bamb.html.handler.BNode
import cn.changjiahong.bamb.bamb.html.handler.SpanNode
import cn.changjiahong.bamb.bamb.html.traverseChild
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Composable
fun demo() {

    val rtTextState = rememberRtTextState()

    var num by remember { mutableIntStateOf(0) }

    Column {

        RtText(rtTextState)

        Button(onClick = {
            rtTextState.contentText = "<>"
        }) {
            Text("Click")
        }
    }
}

@Composable
fun RtText(rtTextState: RtTextState) {

    LaunchedEffect(rtTextState.contentText) {
        rtTextState.update()
    }

    Text(rtTextState.annotatedString)

}

@Composable
fun rememberRtTextState(
    parserType: ParserType = ParserType.Html,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): RtTextState {
    return remember(coroutineScope) {
        RtTextState(parserType, coroutineScope)
    }
}

//@Composable
//fun RtTextState(coroutineScope:CoroutineScope = rememberCoroutineScope()){
//
//}

class RtTextState(
    val parserType: ParserType = ParserType.Html,
    val coroutineScope: CoroutineScope
) {
    internal var contentText: String by mutableStateOf("")

    internal var annotatedString by mutableStateOf(buildAnnotatedString { })
        private set


    fun addTagHandler() {

    }

    fun setLinkAction() {

    }

    fun setCssStyle(cssStyle: String) {

    }

    fun update() {
        println("RtTextState update= $contentText")
        coroutineScope.launch {
            annotatedString = AnnotatedStringParser.parse(contentText, parserType)
        }
    }
}

object AnnotatedStringParser {

    fun parse(content: String, type: ParserType = ParserType.Html,tagHandlers: TagHandlers = TagHandlers.DefaultHandlers): AnnotatedString {
        return when (type) {
            ParserType.Html -> htmlParse(content,tagHandlers)
            ParserType.Markdown -> markdownParse(content)
        }
    }

    private fun htmlParse(content: String, tagHandlers: TagHandlers = TagHandlers.DefaultHandlers): AnnotatedString {
        val doc = Ksoup.parse(content)
        return AnnotatedStringBuilder(doc,tagHandlers).build()
    }

    private fun markdownParse(content: String): AnnotatedString {
        return buildAnnotatedString { append(content) }
    }
}

class AnnotatedStringBuilder(
    val document: Document,
    val tagHandlers: TagHandlers = TagHandlers.DefaultHandlers
) {

    private fun AnnotatedString.Builder.appendNode(node: Node) {
        when {
            node is TextNode -> {
                append(node.text())
            }

            else -> if (!tagHandlers(this, node)) appendChild(node)
        }
    }

    private fun AnnotatedString.Builder.appendChild(node: Node) {
        node.childNodes().forEach {
            appendNode(it)
        }
    }

    fun build(): AnnotatedString {
        return buildAnnotatedString { appendNode(document) }
    }
}


sealed interface ParserType {
    data object Html : ParserType
    data object Markdown : ParserType
}

abstract class Tag(val name: String) {
    object NULL : Tag("")

    object A : Tag("a")
}
private typealias AnnotatedStringNodeHandler = AnnotatedString.Builder.(node: Node) -> Unit

open class TagHandler(val tag: Tag, val annotatedStringNodeHandler: AnnotatedStringNodeHandler) :
        (AnnotatedString.Builder, Node) -> Boolean {
    override fun invoke(builder: AnnotatedString.Builder, node: Node): Boolean = run {
        builder.annotatedStringNodeHandler(node)
        true
    }
}


class TagHandlers : TagHandler(Tag.NULL, {}) {
    private val tagHandlers = mutableMapOf<String, AnnotatedStringNodeHandler>()

    fun addTagHandlers(vararg tagHandlers: TagHandler) {
        tagHandlers.forEach { handler ->
            addTagHandler(handler)
        }
    }

    fun addTagHandler(tagHandler: TagHandler) {
        addTagHandler(tagHandler.tag.name, tagHandler.annotatedStringNodeHandler)
    }

    fun addTagHandler(name: String, annotatedStringNodeHandler: AnnotatedStringNodeHandler) {
        if (tagHandlers.containsKey(name)) {
            return
        }
        tagHandlers[name] = annotatedStringNodeHandler
    }


    override fun invoke(builder: AnnotatedString.Builder, node: Node): Boolean {
        if (tagHandlers.containsKey(node.nodeName())) {
            tagHandlers[node.nodeName()]?.let { it(builder, node) }
            return true
        }
        return false
    }

    companion object {
        val DefaultHandlers: TagHandlers = TagHandlers()

        init {
            Handlers.apply {
                DefaultHandlers.addTagHandlers(
                    ATagHandler
                )
            }
        }
    }

}

object Handlers

val Handlers.ATagHandler by tagHandler(Tag.A) { node: Node ->
    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
        traverseChild(node)
    }
}

fun tagHandler(
    tag: Tag,
    nodeHandler: AnnotatedStringNodeHandler
): ReadOnlyProperty<Handlers, TagHandler> {
    return object : ReadOnlyProperty<Handlers, TagHandler> {
        override fun getValue(
            thisRef: Handlers,
            property: KProperty<*>
        ): TagHandler {
            return TagHandler(tag, nodeHandler)
        }
    }
}


class ParserConfig(

) {

}