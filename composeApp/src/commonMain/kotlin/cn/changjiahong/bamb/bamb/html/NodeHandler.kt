package cn.changjiahong.bamb.bamb.html

import androidx.compose.ui.text.AnnotatedString
import cn.changjiahong.bamb.bamb.html.handler.BNode
import cn.changjiahong.bamb.bamb.html.handler.SpanNode
import com.fleeksoft.ksoup.nodes.Node


typealias NodeBuilder = AnnotatedString.Builder.(node: Node) -> Unit

abstract class NodeHandler(val name: String, val nodeBuilder: NodeBuilder) :
        (AnnotatedString.Builder, Node) -> Unit {
    override fun invoke(p1: AnnotatedString.Builder, node: Node): Unit = p1.nodeBuilder(node)
}

class NodeHandlers : NodeHandler("", {}) {
    private val nodeHandlers = mutableMapOf<String, NodeBuilder>()

    fun addNodeHandler(nodeHandler: NodeHandler) {
        addNodeBuilder(nodeHandler.name, nodeHandler.nodeBuilder)
    }

    fun addNodeBuilder(name: String, nodeHandler: NodeBuilder) {
        if (nodeHandlers.containsKey(name)) {
            return
        }
        nodeHandlers[name] = nodeHandler
    }

    private fun handler(builder: AnnotatedString.Builder, node: Node): Boolean {
        if (nodeHandlers.containsKey(node.nodeName())) {
            nodeHandlers[node.nodeName()]?.let { it(builder, node) }
            return true
        }
        return false
    }

    override fun invoke(p1: AnnotatedString.Builder, node: Node) {
        if (!handler(p1, node)) p1.traverseChild(node)
    }

    companion object {
        val DefaultHandlers: NodeHandlers = NodeHandlers()

        init {
            DefaultHandlers.addNodeHandler(BNode)
            DefaultHandlers.addNodeHandler(SpanNode)
        }
    }

}


