package cn.changjiahong.bamb.app

import cn.changjiahong.bamb.app.login.LoginScreen
import cn.changjiahong.bamb.app.login.RegisterScreen
import cn.changjiahong.bamb.bean.Post

object RR {
    val LOGIN = LoginScreen
    val REGISTER = RegisterScreen()
    val MAIN = MainScreen
    fun POST(post: Post) = PostScreen(post)
}