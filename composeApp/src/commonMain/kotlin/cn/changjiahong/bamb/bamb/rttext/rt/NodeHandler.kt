package cn.changjiahong.bamb.bamb.rttext.rt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fleeksoft.ksoup.nodes.Node
import kotlin.random.Random
import kotlin.uuid.Uuid

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
    placeholder: Placeholder,
    val composeFun: @Composable (contentText: String) -> Unit
) :
    NodeHandler(name, { node ->
        withStyle(ParagraphStyle()) {
            appendInlineContent(name, node.outerHtml().replace("\n", "\\n"))
        }
    }) {

    val inlineContent = name to InlineTextContent(placeholder = placeholder, composeFun)

    @Composable
    override fun handler(builder: AnnotatedStringBuilder, node: Node) {
        val uu = (Random.Default.nextFloat() + 1).toString()
        println("--$uu --")

        builder.apply {
            withStyle(ParagraphStyle()) {
                appendInlineContent(uu,"$uu 哈哈哈哈哈哈哈哈傻还是傻是撒谎啥好飒好飒哈说哈杀手萨哈说")
            }
        }

        var s by remember { mutableStateOf(true) }
        if (s) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()  // 设置固定大小，也可以根据需求动态计算
                    .background(Color.Blue)
                    .onGloballyPositioned { coordinates ->
                        // 获取测量后的尺寸
                        val size = coordinates.size
//                        s=false
                        println(size)

                        HandlerContextSingleton.registerInlineContent(
                            uu to InlineTextContent(
                                Placeholder(
                                    (size.width / 10).sp,
                                    (size.height / 10).sp,
                                    PlaceholderVerticalAlign.Center
                                ),
                                composeFun
                            )
                        )
                    }
            ) {
                composeFun("$uu 哈哈哈哈哈哈哈哈傻还是傻是撒谎啥好飒好飒哈说哈杀手萨哈说")
            }
        }

//
    }

    fun onMe() {

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