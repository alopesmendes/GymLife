package com.ailtontech.gymlife.di

import com.ailtontech.gymlife.presentation.viewModel.HomeViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun viewModelModule(): Module =
    module {
        viewModelOf(::HomeViewModel)
    }
