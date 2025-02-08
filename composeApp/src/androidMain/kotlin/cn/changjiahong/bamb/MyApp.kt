package cn.changjiahong.bamb

import android.app.Application
import org.koin.core.context.startKoin

/**
 *
 * @author ChangJiahong
 * @date 2025/2/9
 */
class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModules)
        }
    }
}