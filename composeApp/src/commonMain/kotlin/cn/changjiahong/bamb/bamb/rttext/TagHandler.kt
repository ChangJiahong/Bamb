package cn.changjiahong.bamb.bamb.rttext

import androidx.compose.ui.text.AnnotatedString
import com.fleeksoft.ksoup.nodes.Node


abstract class Tag(val name: String) {
    object NULL : Tag("")

    object Assemble : Tag("h1,h2,h3,h4,h5,h6,a,strong,em,kbd,ol,ul,hr,p,blockquote")

    object B : Tag("b")

    object H1 : Tag("h1")
    object H2 : Tag("h2")

    object P: Tag("p")

    object Code: Tag("code")


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

        val key = tagHandlers.keys.find { key -> key.split(",").contains(node.nodeName()) }
            ?: return false

        tagHandlers[key]?.let { it(builder, node, appendChild) }
        return true
    }

    override fun invoke(
        builder: AnnotatedString.Builder,
        node: Node,
        appendChild: () -> Unit
    ) {
        if (!handler(builder, node, appendChild)) appendChild()
    }

}