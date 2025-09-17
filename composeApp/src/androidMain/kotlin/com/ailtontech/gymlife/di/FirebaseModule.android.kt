package com.ailtontech.gymlife.di

import com.ailtontech.gymlife.data.firebase.FirebaseManager
import com.ailtontech.gymlife.data.firebase.FirebaseRemoteConfigManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun firebaseModule(): Module =
    module {
        single { FirebaseManager(androidContext().applicationContext, get()) }

        singleOf(::FirebaseRemoteConfigManager)
    }
