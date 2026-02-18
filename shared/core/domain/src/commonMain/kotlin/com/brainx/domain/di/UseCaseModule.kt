package com.brainx.domain.di

import com.brainx.domain.use_cases.GetVideoUseCase
import com.brainx.domain.use_cases.SearchMultiUseCase
import org.koin.dsl.module

internal val useCaseModule = module {
    single {
        SearchMultiUseCase(
            get(),
        )
    }
    single {
        GetVideoUseCase(
            get(),
        )
    }
}