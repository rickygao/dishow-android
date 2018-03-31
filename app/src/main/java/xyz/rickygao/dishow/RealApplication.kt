package xyz.rickygao.dishow

import android.app.Application

class RealApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        buildHawk()
    }
}