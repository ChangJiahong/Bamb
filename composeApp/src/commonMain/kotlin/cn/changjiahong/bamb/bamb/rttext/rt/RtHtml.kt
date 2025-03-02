package cn.changjiahong.bamb.bamb.rttext.rt

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import cn.changjiahong.bamb.bamb.rttext.CssStyle
import cn.changjiahong.bamb.bamb.rttext.addCssStyle
import cn.changjiahong.bamb.bamb.rttext.markdownCss
import cn.changjiahong.bamb.bamb.rttext.mc
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode

@Composable
fun demo() {
    val html = """
        <div class="post-md">
        <!-- 标题 -->
            <h1>一级标题</h1>
            <h2>二级标题</h2>
            <h3>三级标题</h3>
            <h4>四级标题</h4>
            <h5>五级标题</h5>
            <h6>六级标题</h6>

            <!-- 文本格式 -->
            <p>这是一个普通段落。</p>
            <p><b>加粗文本</b></p>
            <p><i>斜体文本</i></p>
            <p><u>下划线文本</u></p>
            <p><s>删除线文本</s></p>
            <p><mark>高亮文本</mark></p>
            <p>上标：x<sup>2</sup>，下标：H<sub>2</sub>O</p>
            <p>引用文本：<q>这是一段引用</q></p>
            <blockquote>这是一个块级引用</blockquote>
            
            <!-- 超链接 -->
            <a href="https://example.com">点击访问 Example</a>

            <!-- 列表 -->
            <ul>
                <li>无序列表项 1</li>
                <li>无序列表项 2</li>
            </ul>
            <ol>
                <li>有序列表项 1</li>
                <li>有序列表项 2</li>
            </ol>
            
            <!-- 表格 -->
            <table border="1">
                <tbody><tr>
                    <th>表头 1</th>
                    <th>表头 2</th>
                </tr>
                <tr>
                    <td>数据 1</td>
                    <td>数据 2</td>
                </tr>
            </tbody></table>

            <!-- 图片 -->
            <img src="https://via.placeholder.com/150" alt="测试图片">

            <!-- 按钮 -->
            <button>点击按钮</button>

            <!-- 其他 HTML5 标签 -->
            <details>
                <summary>点击展开详情</summary>
                <p>这里是详细内容。</p>
            </details>
        </div>
    """.trimIndent()

    RtHtml(mc)
}

@Composable
fun RtHtml(html: String, css: String = markdownCss) {
    val document =
        remember(html, css) { Ksoup.parse(html).apply { addCssStyle(CssStyle.parseCss(css)) } }
    RenderNode(document.body())
}

@Composable
fun RenderNode(
    node: Node,
    modifier: Modifier = Modifier,
    nodeHandlers: List<NodeHandler> = emptyList()
) {

    CompositionLocalProvider(LocalHandlerContext provides rememberHandlerContext(nodeHandlers)) {

        val annotatedString = BuildAnnotatedString(node)

        println("annotatedString")
        RenderNode(annotatedString, modifier)
    }
}

@Composable
private fun RenderNode(
    annotatedString: AnnotatedString,
    modifier: Modifier
) {
    val handlerContext = LocalHandlerContext.current

    Text(
        annotatedString,
        modifier = modifier.verticalScroll(rememberScrollState()).background(Color.White),
        inlineContent = handlerContext.inlineTextContents
    )
}


@Composable
fun BuildAnnotatedString(node: Node) = buildAnnotatedString {
    appendNode(node)
}


@Composable
fun AnnotatedString.Builder.appendNode(node: Node) {
    val handlerContext = LocalHandlerContext.current
    when {
        node is TextNode -> {
            append(node.text())
        }

        else -> handlerContext.handler(this, node)
    }
}


@Composable
fun AnnotatedString.Builder.appendChild(node: Node) {
    node.childNodes().forEach {
        appendNode(it)
    }
}