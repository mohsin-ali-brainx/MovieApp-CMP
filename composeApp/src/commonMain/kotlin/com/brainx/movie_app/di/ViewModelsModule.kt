package com.brainx.movie_app.di

import com.brainx.movie_app.presentation.screens.main_home.viewmodel.MainHomeScreenViewModel
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