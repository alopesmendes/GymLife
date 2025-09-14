package com.ailtontech.gymlife.data.firebase

import android.content.Context
import com.google.firebase.FirebaseApp

actual class FirebaseManager(
    private val context: Context,
    private val firebaseRemoteConfigManager: FirebaseRemoteConfigManager,
) {
    actual suspend fun initialize() {
        FirebaseApp.initializeApp(context)
        firebaseRemoteConfigManager.initialize()
    }

    actual fun getRemoteConfig(): FirebaseRemoteConfigManager = firebaseRemoteConfigManager
}
