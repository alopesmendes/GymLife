package com.ailtontech.gymlife.data.datasources.impl

import com.ailtontech.gymlife.data.datasources.FeatureFlagDatasource
import com.ailtontech.gymlife.data.firebase.FirebaseManager
import com.ailtontech.gymlife.data.logger.Logger

class FeatureFlagDatasourceImpl(
    private val firebaseManager: FirebaseManager,
) : FeatureFlagDatasource {
    override suspend fun initialize() {
        firebaseManager.initialize()
        val success = firebaseManager.getRemoteConfig().fetchAndActivate()

        if (success) {
            Logger.logDebug("Remote config fetched and activated successfully")
        } else {
            Logger.logWarning("Remote config fetch failed or no updates available")
        }
    }

    override suspend fun getBoolean(
        key: String,
        defaultValue: Boolean,
    ): Boolean {
        Logger.logDebug("getBoolean: $key, $defaultValue")
        return firebaseManager.getRemoteConfig().getBoolean(key, defaultValue)
    }
}
