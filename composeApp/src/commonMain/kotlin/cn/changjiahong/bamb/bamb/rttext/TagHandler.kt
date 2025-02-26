package cn.changjiahong.bamb.bamb.rttext

import androidx.compose.ui.text.AnnotatedString
import com.fleeksoft.ksoup.nodes.Node


abstract class Tag(val name: String) {
    object NULL : Tag("")

    object B : Tag("b")

    object H1 : Tag("h1")
    object H2 : Tag("h2")


}

typealias AnnotatedStringNodeHandler = AnnotatedString.Builder.(node: Node, appendChild: () -> Unit) -> Unit

open class TagHandler(
    val tag: Tag,
    val annotatedStringNodeHandler: AnnotatedStringNodeHandler = { _, _ -> }
) : (AnnotatedString.Builder, Node, () -> Unit) -> Unit {
    override fun invoke(
        builder: AnnotatedString.Builder,
        node: Node,
        appendChild: () -> Unit
    ) =
        builder.annotatedStringNodeHandler(node, appendChild)
}


class TagHandlers : TagHandler(Tag.NULL) {
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

    fun handler(
        builder: AnnotatedString.Builder,
        node: Node,
        appendChild: () -> Unit
    ): Boolean {
        if (tagHandlers.containsKey(node.nodeName())) {
            tagHandlers[node.nodeName()]?.let { it(builder, node, appendChild) }
            return true
        }
        return false
    }

    override fun invoke(
        builder: AnnotatedString.Builder,
        node: Node,
        appendChild: () -> Unit
    ) {
        if (!handler(builder, node, appendChild)) appendChild()
    }

}