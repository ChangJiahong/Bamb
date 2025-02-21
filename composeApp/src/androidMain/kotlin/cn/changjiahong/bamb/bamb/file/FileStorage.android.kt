package cn.changjiahong.bamb.bamb.file

import cn.changjiahong.bamb.MyApp

actual object FileStorage {
    actual fun fileDirectory(): String {
        return MyApp.app.filesDir.absolutePath
    }

    actual fun getSharedResourceAssetsFilePath(): String {
       return "/android_asset/"
    }

}