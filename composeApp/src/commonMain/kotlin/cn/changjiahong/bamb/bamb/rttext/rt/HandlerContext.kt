package cn.changjiahong.bamb.bamb.rttext.rt

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.fleeksoft.ksoup.nodes.Node
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

val LocalHandlerContext: ProvidableCompositionLocal<HandlerContext> =
    staticCompositionLocalOf { error("HandlerContext not initialized") }


open class HandlerContext : AnnotatedStringBuilderHandler {

    private val nodeHandlers = mutableMapOf<String, NodeHandler>()
    private val _inlineTextContents = mutableMapOf<String, InlineTextContent>()

    var inlineTextContents by mutableStateOf<Map<String, InlineTextContent>>(mapOf())
        private set

    private var inlineContentSize = 0


    fun registerNodeHandlers(vararg nodeHandler: NodeHandler) {
        registerNodeHandlers(nodeHandler.toList())
    }

    fun registerNodeHandlers(nodeHandlers: List<NodeHandler>) {
        nodeHandlers.forEach { handler -> registerNodeHandler(handler) }
    }

    fun registerNodeHandler(nodeHandler: NodeHandler) {
        val names = nodeHandler.name.split(",")
        names.forEach { name ->
            if (nodeHandlers.containsKey(name)) {
                return@forEach
            }
            nodeHandlers[name] = nodeHandler

        }
    }

    fun registerInlineContent(inlineContent: InlineContent) {
        if (_inlineTextContents.containsKey(inlineContent.first)) {
            return
        }
        _inlineTextContents[inlineContent.first] = inlineContent.second

        if (_inlineTextContents.size == inlineContentSize) {
            inlineTextContents = _inlineTextContents
        }
    }

    fun hasInlineContent(key: String): Boolean {
        return _inlineTextContents.containsKey(key)
    }

    @Composable
    override fun handler(builder: AnnotatedStringBuilder, node: Node) {
        val name = node.nodeName()
        if (nodeHandlers.containsKey(name)) {
            nodeHandlers[name]!!.handler(builder, node)
            return
        }
        defaultNodeHandler.handler(builder, node)
    }

    fun preRegisterInlineContent() {
        inlineContentSize++
    }

}
