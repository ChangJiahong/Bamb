package cn.changjiahong.bamb

import android.app.Application
import android.content.Context
import org.koin.core.context.startKoin

/**
 *
 * @author ChangJiahong
 * @date 2025/2/9
 */
class MyApp : Application() {

    companion object {
        lateinit var app: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        startKoin {
            modules(appModules)
        }
    }

    override fun attachBaseContext(base: Context?) {
        app = this
        super.attachBaseContext(base)
    }
}