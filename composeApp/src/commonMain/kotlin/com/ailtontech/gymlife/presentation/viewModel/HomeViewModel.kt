package com.ailtontech.gymlife.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ailtontech.gymlife.data.datasources.FeatureFlagDatasource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val featureFlagDatasource: FeatureFlagDatasource,
) : ViewModel() {
    private val _isFeatureFlagEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFeatureFlagEnabled =
        _isFeatureFlagEnabled.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = false,
        )

    init {
        loadFeatureFlags()
    }

    fun loadFeatureFlags() {
        viewModelScope.launch {
            featureFlagDatasource.initialize()
            _isFeatureFlagEnabled.update {
                featureFlagDatasource.getBoolean("IS_FEATURE_FLAG_ENABLED", true)
            }
        }
    }
}
