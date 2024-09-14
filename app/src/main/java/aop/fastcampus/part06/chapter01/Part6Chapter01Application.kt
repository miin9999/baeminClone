package aop.fastcampus.part06.chapter01

import android.app.Application
import android.content.Context
import aop.fastcampus.part06.chapter01.di.appModule
import org.koin.core.context.startKoin

class Part6Chapter01Application : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this

        startKoin{
            modules(appModule)
        }

    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }

    companion object {
        // 앱이 terminate 될 때를 대비에 nullable로
        var appContext: Context? = null
            private set

    }}