import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.io.FileInputStream
import java.time.LocalDateTime
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kover)
    alias(libs.plugins.detekt)
    alias(libs.plugins.buildKonfig)
}

fun getVariable(name: String): String? {
    // 1. Check environment variable (for CI/CD)
    val envVar = System.getenv(name)
    if (!envVar.isNullOrBlank()) {
        return envVar
    }

    // 2. Check project property (gradle -P$name=$value)
    val projectProp = project.findProperty(name)?.toString()
    if (!projectProp.isNullOrBlank()) {
        return projectProp
    }

    // 3. Check local.properties (for local development)
    val localPropertiesFile = File(rootDir, "local.properties")
    if (localPropertiesFile.exists()) {
        try {
            val props = Properties()
            FileInputStream(localPropertiesFile).use { props.load(it) }
            val localProp = props.getProperty(name)
            if (!localProp.isNullOrBlank()) {
                return localProp
            }
        } catch (e: Exception) {
            println("Warning: Could not read local.properties: ${e.message}")
        }
    }

    return null
}

val environment = getVariable("APP_ENVIRONMENT") ?: "dev"

val appIdentifier
    get() = if (environment == "prod") {
        "com.ailtontech.gymlife"
    } else {
        "com.ailtontech.gymlife.${environment}"
    }

val appName
    get() = when (environment) {
        "prod" -> "GymLife"
        "dev" -> "GymLife Dev"
        "staging" -> "GymLife Staging"
        "test" -> "GymLife Test"
        else -> "GymLife $environment"
    }


kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            binaryOption("bundleId", appIdentifier)
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("composeApp")
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.constraintlayout)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(projects.shared)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "com.ailtontech.gymlife"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = appIdentifier
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"

        // Generate string resources with dynamic app name
        resValue("string", "app_name", appName)

        // Optional: Add environment as build config
        buildConfigField("String", "ENVIRONMENT", "\"${environment}\"")
        buildConfigField("String", "APP_NAME", "\"${appName}\"")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        getByName("debug") {
            // You can override app name specifically for debug builds if needed
            // resValue("string", "app_name", "GymLife Debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.ailtontech.gymlife.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = appName // Use app name for package name
            packageVersion = "1.0.0"

            // Optional: Set different descriptions per environment
            description = when (environment) {
                "prod" -> "GymLife - Your fitness companion"
                else -> "GymLife ${environment.uppercase()} - Development build"
            }
        }
    }
}

buildkonfig {
    packageName = appIdentifier

    defaultConfigs {
        // Make app name available to common code
        buildConfigField(STRING, "APP_NAME", appName)
        buildConfigField(STRING, "APP_ENVIRONMENT", environment)
        buildConfigField(STRING, "APP_IDENTIFIER", appIdentifier)
    }
}

// Task to generate iOS configuration file
abstract class GenerateIOSConfigTask : DefaultTask() {
    @get:Input
    abstract val generateEnvironment: Property<String>

    @get:Input
    abstract val generateAppName: Property<String>

    @get:Input
    abstract val generateAppIdentifier: Property<String>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun generate() {
        val file = outputFile.get().asFile
        file.parentFile.mkdirs()

        val configContent = """
            // Auto-generated iOS configuration - DO NOT EDIT MANUALLY
            // Generated on: ${LocalDateTime.now()}
            // Environment: ${generateEnvironment.get()}
            TEAM_ID=

            PRODUCT_NAME = ${generateAppName.get()}
            PRODUCT_BUNDLE_IDENTIFIER = ${generateAppIdentifier.get()}$(TEAM_ID)

            CURRENT_PROJECT_VERSION = 1
            MARKETING_VERSION = 1.0.0

            // Additional build settings can be added here
        """.trimIndent()

        file.writeText(configContent)
        println("Generated iOS config: ${file.absolutePath}")
        println("App name: ${generateAppName.get()}")
        println("Bundle ID: ${generateAppIdentifier.get()}")
    }
}

tasks.register<GenerateIOSConfigTask>("generateIOSConfig") {
    println("environment: $environment, appName: $appName, appIdentifier: $appIdentifier")
    this.generateEnvironment.set(environment)
    this.generateAppName.set(appName)
    generateAppIdentifier.set(appIdentifier)
    outputFile.set(File(project.rootDir, "iosApp/Configuration/DynamicConfig.xcconfig"))
}

// Task to clean generated iOS config
abstract class CleanIOSConfigTask : DefaultTask() {
    @get:OutputFile
    abstract val configFile: RegularFileProperty

    @TaskAction
    fun clean() {
        val file = configFile.get().asFile
        if (file.exists()) {
            file.delete()
            println("Deleted generated iOS config: ${file.absolutePath}")
        } else {
            println("iOS config file does not exist: ${file.absolutePath}")
        }
    }
}

// Task to clean generated iOS config
tasks.register<CleanIOSConfigTask>("cleanIOSConfig") {
    configFile.set(File(project.rootDir, "iosApp/Configuration/DynamicConfig.xcconfig"))
}

tasks.matching {
    (it.name.contains("ios", ignoreCase = true) || it.name.contains("Framework", ignoreCase = true)) &&
        !it.name.contains("generateIOSConfig") &&
        !it.name.contains("cleanIOSConfig")
}.configureEach {
    dependsOn("generateIOSConfig")
}

// Clean the generated config when cleaning
tasks.named("clean") {
    dependsOn("cleanIOSConfig")
}
