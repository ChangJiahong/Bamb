package cn.changjiahong.bamb.bamb.rttext.rt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.fleeksoft.ksoup.nodes.Node

typealias AnnotatedStringBuilder = AnnotatedString.Builder

typealias NodeProcessor = @Composable AnnotatedStringBuilder.(node: Node) -> Unit

typealias InlineContent = Pair<String, InlineTextContent>

@Composable
inline fun AnnotatedStringBuilder.withStyle(
    style: SpanStyle,
    block: @Composable AnnotatedStringBuilder.() -> Unit
) {
    val index = pushStyle(style)
    block(this)
    pop(index)
}

@Composable
inline fun AnnotatedStringBuilder.withStyle(
    style: ParagraphStyle,
    crossinline block: @Composable AnnotatedStringBuilder.() -> Unit
) {
    val index = pushStyle(style)
    block(this)
    pop(index)
}


interface AnnotatedStringBuilderHandler {
    @Composable
    fun handler(builder: AnnotatedStringBuilder, node: Node)
}

open class NodeHandler(val name: String, val nodeProcessor: NodeProcessor) :
    AnnotatedStringBuilderHandler {

    @Composable
    override fun handler(builder: AnnotatedStringBuilder, node: Node) {
        builder.nodeProcessor(node)
    }

}

open class InlineNodeProcessor(
    name: String,
    val composeFun: @Composable (contentText: String) -> Unit
) : NodeHandler(name, {}) {

    @Composable
    override fun handler(builder: AnnotatedStringBuilder, node: Node) {


        val nodeHash = (node.hashCode()).toString()

        builder.apply {
            withStyle(ParagraphStyle()) {
                appendInlineContent(
                    nodeHash,
                    node.outerHtml().replace("\n","\\n")
                )
            }
        }

        onMeasure(nodeHash,
            node.outerHtml().replace("\n","\\n"))
    }

    @Composable
    private fun onMeasure(
        id: String,
        alternateText: String
    ) {
        val handlerContext = LocalHandlerContext.current
        if (handlerContext.hasInlineContent(id)){
            return
        }
        var show by remember { mutableStateOf(true) }
        if (show) {
            handlerContext.preRegisterInlineContent()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()  // 设置固定大小，也可以根据需求动态计算
                    .background(Color.Blue)
                    .onGloballyPositioned { coordinates ->
                        // 获取测量后的尺寸
                        val size = coordinates.size
                        show = false
                        println(size)

                        handlerContext.registerInlineContent(
                            id to InlineTextContent(
                                Placeholder(
                                    (size.width / 3).sp,
                                    (size.height / 3).sp,
                                    PlaceholderVerticalAlign.Center
                                ),
                                composeFun
                            )
                        )
                    }
            ) {
                composeFun(alternateText)
            }
        }
    }


}

@Composable
internal fun Float.pxToSp(): TextUnit {
    return (this / LocalDensity.current.fontScale).sp
}

@Composable
internal fun Int.pxToSp(): TextUnit {
    return (this / LocalDensity.current.fontScale).sp
}