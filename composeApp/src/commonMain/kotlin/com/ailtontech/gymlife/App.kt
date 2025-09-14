package com.ailtontech.gymlife

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ailtontech.gymlife.dev.BuildKonfig
import com.ailtontech.gymlife.di.appModules
import com.ailtontech.gymlife.presentation.viewModel.HomeViewModel
import gymlife.composeapp.generated.resources.Res
import gymlife.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinMultiplatformApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun App() {
    KoinMultiplatformApplication(
        config =
            koinConfiguration {
                modules(appModules)
            },
    ) {
        val homeViewModel = koinViewModel<HomeViewModel>()
        val isEnabled by homeViewModel.isFeatureFlagEnabled.collectAsStateWithLifecycle()

        MaterialTheme {
            var showContent by remember { mutableStateOf(false) }
            Column(
                modifier =
                    Modifier
                        .safeContentPadding()
                        .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Is feature flag enabled: $isEnabled",
                    style = MaterialTheme.typography.titleLarge,
                )

                Button(onClick = { showContent = !showContent }) {
                    Text("Click me! ${BuildKonfig.APP_ENVIRONMENT}")
                }
                AnimatedVisibility(showContent) {
                    val greeting = remember { Greeting().greet() }
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painterResource(Res.drawable.compose_multiplatform), null)
                        Text("Compose: $greeting")
                    }
                }
            }
        }
    }
}
