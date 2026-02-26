package com.brainx.movie_app

import android.app.Application
import com.brainx.movie_app.di.initKoin

import org.koin.android.ext.koin.androidContext

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MainApplication)
        }
    }
}