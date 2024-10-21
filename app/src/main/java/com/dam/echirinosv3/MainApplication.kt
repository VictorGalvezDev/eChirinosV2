package com.dam.echirinosv3

import android.app.Application
import com.dam.echirinosv3.data.AppContainer
import com.dam.echirinosv3.data.DefaultAppContainer

class MainApplication : Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}