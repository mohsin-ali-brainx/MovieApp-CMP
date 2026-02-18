package com.brainx.local_datastore.di

import org.koin.core.module.Module

internal expect val datastorePlatformModule: Module

val datastoreModuleProvider = listOf(datastorePlatformModule)