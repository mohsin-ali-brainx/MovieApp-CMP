import org.gradle.api.GradleException

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "com.brainx.bootstrap_di"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "sharedCoreBootstrapDIKit"

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = xcfName
            isStatic = true
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                // Add KMP dependencies here
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.core)

                implementation(project(":shared:core:datasource"))

            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
                implementation(libs.koin.android)
                implementation(libs.koin.androidx.compose)
            }
        }

        nativeMain{
            dependencies {
                implementation(project(":shared:core:datasource"))
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.testExt.junit)
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }

}

val forbidInfraImportsInBootStrapDI = tasks.register("forbidInfraImportsInBootStrapDI") {
    group = "verification"
    description = "Fail build if composeApp imports infra/data modules directly"

    doLast {
        val forbidden = listOf(
            "import com.brainx.ktor_network.",
            "import com.brainx.room_database.",
            "import com.brainx.local_datastore."
        )

        val violations = fileTree("src") {
            include("**/*.kt")
        }.files.flatMap { file ->
            file.readLines().mapIndexedNotNull { index, line ->
                val trimmed = line.trim()
                if (forbidden.any { trimmed.startsWith(it) }) {
                    "${file.path}:${index + 1} -> $trimmed"
                } else {
                    null
                }
            }
        }

        if (violations.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("Forbidden imports found in BootStrapDI:")
                    violations.forEach { appendLine(" - $it") }
                }
            )
        }
    }
}

tasks.matching { it.name in setOf("check", "preBuild") }
    .configureEach { dependsOn(forbidInfraImportsInBootStrapDI) }