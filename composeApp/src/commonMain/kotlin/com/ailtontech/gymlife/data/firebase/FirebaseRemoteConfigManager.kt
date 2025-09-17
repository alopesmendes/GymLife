package com.ailtontech.gymlife.data.firebase

expect class FirebaseRemoteConfigManager() {
    fun initialize()

    suspend fun fetchAndActivate(): Boolean

    fun getString(
        key: String,
        defaultValue: String = "",
    ): String

    fun getBoolean(
        key: String,
        defaultValue: Boolean = false,
    ): Boolean

    fun getLong(
        key: String,
        defaultValue: Long = 0L,
    ): Long

    fun getDouble(
        key: String,
        defaultValue: Double = 0.0,
    ): Double
}
