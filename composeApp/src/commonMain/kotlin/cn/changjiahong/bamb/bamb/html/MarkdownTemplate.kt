package cn.changjiahong.bamb.bamb.html

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import cn.changjiahong.bamb.bamb.file.FileStorage
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebStateSaver
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState


@Composable
fun rememberWebViewStateWithMarkdownData(
    markdownContent: String,
    encoding: String = "utf-8",
    mimeType: String? = null,
    historyUrl: String? = null,
): WebViewState =
    rememberSaveable(saver = WebStateSaver) {
        WebViewState(markdownData(markdownContent, encoding, mimeType, historyUrl))
    }.apply {
        this.content = markdownData(markdownContent, encoding, mimeType, historyUrl)
    }

fun WebViewNavigator.loadMarkdown(
    markdownContent: String,
    encoding: String = "utf-8",
    mimeType: String? = null,
    historyUrl: String? = null,
) {
    val html = markdownTemplate(markdownContent)
    val baseUrl = "file://${FileStorage.getSharedResourceAssetsFilePath()}"
    loadHtml(html,baseUrl, mimeType, encoding, historyUrl)
}

fun markdownData(
    markdownContent: String, encoding: String = "utf-8",
    mimeType: String? = null,
    historyUrl: String? = null,
) = run {
    val baseUrl = "file://${FileStorage.getSharedResourceAssetsFilePath()}"
    WebContent.Data(markdownTemplate(markdownContent), baseUrl, encoding, mimeType, historyUrl)
}

fun markdownTemplate(markdownContent: String) = """
    <!DOCTYPE html>
    <html lang="en" class="ss-shaded-scrollbars" style="transition: background-color 0.15s;">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, viewport-fit=cover"
              name="viewport">
        <meta name="description" content="">
        <meta name="hexo-theme-A4" content="v1.9.1">
        <title></title>
        <link rel="stylesheet" href="./md_files/style1.css">
        <link rel="stylesheet" href="./md_files/reset.css">
        <link rel="stylesheet" href="./md_files/markdown.css">
        <link rel="stylesheet" href="./md_files/fonts.css">
        <link rel="stylesheet" href="./md_files/style.css">

        <meta name="generator" content="Hexo 7.3.0">
        <style>
            .github-emoji {
              position: relative;
              display: inline-block;
              width: 1.2em;
              min-height: 1.2em;
              overflow: hidden;
              vertical-align: top;
              color: transparent;
            }

            .github-emoji>span {
              position: relative;
              z-index: 10;
            }

            .github-emoji img,
            .github-emoji .fancybox {
              margin: 0 !important;
              padding: 0 !important;
              border: none !important;
              outline: none !important;
              text-decoration: none !important;
              user-select: none !important;
              cursor: auto !important;
            }

            .github-emoji img {
              height: 1.2em !important;
              width: 1.2em !important;
              position: absolute !important;
              left: 50% !important;
              top: 50% !important;
              transform: translate(-50%, -50%) !important;
              user-select: none !important;
              cursor: auto !important;
            }

            .github-emoji-fallback {
              color: inherit;
            }

            .github-emoji-fallback img {
              opacity: 0 !important;
            }
        </style>

    </head>



    <body inmaintabuse="1">
    
    <div class="paper">

        <div class="paper-main">


            <div class="content post-main">

                <div class="post-md">
                    <div id="md-viewer"></div>
                    <textarea type="hidden" id="mdContent" style="display: none;">${markdownContent}</textarea>
                </div>


            </div>


            <block>
                <div class="f">

                    <script src="./md_files/jquery.min.js"></script>
                    <!--目录-->
                    <script src="./md_files/jquery(1).min.js" type="text/javascript"></script>
                    <script src="./md_files/jquery-ui.min.js" type="text/javascript"></script>
                    <script src="./md_files/jquery.tocify.min.js" type="text/javascript"></script>

                </div>
            </block>
        </div>


    </div>
    <link rel="stylesheet" href="./md_files/index.css">
    <link rel="stylesheet" href="./md_files/github.css">
    <!--<link rel="stylesheet" href="/assets/md/github-markdown.css"/>-->
    <script>
        let process = {
          env: {
            NODE_ENV: "production"
          }
        }

    </script>
    <script src="./md_files/bytemd-1.21.0.umd.js.js"></script>
    <script src="./md_files/plugin-gfm-1.21.0.umd.js.js"></script>
    <script src="./md_files/index.umd.js"></script>
    <script>
        const plugins = [bytemdPluginGfm(), bytemdPluginHighlight()]

        const mdContent = ${'$'}("#mdContent")

        const editor = new bytemd.Viewer({
          target: document.getElementById('md-viewer'), // DOM to render
          props: {
            value: mdContent.val(),
            plugins,
          },
        })
        editor.${'$'}on('change', (e) => {
          // mdContent.val(e.detail.value)
          console.log(e.detail.value);
          // console.log(mdContent.val());
          editor.${'$'}set({ value: e.detail.value })
        })
    </script>

    </body>

    </html>
    
    
""".trimIndent()