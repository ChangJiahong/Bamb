package cn.changjiahong.bamb.bamb.rttext

import androidx.compose.ui.text.AnnotatedString
import com.fleeksoft.ksoup.nodes.Node


abstract class Tag(val name: String) {
    object NULL : Tag("")

    object Assemble : Tag("em,kbd,ol,ul,hr,blockquote")
    object H : Tag("h1,h2,h3,h4,h5,h6")

    object B : Tag("b,strong")

    object A : Tag("a")

    object P : Tag("p")

    object Code : Tag("code")


}

typealias AnnotatedStringNodeHandler = AnnotatedString.Builder.(node: Node, parserConfig: ParserConfig) -> Unit


open class TagHandler(
    val tag: Tag,
    val annotatedStringNodeHandler: AnnotatedStringNodeHandler = { _, _ -> }
) : (AnnotatedString.Builder, Node, ParserConfig) -> Unit {
    override fun invoke(
        builder: AnnotatedString.Builder,
        node: Node,
        parserConfig: ParserConfig
    ) =
        builder.annotatedStringNodeHandler(node, parserConfig)
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
        parserConfig: ParserConfig
    ): Boolean {
        if (tagHandlers.containsKey(node.nodeName())) {
            tagHandlers[node.nodeName()]?.let { it(builder, node, parserConfig) }
            return true
        }

        val key = tagHandlers.keys.find { key -> key.split(",").contains(node.nodeName()) }
            ?: return false

        tagHandlers[key]?.let { it(builder, node, parserConfig) }
        return true
    }

    override fun invoke(
        builder: AnnotatedString.Builder,
        node: Node,
        parserConfig: ParserConfig
    ) {
        if (!handler(builder, node, parserConfig)) builder.appendChild(node,parserConfig)
    }

}