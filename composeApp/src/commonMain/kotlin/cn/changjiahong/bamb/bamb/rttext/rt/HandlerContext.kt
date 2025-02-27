package cn.changjiahong.bamb.bamb.rttext.rt

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import com.fleeksoft.ksoup.nodes.Node


open class HandlerContext : AnnotatedStringBuilderHandler {

    private val nodeHandlers = mutableMapOf<String, NodeHandler>()
    private val _inlineTextContents = mutableMapOf<String, InlineTextContent>()
    val inlineTextContents: Map<String, InlineTextContent> = _inlineTextContents


    fun registerNodeHandlers(vararg nodeHandler: NodeHandler) {
        nodeHandler.forEach { handler -> registerNodeHandler(handler) }
    }

    fun registerNodeHandler(nodeHandler: NodeHandler) {
        val names = nodeHandler.name.split(",")
        names.forEach { name ->
            if (nodeHandlers.containsKey(name)) {
                return@forEach
            }
            nodeHandlers[name] = nodeHandler
            if (nodeHandler is InlineNodeProcessor) {
                registerInlineContent(nodeHandler.inlineContent)
            }
        }
    }

    fun registerInlineContent(inlineContent: InlineContent) {
        if (_inlineTextContents.containsKey(inlineContent.first)) {
            return
        }
        _inlineTextContents[inlineContent.first] = inlineContent.second
    }

    @Composable
    override fun handler(builder: AnnotatedStringBuilder, node: Node) {
        val name = node.nodeName()
        if (nodeHandlers.containsKey(name)) {
            nodeHandlers[name]!!.handler(builder, node)
        }
        defaultNodeHandler.handler(builder, node)
    }

}
