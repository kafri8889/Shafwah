package com.anafthdev.shafwah.plugins

import com.anafthdev.shafwah.routing.dimsumRouting
import com.anafthdev.shafwah.routing.iceTeaRouting
import com.anafthdev.shafwah.service.DimsumService
import com.anafthdev.shafwah.service.IceTeaService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:h2:file:E:\\DOCUMENTS_V2\\Multiplatform Project\\Shafwah\\server\\db\\product;",
        user = "",
        driver = "org.h2.Driver",
        password = ""
    )

    val iceTeaService = IceTeaService(database)
    val dimsumService = DimsumService(database)

    routing {
        iceTeaRouting(iceTeaService)
        dimsumRouting(dimsumService)
    }
}
