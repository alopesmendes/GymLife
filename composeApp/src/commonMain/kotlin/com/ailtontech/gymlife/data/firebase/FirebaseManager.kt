package com.ailtontech.gymlife.data.firebase

expect class FirebaseManager {
    suspend fun initialize()

    fun getRemoteConfig(): FirebaseRemoteConfigManager
}
