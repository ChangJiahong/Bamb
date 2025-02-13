package cn.changjiahong.bamb

interface Platform {
    val name: String

    fun moveTaskToBack(boolean: Boolean)
}

expect fun getPlatform(): Platform