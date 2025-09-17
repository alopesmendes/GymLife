package com.ailtontech.gymlife.data.firebase

import com.ailtontech.gymlife.core.Constants
import com.ailtontech.gymlife.data.logger.Logger
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

actual class FirebaseRemoteConfigManager actual constructor() {
    private val remoteConfig = Firebase.remoteConfig

    actual suspend fun fetchAndActivate(): Boolean =
        runCatching {
            // Add timeout to prevent indefinite blocking
            val updated =
                withTimeout(Constants.FETCH_TIMEOUT) {
                    remoteConfig.fetchAndActivate().await()
                }
            // Log all current config values for debugging
            logAllConfigValues()

            updated
        }.getOrElse { exception ->
            Logger.logError("fetchAndActivate failed: ${exception.message}")
            Logger.logError("Exception type: ${exception::class.simpleName}")
            false
        }

    private fun logAllConfigValues() {
        Logger.logInfo("=== Current Remote Config Values ===")
        val info = remoteConfig.info
        Logger.logInfo("Last fetch status: ${info.lastFetchStatus}")
        Logger.logInfo("Last fetch time: ${info.fetchTimeMillis}")
        Logger.logInfo("Config settings interval: ${info.configSettings.minimumFetchIntervalInSeconds}")
    }

    actual fun getString(
        key: String,
        defaultValue: String,
    ): String =
        runCatching {
            remoteConfig.getString(key).takeIf({ it.isNotBlank() }) ?: defaultValue
        }.getOrDefault(defaultValue)

    actual fun getBoolean(
        key: String,
        defaultValue: Boolean,
    ): Boolean =
        runCatching {
            val configValue = remoteConfig.getValue(key)
            Logger.logDebug(
                """
            |feature flag key: $key,
            |value: ${configValue.asBoolean()},
            |not found: ${configValue.source == FirebaseRemoteConfig.VALUE_SOURCE_STATIC}
            |
                """.trimMargin(),
            )
            if (configValue.source == FirebaseRemoteConfig.VALUE_SOURCE_REMOTE) {
                remoteConfig.getBoolean(key)
            } else {
                defaultValue
            }
        }.getOrElse {
            Logger.logError(it.message ?: "")
            defaultValue
        }

    actual fun getLong(
        key: String,
        defaultValue: Long,
    ): Long =
        runCatching {
            remoteConfig.getLong(key)
        }.getOrDefault(defaultValue)

    actual fun getDouble(
        key: String,
        defaultValue: Double,
    ): Double =
        runCatching {
            remoteConfig.getDouble(key)
        }.getOrDefault(defaultValue)

    actual fun initialize() {
        val configSettings =
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = Constants.FETCH_INTERVAL_SECONDS
            }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }
}
