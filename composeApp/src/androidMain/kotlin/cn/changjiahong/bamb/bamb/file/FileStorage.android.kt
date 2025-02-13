package cn.changjiahong.bamb.bamb.file

import cn.changjiahong.bamb.MyApp

object FileStorage {
    actual fun fileDirectory(): String {
        return MyApp.app.filesDir.absolutePath
    }

}