package com.ailtontech.gymlife.di

import com.ailtontech.gymlife.data.firebase.FirebaseManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun firebaseModule(): Module =
    module {
        single { FirebaseManager() }
    }
