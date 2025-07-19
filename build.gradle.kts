import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
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
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint") // Version should be inherited from parent

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

    afterEvaluate {
        tasks.findByName("check")?.dependsOn("ktlintCheck")
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
        println("   ./gradlew format       - Format all code")
        println("   ./gradlew check        - Check code style")
        println("   ./gradlew setupProject - Run this setup again")
    }
}

