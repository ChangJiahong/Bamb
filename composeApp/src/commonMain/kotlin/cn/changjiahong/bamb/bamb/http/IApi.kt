package cn.changjiahong.bamb.bamb.http

import de.jensklingenberg.ktorfit.Ktorfit

abstract class IApi(val baseApi: String)

fun IApi.getKtorfit(): Ktorfit {
    return KtorfitManager.getKtorfit(baseApi)
}