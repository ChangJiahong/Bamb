package cn.changjiahong.bamb

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform