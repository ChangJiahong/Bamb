package cn.changjiahong.bamb.bamb.file

expect object FileStorage {

    fun fileDirectory(): String
    fun getSharedResourceAssetsFilePath(): String
}