package com.anafthdev.shafwah

import SERVER_PORT
import com.anafthdev.shafwah.plugins.configureDatabases
import com.anafthdev.shafwah.plugins.configureMonitoring
import com.anafthdev.shafwah.plugins.configureRouting
import com.anafthdev.shafwah.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureMonitoring()
    configureSerialization()
    configureDatabases()
    configureRouting()
}
