import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }
    
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.animation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(projects.shared)
            implementation(projects.server)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.org.jetbrains.compose.material3)
            implementation(libs.ktor.serialization.kotlinx.json.jvm)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization.jvm)
            implementation(libs.ktor.serialization.gson.jvm)
            implementation(libs.ktor.client.logging.jvm)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            api(libs.precompose)
        }
    }
}


compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.anafthdev.shafwah"
            packageVersion = "1.0.0"
        }
    }
}

compose.experimental {
    web.application {}
}