package com.brainx.ticket_tribe.di

import com.brainx.utils_extensions.enums.CoroutineDispatcherModuleEnums
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coroutineDispatchersModule= module {
    single<CoroutineDispatcher>(named(CoroutineDispatcherModuleEnums.DEFAULT.dispatcherName)) { Dispatchers.Default }
    single<CoroutineDispatcher>(named(CoroutineDispatcherModuleEnums.IO.dispatcherName)) { Dispatchers.IO }
    single<CoroutineDispatcher>(named(CoroutineDispatcherModuleEnums.MAIN.dispatcherName)) { Dispatchers.Main }
    single<CoroutineDispatcher>(named(CoroutineDispatcherModuleEnums.MAIN_IMMEDIATE.dispatcherName)) { Dispatchers.Main.immediate}
}