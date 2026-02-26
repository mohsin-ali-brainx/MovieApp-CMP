package com.brainx.movie_app

import androidx.compose.ui.window.ComposeUIViewController
import com.brainx.movie_app.di.initKoin


fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }