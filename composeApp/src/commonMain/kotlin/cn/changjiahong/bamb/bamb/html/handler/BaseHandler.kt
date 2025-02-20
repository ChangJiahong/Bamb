package cn.changjiahong.bamb.bamb.html.handler

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import cn.changjiahong.bamb.bamb.html.NodeHandler
import cn.changjiahong.bamb.bamb.html.traverseChild
import com.fleeksoft.ksoup.nodes.Node


object BNode : NodeHandler("b", { node: Node ->
    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
        traverseChild(node)
    }

})


fun hexToInt(hex: String): Long {
    // 去掉 '#'
    val cleanedHex = hex.trimStart('#')
    // 将十六进制字符串转换为 Int
    return cleanedHex.toLong(16)
}

object SpanNode : NodeHandler("span", { node: Node ->
    val color =
        if (node.hasAttr("color")) Color(hexToInt(node.attr("color"))) else Color.Unspecified
    withStyle(style = SpanStyle(color = color)) {
        traverseChild(node)
    }
})


class ClickNode(onClick: (action: String) -> Unit) : NodeHandler("click", { node: Node ->
    val action = node.attr("action")
    withLink(LinkAnnotation.Clickable(action, linkInteractionListener = { onClick(action) })) {
        traverseChild(node)
    }
})