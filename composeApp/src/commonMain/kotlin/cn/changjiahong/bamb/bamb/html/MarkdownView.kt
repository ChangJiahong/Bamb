package cn.changjiahong.bamb.bamb.html

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewNavigator

private fun WebViewNavigator.setContent(markdownContent: String) {
    evaluateJavaScript("updateMarkdown(${markdownContent.jsStringValue()})")
}

@Composable
fun MarkdownView(markdownContent: String,modifier: Modifier = Modifier) {


    val webViewState = //rememberWebViewStateWithHTMLFile("markdownTemplate.html")
        rememberWebViewStateWithMarkdownData(markdownContent)

    val webViewNavigator = rememberWebViewNavigator()

    WebView(
        webViewState,
        modifier = modifier,
        navigator = webViewNavigator
    )

    LaunchedEffect(markdownContent) {
        webViewNavigator.setContent(markdownContent)
    }
}


fun String.jsStringValue(): String {
    return "`${this.replace("`", "\\`")}`"
}


val markdownContent = """
    # 硬件方面
    ## 先上渲染图
    ![Untitled2.JPG](http://localhost:8081/assets/images/faces/face1.jpg)
    #### 实物图
    ![63b1c6cc5698bf18db31a922b621630.jpg](http://localhost:8081/assets/images/logo.svg)
    ### 配置
    - 主控：esp32 micro32 plus
    - 主频：240Mhz
    - Flash：8M
    - PSRAM：2M

    ## 软件方面
    众所周知，LVGL是一个十分优秀的图形框架，小到几百kb的单片机，大到Linux都可以运行。既然它这么优秀，各种组件又十分的全面，没道理不用。
    ### 跟着官方例程适配esp32
    #### 显示驱动
    由于我的像素屏设计的是32\*16尺寸的，使用的是512个WS2812B灯珠，所以LVGL官方适配的屏幕驱动是没法使用的，所以首先需要自己实现WS2812B的驱动，这里采用的是FastLED。然后直接去适配LVGL的绘制方法就可以了。
    官方提供的lv_port_disp.cpp中有disp_flush函数，这个函数就是用来填充屏幕，只需要将它的每个像素绘制到屏幕中就可以了。

    ```c++
    static void disp_flush(lv_disp_drv_t * disp_drv, const lv_area_t * area, lv_color_t * color_p)
    {
        if(disp_flush_enabled) {
            /*将所有像素逐一放到屏幕上的最简单的情况（但也是最慢的情况）*/
            int32_t x;
            int32_t y;
            for(y = area-&gt;y1; y &lt;= area-&gt;y2; y++) {
                for(x = area-&gt;x1; x &lt;= area-&gt;x2; x++) {
                    /*Put a pixel to the display. For example:*/
                    /*put_px(x, y, *color_p)*/
                    // 设置像素点
                    pixels[matrixIndex[y * SCREEN_WIDTH + x]]=CRGB(color_p-&gt;ch.red,color_p-&gt;ch.green,color_p-&gt;ch.blue);
                    color_p++;
                }
            }
            // 刷新
            FastLED.show();
        }
        /*IMPORTANT!!!
        *Inform the graphics library that you are ready with the flushing 最后必须得调用,通知 lvgl 库你已经 flushing 拷贝完成了*/
        lv_disp_flush_ready(disp_drv);
    }
    ```

    用disp_drv.hor_res和disp_drv.ver_res设置屏幕的宽高
    ```c++
    /*Set the resolution of the display*/
    disp_drv.hor_res = MY_DISP_HOR_RES;
    disp_drv.ver_res = MY_DISP_VER_RES;
    ```
    设置好这些，就可以显示了。

    #### 输入驱动

    LVGL的输入控制方式有很多中可以选择，具体可以根据硬件灵活使用，由于我的硬件直设计了两个按钮，想要用两个按钮实现上下左右确认返回等控制是十分困难的，所以传统的按键映射控制是不行了，这里我采用的是模仿编码器控制，编码器只有上滚动、下滚动和确认；我将每个button按钮分成click和longClick两个事件，故两个按钮就可以产生四个事件，这样模仿编码器的三个事件足够了。
    - btn1 OnClick =&gt; left事件
    - btn2 OnClick =&gt; right事件
    - btn1 LongClick =&gt; enter事件
    - btn2 LongClick =&gt; esc事件

    在LVGL例程中需要实现**keypad_read** 和 **keypad_get_key**函数；这里使用OneButton库来扫描按钮事件
    由于OneButton只有按键事件回调，没有按键扫描函数，所以修改了一下OneButton的代码，使其可以返回按键状态。


    ```c++
    /*Get the currently being pressed key. 0 if no key is pressed 获取当前按下的键。如果未按键，则为0*/
    static uint32_t keypad_get_key(void)
    {
        /*Your code comes here*/
        // OneButton 中修改了tick()函数，使其可以返回按键状态，用来获取扫描按键状态
        stateType_t type1 = buttons[0]-&gt;tick();
        stateType_t type2 = buttons[1]-&gt;tick();

        if (type1!=BTN_TYPE_NONE){
                switch (type1) {
                    case BTN_TYPE_CLICK:
                        return 1;
                    case BTN_TYPE_LONG_PRESS_START:
                        return 3;
                }
            } else if (type2!=BTN_TYPE_NONE){
                switch (type2) {
                    case BTN_TYPE_CLICK:
                        return 2;
                    case BTN_TYPE_LONG_PRESS_START:
                        return 4;
            }
        }
        return 0;
    }

    /*Will be called by the library to read the keypad*/
    static void keypad_read(lv_indev_drv_t * indev_drv, lv_indev_data_t * data)
    {
        static uint32_t last_key = 0;

        /*Get the current x and y coordinates*/
        // mouse_get_xy(&amp;data-&gt;point.x, &amp;data-&gt;point.y);

        /*Get whether the a key is pressed and save the pressed key 获取是否按下了a键并保存按下的键 */
        uint32_t act_key = keypad_get_key();
        if(act_key != 0) {
            data-&gt;state = LV_INDEV_STATE_PR;

            /*Translate the keys to LVGL control characters according to your key definitions 根据您的密钥定义将密钥转换为LVGL控制字符*/
            switch(act_key) {
                case 1:
                    // 单击key1
                    act_key = LV_KEY_LEFT;
                    // Serial.println("LV_KEY_LEFT");
                    break;
                case 2:
                    // 单击key2
                    act_key = LV_KEY_RIGHT;
                    // Serial.println("LV_KEY_RIGHT");
                    break;
                case 3:
                    // 长按key1
                    act_key = LV_KEY_ENTER;
                    // Serial.println("LV_KEY_ENTER");
                    break;
                case 4:
                    // 长按key2
                    act_key = LV_KEY_ESC;
                    // Serial.println("LV_KEY_ESC");
                    break;
            }
            last_key = act_key;
        } else {
            data-&gt;state = LV_INDEV_STATE_REL;
        }

        data-&gt;key = last_key;
    }

    ```

    输入输出适配完成，就可以愉快的玩耍LVGL了，但是LVGL自带的字体是16px和8px的，并且还有亚像素渲染，导致字体显示虚化，有重叠。理论上16px和8px的字符都是可以显示的，但是由于有亚像素渲染，就会导致字体有重影，毕竟咱像素屏只有32\*16的大小，最终的显示效果就是黏在一起辨认不清，而这还只是显示英文字符，汉字就更困难了，通用字库的汉字最小像素大小就是16px，即如果想要显示完整的汉字细节，咱整个屏幕一次只能放下两个汉字，体验感太差了。经过不懈努力下，我找到了一款8px的字体，十分的优秀，能够在32\*16像素的屏幕能够足足显示8个汉字。并且模仿8段数码管，自定义了一套3\*5大小的数字。

    记录一下自定义字体的坑，LVGL自定义字体lv_font_fmt_txt_glyph_dsc_t类型的字段说明

    ```c++
    uint32_t bitmap_index : 20; /**&lt; 位图的起始索引。一个字体最多可以是1MB.*/
    uint32_t adv_w : 12; /**&lt;在此宽度之后绘制下一个图示符。8.4格式（存储real_value*16）.*/
    uint8_t box_w; /**&lt; 图示符的边界框的宽度*/
    uint8_t box_h; /**&lt; 图示符的边界框的高度*/
    int8_t ofs_x; /**&lt; 边界框的x偏移*/
    int8_t ofs_y; /**&lt; 边界框的y偏移。从线路顶部开始测量*/

    ```

    划重点,adv_w属性为字符宽度，它的大小是16的倍数，即不管你的字符高多少，如果你的字符宽1，那么adv_w=16 宽2 adv_w=32；如果将adv_w理解成字符编码的宽度，那么3\*5大小的字符adv_w=15，这样是不对的，它的adv_w应该是3\*16=48.

    ### 应用程序框架设计
    为了更愉快的玩转像素屏，方便以后的扩展功能，设计了一套App程序框架，该框架有App的启动、销毁等生命周期，任务栈功能，可以实现页面的切换，销毁等。


    另外由于LVGL不是线程安全的，所以在多任务更新界面时，LVGL会有冲突，这里我参照Android的消息机制写了一个轻量级的多任务消息通知框架，这部分比较复杂，放在下篇文章单独写一下。



""".trimIndent()
