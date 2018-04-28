package xyz.rickygao.dishow.common

import android.app.Application

class RealApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        buildHawk()
    }
}