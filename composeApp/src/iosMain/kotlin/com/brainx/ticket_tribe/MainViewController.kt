package com.brainx.ticket_tribe

import androidx.compose.ui.window.ComposeUIViewController
import com.brainx.ticket_tribe.di.initKoin


fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }