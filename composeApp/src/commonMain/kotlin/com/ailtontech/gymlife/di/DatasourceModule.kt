package com.ailtontech.gymlife.di

import com.ailtontech.gymlife.data.datasources.FeatureFlagDatasource
import com.ailtontech.gymlife.data.datasources.impl.FeatureFlagDatasourceImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun datasourceModule(): Module =
    module {
        singleOf(::FeatureFlagDatasourceImpl) bind FeatureFlagDatasource::class
    }
