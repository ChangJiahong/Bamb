package cn.changjiahong.bamb.bamb.file

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual object FileStorage {
    @OptIn(ExperimentalForeignApi::class)
    actual fun fileDirectory(): String {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory).path!!
    }

    actual fun getSharedResourceAssetsFilePath(): String {
        // 获取资源文件的路径
        val bundle = NSBundle.mainBundle
        val path = bundle.resourceURL?.path
        return "$path/compose-resources/assets/" // 返回路径
    }

}