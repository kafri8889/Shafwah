plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
}

group = "com.anafthdev.shafwah"
version = "1.0.0"
application {
    mainClass.set("com.anafthdev.shafwah.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["development"] ?: "false"}")
}

dependencies {

    implementation(projects.shared)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.auth.jvm)
    implementation(libs.ktor.server.netty.jvm)
    implementation(libs.ktor.server.resources)
    implementation(libs.ktor.server.host.common.jvm)
    implementation(libs.ktor.server.status.pages.jvm)
    implementation(libs.ktor.server.double.receive.jvm)
    implementation(libs.ktor.server.auto.head.response.jvm)
    implementation(libs.ktor.server.call.logging.jvm)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.content.negotiation.jvm)
    implementation(libs.ktor.server.http.redirect.jvm)
    implementation(libs.ktor.server.default.headers.jvm)
    implementation(libs.ktor.server.caching.headers.jvm)
    implementation(libs.ktor.serialization.kotlinx.json.jvm)
    implementation(libs.ktor.serialization.gson.jvm)
    implementation(libs.org.jetbrains.exposed.dao)
    implementation(libs.org.jetbrains.exposed.core)
    implementation(libs.org.jetbrains.exposed.jdbc)
    implementation(libs.h2database)
    implementation(libs.logback)

}