package com.brainx.ticket_tribe

import android.app.Application
import com.brainx.ticket_tribe.di.initKoin

import org.koin.android.ext.koin.androidContext

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MainApplication)
        }
    }
}