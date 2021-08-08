package com.mimo

import android.app.Application
import com.mimo.di.dataSourceModule
import com.mimo.di.networkModule
import com.mimo.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BaseApplication)
            modules(listOf(viewModelModule, dataSourceModule, networkModule))
        }
    }
}