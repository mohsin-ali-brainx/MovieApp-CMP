import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.api.GradleException
import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    val frameworkName = "ComposeApp"

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = frameworkName
            isStatic = true

        }
    }


    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.okhttp)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            implementation(project(":shared:core:domain"))
            implementation(project(":shared:core:bootstrapDI"))
            implementation(project(":shared:utilsExtensions"))


        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)

            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.core)

            implementation(libs.jetbrains.compose.navigation)

            implementation(libs.bundles.coil)


            implementation(project(":shared:core:bootstrapDI"))
            implementation(project(":shared:core:domain"))
            implementation(project(":shared:utilsExtensions"))

            implementation(libs.constraintlayout.compose.multiplatform)

            implementation(libs.kotlinx.serialization.json)


        }

        nativeMain.dependencies {
            implementation(project(":shared:core:bootstrapDI"))
            implementation(project(":shared:core:domain"))
            implementation(project(":shared:utilsExtensions"))

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.brainx.movie_app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.brainx.movie_app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {
            val apiKey = localProperties.getProperty("TMDB_API_KEY", "")
            val accessToken = localProperties.getProperty("TMDB_ACCESS_TOKEN", "")
            buildConfigField("String", "TMDB_API_KEY", "\"$apiKey\"")
            buildConfigField("String", "TMDB_ACCESS_TOKEN", "\"$accessToken\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("release") {
            val apiKey = localProperties.getProperty("TMDB_API_KEY", "")
            val accessToken = localProperties.getProperty("TMDB_ACCESS_TOKEN", "")
            buildConfigField("String", "TMDB_API_KEY", "\"$apiKey\"")
            buildConfigField("String", "TMDB_ACCESS_TOKEN", "\"$accessToken\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isMinifyEnabled = false
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.runtime.android)
    debugImplementation(libs.compose.uiTooling)
}