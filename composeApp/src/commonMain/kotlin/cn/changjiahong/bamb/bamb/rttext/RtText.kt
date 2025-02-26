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
import androidx.compose.ui.text.buildAnnotatedString
import cn.changjiahong.bamb.bamb.html.NodeHandlers
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun demo() {

    val rtTextState = rememberRtTextState()

    var num by remember { mutableIntStateOf(0) }

    Column {

        RtText(rtTextState)

        Button(onClick = {
            rtTextState.contentText = "Click $num"
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

    fun parse(content: String, type: ParserType = ParserType.Html): AnnotatedString {
        return when (type) {
            ParserType.Html -> htmlParse(content)
            ParserType.Markdown -> markdownParse(content)
        }
    }

    private fun htmlParse(content: String): AnnotatedString {
        val doc = Ksoup.parse(content)
        return AnnotatedStringBuilder(doc).build()
    }

    private fun markdownParse(content: String): AnnotatedString {
        return buildAnnotatedString { append(content) }
    }
}

class AnnotatedStringBuilder(
    val document: Document,
    val handlers: NodeHandlers = NodeHandlers.DefaultHandlers
) {

    private fun AnnotatedString.Builder.appendNode(node: Node) {
        when {
            node is TextNode -> {
                append(node.text())
            }

            else -> handlers(this, node)
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

interface Tag {
    object A : Tag
}

abstract class TagHandler(val tag: Tag)

class ParserConfig(

) {

}