package com.brainx.ticket_tribe.di

import com.brainx.ticket_tribe.presentation.screens.main_home.viewmodel.MainHomeScreenViewModel
import com.brainx.utils_extensions.enums.CoroutineDispatcherModuleEnums
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<MainHomeScreenViewModel>{
        MainHomeScreenViewModel(
            get(named(CoroutineDispatcherModuleEnums.IO.dispatcherName)),
            get()
            )
    }
//    viewModel<VideoPlayerViewModel>{
//        VideoPlayerViewModel(
//            ioDispatcher =  get(named(CoroutineDispatcherModuleEnums.IO.dispatcherName)),
//            mainDispatcher = get(named(CoroutineDispatcherModuleEnums.MAIN.dispatcherName)),
//            getVideoDataUseCase =  get()
//        )
//    }
}