package cn.changjiahong.bamb.bamb.rttext

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.properties.ReadOnlyProperty

val mc = """
    <div class="content post-main">

            <div class="post-md">
                <div id="md-viewer">
                    <div class="markdown-body"><h1>硬件方面</h1>
                        <h2>先上渲染图</h2>
                        <p><img src="http://localhost:8081/assets/images/faces/face1.jpg" alt="Untitled2.JPG"></p>
                        <h4>实物图</h4>
                        <p><img src="http://localhost:8081/assets/images/logo.svg" alt="63b1c6cc5698bf18db31a922b621630.jpg"></p>
                        <h3>配置</h3>
                        <ul>
                            <li>主控：esp32 micro32 plus</li>
                            <li>主频：240Mhz</li>
                            <li>Flash：8M</li>
                            <li>PSRAM：2M</li>
                        </ul>
                        <h2>软件方面</h2>
                        <p>
                            众所周知，LVGL是一个十分优秀的图形框架，小到几百kb的单片机，大到Linux都可以运行。既然它这么优秀，各种组件又十分的全面，没道理不用。</p>
                        <h3>跟着官方例程适配esp32</h3>
                        <h4>显示驱动</h4>
                        <p>
                            由于我的像素屏设计的是32*16尺寸的，使用的是512个WS2812B灯珠，所以LVGL官方适配的屏幕驱动是没法使用的，所以首先需要自己实现WS2812B的驱动，这里采用的是FastLED。然后直接去适配LVGL的绘制方法就可以了。
                            官方提供的lv_port_disp.cpp中有disp_flush函数，这个函数就是用来填充屏幕，只需要将它的每个像素绘制到屏幕中就可以了。</p>
                        <pre><code class="language-c++ hljs language-c"><span class="hljs-type">static</span> <span class="hljs-type">void</span> <span class="hljs-title function_">disp_flush</span><span class="hljs-params">(<span class="hljs-type">lv_disp_drv_t</span> * disp_drv, <span class="hljs-type">const</span> <span class="hljs-type">lv_area_t</span> * area, <span class="hljs-type">lv_color_t</span> * color_p)</span>
    {
      <span class="hljs-keyword">if</span>(disp_flush_enabled) {
          <span class="hljs-comment">/*将所有像素逐一放到屏幕上的最简单的情况（但也是最慢的情况）*/</span>
          <span class="hljs-type">int32_t</span> x;
          <span class="hljs-type">int32_t</span> y;
          <span class="hljs-keyword">for</span>(y = area-&gt;y1; y &lt;= area-&gt;y2; y++) {
              <span class="hljs-keyword">for</span>(x = area-&gt;x1; x &lt;= area-&gt;x2; x++) {
                  <span class="hljs-comment">/*Put a pixel to the display. For example:*/</span>
                  <span class="hljs-comment">/*put_px(x, y, *color_p)*/</span>
                  <span class="hljs-comment">// 设置像素点</span>
                  pixels[matrixIndex[y * SCREEN_WIDTH + x]]=CRGB(color_p-&gt;ch.red,color_p-&gt;ch.green,color_p-&gt;ch.blue);
                  color_p++;
              }
          }
          <span class="hljs-comment">// 刷新</span>
          FastLED.show();
      }
      <span class="hljs-comment">/*IMPORTANT!!!
      *Inform the graphics library that you are ready with the flushing 最后必须得调用,通知 lvgl 库你已经 flushing 拷贝完成了*/</span>
      lv_disp_flush_ready(disp_drv);
    }
    </code></pre>
                        <p>用disp_drv.hor_res和disp_drv.ver_res设置屏幕的宽高</p>
                        <pre><code class="language-c++ hljs language-c"><span class="hljs-comment">/*Set the resolution of the display*/</span>
    disp_drv.hor_res = MY_DISP_HOR_RES;
    disp_drv.ver_res = MY_DISP_VER_RES;
    </code></pre>
                        <p>设置好这些，就可以显示了。</p>
                        <h4>输入驱动</h4>
                        <p>
                            LVGL的输入控制方式有很多中可以选择，具体可以根据硬件灵活使用，由于我的硬件直设计了两个按钮，想要用两个按钮实现上下左右确认返回等控制是十分困难的，所以传统的按键映射控制是不行了，这里我采用的是模仿编码器控制，编码器只有上滚动、下滚动和确认；我将每个button按钮分成click和longClick两个事件，故两个按钮就可以产生四个事件，这样模仿编码器的三个事件足够了。</p>
                        <ul>
                            <li>btn1 OnClick =&gt; left事件</li>
                            <li>btn2 OnClick =&gt; right事件</li>
                            <li>btn1 LongClick =&gt; enter事件</li>
                            <li>btn2 LongClick =&gt; esc事件</li>
                        </ul>
                        <p>在LVGL例程中需要实现<strong>keypad_read</strong> 和 <strong>keypad_get_key</strong>函数；这里使用OneButton库来扫描按钮事件
                            由于OneButton只有按键事件回调，没有按键扫描函数，所以修改了一下OneButton的代码，使其可以返回按键状态。
                        </p>
                        <pre><code class="language-c++ hljs language-c"><span class="hljs-comment">/*Get the currently being pressed key. 0 if no key is pressed 获取当前按下的键。如果未按键，则为0*/</span>
    <span class="hljs-type">static</span> <span class="hljs-type">uint32_t</span> <span class="hljs-title function_">keypad_get_key</span><span class="hljs-params">(<span class="hljs-type">void</span>)</span>
    {
      <span class="hljs-comment">/*Your code comes here*/</span>
      <span class="hljs-comment">// OneButton 中修改了tick()函数，使其可以返回按键状态，用来获取扫描按键状态</span>
      stateType_t type1 = buttons[<span class="hljs-number">0</span>]-&gt;tick();
      stateType_t type2 = buttons[<span class="hljs-number">1</span>]-&gt;tick();

      <span class="hljs-keyword">if</span> (type1!=BTN_TYPE_NONE){
              <span class="hljs-keyword">switch</span> (type1) {
                  <span class="hljs-keyword">case</span> BTN_TYPE_CLICK:
                      <span class="hljs-keyword">return</span> <span class="hljs-number">1</span>;
                  <span class="hljs-keyword">case</span> BTN_TYPE_LONG_PRESS_START:
                      <span class="hljs-keyword">return</span> <span class="hljs-number">3</span>;
              }
          } <span class="hljs-keyword">else</span> <span class="hljs-keyword">if</span> (type2!=BTN_TYPE_NONE){
              <span class="hljs-keyword">switch</span> (type2) {
                  <span class="hljs-keyword">case</span> BTN_TYPE_CLICK:
                      <span class="hljs-keyword">return</span> <span class="hljs-number">2</span>;
                  <span class="hljs-keyword">case</span> BTN_TYPE_LONG_PRESS_START:
                      <span class="hljs-keyword">return</span> <span class="hljs-number">4</span>;
          }
      }
      <span class="hljs-keyword">return</span> <span class="hljs-number">0</span>;
    }

    <span class="hljs-comment">/*Will be called by the library to read the keypad*/</span>
    <span class="hljs-type">static</span> <span class="hljs-type">void</span> <span class="hljs-title function_">keypad_read</span><span class="hljs-params">(<span class="hljs-type">lv_indev_drv_t</span> * indev_drv, <span class="hljs-type">lv_indev_data_t</span> * data)</span>
    {
      <span class="hljs-type">static</span> <span class="hljs-type">uint32_t</span> last_key = <span class="hljs-number">0</span>;

      <span class="hljs-comment">/*Get the current x and y coordinates*/</span>
      <span class="hljs-comment">// mouse_get_xy(&amp;data-&gt;point.x, &amp;data-&gt;point.y);</span>

      <span class="hljs-comment">/*Get whether the a key is pressed and save the pressed key 获取是否按下了a键并保存按下的键 */</span>
      <span class="hljs-type">uint32_t</span> act_key = keypad_get_key();
      <span class="hljs-keyword">if</span>(act_key != <span class="hljs-number">0</span>) {
          data-&gt;state = LV_INDEV_STATE_PR;

          <span class="hljs-comment">/*Translate the keys to LVGL control characters according to your key definitions 根据您的密钥定义将密钥转换为LVGL控制字符*/</span>
          <span class="hljs-keyword">switch</span>(act_key) {
              <span class="hljs-keyword">case</span> <span class="hljs-number">1</span>:
                  <span class="hljs-comment">// 单击key1</span>
                  act_key = LV_KEY_LEFT;
                  <span class="hljs-comment">// Serial.println("LV_KEY_LEFT");</span>
                  <span class="hljs-keyword">break</span>;
              <span class="hljs-keyword">case</span> <span class="hljs-number">2</span>:
                  <span class="hljs-comment">// 单击key2</span>
                  act_key = LV_KEY_RIGHT;
                  <span class="hljs-comment">// Serial.println("LV_KEY_RIGHT");</span>
                  <span class="hljs-keyword">break</span>;
              <span class="hljs-keyword">case</span> <span class="hljs-number">3</span>:
                  <span class="hljs-comment">// 长按key1</span>
                  act_key = LV_KEY_ENTER;
                  <span class="hljs-comment">// Serial.println("LV_KEY_ENTER");</span>
                  <span class="hljs-keyword">break</span>;
              <span class="hljs-keyword">case</span> <span class="hljs-number">4</span>:
                  <span class="hljs-comment">// 长按key2</span>
                  act_key = LV_KEY_ESC;
                  <span class="hljs-comment">// Serial.println("LV_KEY_ESC");</span>
                  <span class="hljs-keyword">break</span>;
          }
          last_key = act_key;
      } <span class="hljs-keyword">else</span> {
          data-&gt;state = LV_INDEV_STATE_REL;
      }

      data-&gt;key = last_key;
    }

    </code></pre>
                        <p>
                            输入输出适配完成，就可以愉快的玩耍LVGL了，但是LVGL自带的字体是16px和8px的，并且还有亚像素渲染，导致字体显示虚化，有重叠。理论上16px和8px的字符都是可以显示的，但是由于有亚像素渲染，就会导致字体有重影，毕竟咱像素屏只有32*16的大小，最终的显示效果就是黏在一起辨认不清，而这还只是显示英文字符，汉字就更困难了，通用字库的汉字最小像素大小就是16px，即如果想要显示完整的汉字细节，咱整个屏幕一次只能放下两个汉字，体验感太差了。经过不懈努力下，我找到了一款8px的字体，十分的优秀，能够在32*16像素的屏幕能够足足显示8个汉字。并且模仿8段数码管，自定义了一套3*5大小的数字。</p>
                        <p>
                            记录一下自定义字体的坑，LVGL自定义字体lv_font_fmt_txt_glyph_dsc_t类型的字段说明</p>
                        <pre><code class="language-c++ hljs language-c"><span class="hljs-type">uint32_t</span> bitmap_index : <span class="hljs-number">20</span>; <span class="hljs-comment">/**&lt; 位图的起始索引。一个字体最多可以是1MB.*/</span>
    <span class="hljs-type">uint32_t</span> adv_w : <span class="hljs-number">12</span>; <span class="hljs-comment">/**&lt;在此宽度之后绘制下一个图示符。8.4格式（存储real_value*16）.*/</span>
    <span class="hljs-type">uint8_t</span> box_w; <span class="hljs-comment">/**&lt; 图示符的边界框的宽度*/</span>
    <span class="hljs-type">uint8_t</span> box_h; <span class="hljs-comment">/**&lt; 图示符的边界框的高度*/</span>
    <span class="hljs-type">int8_t</span> ofs_x; <span class="hljs-comment">/**&lt; 边界框的x偏移*/</span>
    <span class="hljs-type">int8_t</span> ofs_y; <span class="hljs-comment">/**&lt; 边界框的y偏移。从线路顶部开始测量*/</span>

    </code></pre>
                        <p>划重点,adv_w属性为字符宽度，它的大小是16的倍数，即不管你的字符高多少，如果你的字符宽1，那么adv_w=16
                            宽2
                            adv_w=32；如果将adv_w理解成字符编码的宽度，那么3*5大小的字符adv_w=15，这样是不对的，它的adv_w应该是3*16=48.</p>
                        <h3>应用程序框架设计</h3>
                        <p>
                            为了更愉快的玩转像素屏，方便以后的扩展功能，设计了一套App程序框架，该框架有App的启动、销毁等生命周期，任务栈功能，可以实现页面的切换，销毁等。</p>
                        <p>
                            另外由于LVGL不是线程安全的，所以在多任务更新界面时，LVGL会有冲突，这里我参照Android的消息机制写了一个轻量级的多任务消息通知框架，这部分比较复杂，放在下篇文章单独写一下。</p>
                        <!--1--></div>

                </div>
            </div>
        </div>
        
""".trimIndent()
@Composable
fun demo() {

    val rtTextState = rememberRtTextState()

    var num by remember { mutableIntStateOf(0) }

    Column {

        Button(onClick = {
            rtTextState.setCssStyle(markdownCss)
            rtTextState.contentText = mc
        }) {
            Text("Click")
        }
        RtText(rtTextState)

    }
}

@Composable
fun RtText(rtTextState: RtTextState) {

    LaunchedEffect(rtTextState.contentText) {
        rtTextState.update()
    }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Text(rtTextState.annotatedString, modifier = Modifier.fillMaxSize())
    }


}

@Composable
fun rememberRtTextState(
    parserType: ParserType = ParserType.Html,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): RtTextState {
    return remember(coroutineScope) {
        RtTextState(parserType, coroutineScope)
    }
}


class RtTextState(
    val parserType: ParserType = ParserType.Html,
    val coroutineScope: CoroutineScope
) {
    internal var contentText: String by mutableStateOf("")

    internal var annotatedString by mutableStateOf(buildAnnotatedString { })
        private set

    private val parserConfig = ParserConfig()

    fun addTagHandler(tagHandler: TagHandler) {
        parserConfig.tagHandlers.addTagHandler(tagHandler)
    }

    fun setLinkAction() {

    }

    fun setCssStyle(cssStyle: String) {
        parserConfig.cssStyle = CssStyle.parseCss(cssStyle)
    }

    fun update() {
        coroutineScope.launch {
            annotatedString = AnnotatedStringParser.parse(contentText, parserType, parserConfig)
        }
    }
}
