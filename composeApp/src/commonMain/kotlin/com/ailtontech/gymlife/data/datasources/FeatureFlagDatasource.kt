package com.ailtontech.gymlife.data.datasources

interface FeatureFlagDatasource {
    suspend fun initialize()

    suspend fun getBoolean(
        key: String,
        defaultValue: Boolean = false,
    ): Boolean
}
