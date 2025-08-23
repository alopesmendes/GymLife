import kotlinx.kover.gradle.plugin.dsl.GroupingEntityType
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.kover)
}

kover {
    reports {
        filters {
            // Exclude Android generated classes
            excludes.androidGeneratedClasses()

            // Exclude build directories
            excludes.packages("**.build.**")

            // Exclude entities and models packages
            excludes.packages("**.entities.**")
            excludes.packages("**.models.**")

            // Exclude Compose resources
            excludes.packages("**/composeResources/**")
            excludes.classes("**.*Res*") // Compose resources classes
            excludes.classes("**/Res\$*") // Compose resources nested classes

            // Exclude functions with @Preview annotation
            excludes.annotatedBy("androidx.compose.ui.tooling.preview.Preview")

            // Exclude common generated file patterns
            excludes.classes("*.*\$WhenMappings") // Kotlin when mappings
            excludes.classes("*.*\$Companion") // Companion objects (optional)
            excludes.classes("**.*Test*") // Test classes
            excludes.classes("**.*\$serializer") // Kotlinx.serialization generated
            excludes.classes("*BuildConfig*") // Build config
            excludes.classes("*Manifest*") // Android manifest
            excludes.classes("*.R") // Android resources
            excludes.classes("*.R\$*") // Android resources
        }

        verify {
            rule {
                bound {
                    minValue.set(80)
                }
            }
        }
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint") // Version should be inherited from parent
    apply(plugin = "org.jetbrains.kotlinx.kover")

    repositories {
        // Required to download KtLint
        mavenCentral()
    }

    // Optionally configure plugin
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        outputToConsole.set(true)
        ignoreFailures.set(false)
        enableExperimentalRules.set(true)
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.PLAIN_GROUP_BY_FILE)
            reporter(ReporterType.CHECKSTYLE)
            reporter(ReporterType.SARIF)
        }
    }

    // Project-specific coverage rules can still be configured in individual build.gradle.kts files
    afterEvaluate {
        tasks.findByName("check")?.dependsOn("ktlintCheck")
        // Add kover verification to check task
        tasks.findByName("check")?.dependsOn("koverVerify")
    }

    tasks.register("format") {
        group = "formatting"
        description = "Format Kotlin code with ktlint"
        dependsOn("ktlintFormat")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

val preCommitHook = tasks.register("preCommit", Copy::class) {
    group = "git hooks"
    description = "Installs the pre-commit Git hook script"
    from("$rootDir/scripts/pre-commit.sh")
    into("$rootDir/.git/hooks")
    rename("pre-commit.sh", "pre-commit")
    filePermissions {
        unix("rwxr-xr-x")
    }
}

tasks.register("setupProject") {
    group = "setup"
    description = "Sets up the project for development (installs git hooks, etc.)"
    dependsOn(preCommitHook)

    doLast {
        println("🚀 Project setup complete!")
        println("📋 Available commands:")
        println("   ./gradlew format                       - Format all code")
        println("   ./gradlew check                        - Check code style and coverage")
        println("   ./gradlew test -Pkover koverHtmlReport - Get the coverage from all projects")
        println("   ./gradlew setupProject                 - Run this setup again")
    }
}
