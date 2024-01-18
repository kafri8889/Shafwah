package com.anafthdev.shafwah.routing

import com.anafthdev.shafwah.common.checkId
import com.anafthdev.shafwah.common.checkParams
import com.anafthdev.shafwah.common.checkQueries
import com.anafthdev.shafwah.model.response.*
import com.anafthdev.shafwah.service.IceTeaService
import com.google.gson.Gson
import data.IceTeaVariant
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.IceTea

private suspend fun ApplicationCall.receiveIceTea(): IceTea? {
    return try {
        receive(IceTea::class)
    } catch (e: BadRequestException) {
        e.printStackTrace()
        respond(
            status = HttpStatusCode.BadRequest,
            message = ErrorResponse(
                message = "Cannot parse request body, check your request body!",
                status = HttpStatusCode.BadRequest.value,
                data = null
            )
        )
        null
    }
}

fun Routing.iceTeaRouting(iceTeaService: IceTeaService) {

    // Get all record
    get("/iceTea") {
        val iceTeaList = iceTeaService.getAll()
        val response = Gson().toJson(
            GetMultipleIceTeaResponse(
                message = "Get all record successfully",
                status = HttpStatusCode.OK.value,
                data = iceTeaList
            )
        )

        call.respondText(
            contentType = ContentType.Application.Json,
            status = HttpStatusCode.OK,
            provider = { response }
        )
    }

    // Get record by ID
    // route: /iceTea/123
    get("/iceTea/{id}") {
        val id = call.parameters["id"]?.toLong()

        checkId(id)

        val iceTea = iceTeaService.getById(id)
        val response = if (iceTea == null) {
            Gson().toJson(
                GetSingleIceTeaResponse(
                    message = "Not found",
                    status = HttpStatusCode.NotFound.value,
                    data = null
                )
            )
        } else {
            Gson().toJson(
                GetSingleIceTeaResponse(
                    message = "Success",
                    status = HttpStatusCode.OK.value,
                    data = iceTea
                )
            )
        }

        call.respondText(
            contentType = ContentType.Application.Json,
            status = if (iceTea == null) HttpStatusCode.NotFound else HttpStatusCode.OK,
            provider = { response }
        )
    }

    // Get record by date range
    get("/iceTea/date-range") {
        checkQueries("from", "to") {
            val from = call.request.queryParameters["from"]!!.toLong()
            val to = call.request.queryParameters["to"]!!.toLong()
            val iceTeaList = iceTeaService.getByDateRange(from, to)
            val response = Gson().toJson(
                GetMultipleIceTeaResponse(
                    message = "Get record by date range successfully",
                    status = HttpStatusCode.OK.value,
                    data = iceTeaList
                )
            )

            call.respondText(
                contentType = ContentType.Application.Json,
                status = HttpStatusCode.OK,
                provider = { response }
            )
        }
    }

    // Get record by price range
    get("/iceTea/price-range") {
        checkQueries("from", "to") {
            val from = call.request.queryParameters["from"]!!.toDouble()
            val to = call.request.queryParameters["to"]!!.toDouble()
            val iceTeaList = iceTeaService.getByPriceRange(from, to)
            val response = Gson().toJson(
                GetMultipleIceTeaResponse(
                    message = "Get record by price range successfully",
                    status = HttpStatusCode.OK.value,
                    data = iceTeaList
                )
            )

            call.respondText(
                contentType = ContentType.Application.Json,
                status = HttpStatusCode.OK,
                provider = { response }
            )
        }
    }

    // Get record by date and price range
    get("/iceTea/date-price-range") {
        checkQueries("dateFrom", "dateTo", "priceFrom", "priceTo") {
            val dateFrom = call.request.queryParameters["dateFrom"]!!.toLong()
            val dateTo = call.request.queryParameters["dateTo"]!!.toLong()
            val priceFrom = call.request.queryParameters["dateTo"]!!.toDouble()
            val priceTo = call.request.queryParameters["priceTo"]!!.toDouble()
            val iceTeaList = iceTeaService.getByPriceRangeAndDateRange(dateFrom, dateTo, priceFrom, priceTo)
            val response = Gson().toJson(
                GetMultipleIceTeaResponse(
                    message = "Get record by date and price range successfully",
                    status = HttpStatusCode.OK.value,
                    data = iceTeaList
                )
            )

            call.respondText(
                contentType = ContentType.Application.Json,
                status = HttpStatusCode.OK,
                provider = { response }
            )
        }
    }

    // Get record by variant
    get("/iceTea/variant/{variant}") {
        checkParams("variant") {
            val ordinal = call.parameters["variant"]!!.toInt()

            if (ordinal >= IceTeaVariant.entries.size) {
                call.respondText(
                    contentType = ContentType.Application.Json,
                    status = HttpStatusCode.NotFound,
                    provider = {
                        Gson().toJson(
                            ErrorResponse(
                                message = "Variant code not found. Available code from 0 until ${IceTeaVariant.entries.lastIndex}, but was $ordinal",
                                status = HttpStatusCode.NotFound.value,
                                data = null
                            )
                        )
                    }
                )

                return@checkParams
            }

            val variant = IceTeaVariant.entries[ordinal]
            val iceTeaList = iceTeaService.getByVariant(variant)
            val response = Gson().toJson(
                GetMultipleIceTeaResponse(
                    message = "Get record by variant ${variant.name} successfully",
                    status = HttpStatusCode.OK.value,
                    data = iceTeaList
                )
            )

            call.respondText(
                contentType = ContentType.Application.Json,
                status = HttpStatusCode.OK,
                provider = { response }
            )
        }
    }

    // Delete all record
    delete("/iceTea") {
        val rows = iceTeaService.deleteAll()
        val response = Gson().toJson(
            DeleteIceTeaResponse(
                message = "Delete all record successfully",
                status = HttpStatusCode.OK.value,
                data = rows
            )
        )

        call.respondText(
            contentType = ContentType.Application.Json,
            status = HttpStatusCode.Created,
            provider = { response }
        )
    }

    // Delete record by ID
    // route: /iceTea/123
    delete("/iceTea/{id}") {
        val id = call.parameters["id"]?.toLong()

        checkId(id)

        val rows = iceTeaService.deleteById(id)
        val response = Gson().toJson(
            DeleteIceTeaResponse(
                message = "Successfully deleted record with ID $id",
                status = HttpStatusCode.OK.value,
                data = rows
            )
        )

        call.respondText(
            contentType = ContentType.Application.Json,
            status = HttpStatusCode.OK,
            provider = { response }
        )
    }

    // Delete record by date range
    delete("/iceTea/date-range") {
        checkQueries("from", "to") {
            val from = call.request.queryParameters["from"]!!.toLong()
            val to = call.request.queryParameters["to"]!!.toLong()
            val rows = iceTeaService.deleteByDateRange(from, to)
            val response = Gson().toJson(
                DeleteIceTeaResponse(
                    message = "Delete record by date range successfully",
                    status = HttpStatusCode.OK.value,
                    data = rows
                )
            )

            call.respondText(
                contentType = ContentType.Application.Json,
                status = HttpStatusCode.OK,
                provider = { response }
            )
        }
    }

    // Update by id
    put("/iceTea") {
        val newIceTea = call.receiveIceTea() ?: return@put
        val updatedRows = iceTeaService.update(newIceTea)
        val response = Gson().toJson(
            UpdateIceTeaResponse(
                message = "Successfully updated record with ID ${newIceTea.id}",
                status = HttpStatusCode.OK.value,
                data = updatedRows
            )
        )

        call.respondText(
            contentType = ContentType.Application.Json,
            status = HttpStatusCode.OK,
            provider = { response }
        )
    }

    // Update record by date range
    put("/iceTea/date-range") {
        checkQueries("from", "to") {
            val newIceTea = call.receiveIceTea() ?: return@checkQueries
            val from = call.request.queryParameters["from"]!!.toLong()
            val to = call.request.queryParameters["to"]!!.toLong()
            val rows = iceTeaService.updateByDateRange(from, to, newIceTea)
            val response = Gson().toJson(
                DeleteIceTeaResponse(
                    message = "Update record by date range successfully",
                    status = HttpStatusCode.OK.value,
                    data = rows
                )
            )

            call.respondText(
                contentType = ContentType.Application.Json,
                status = HttpStatusCode.OK,
                provider = { response }
            )
        }
    }

    // Update record by price range
    put("/iceTea/price-range") {
        checkQueries("from", "to") {
            val newIceTea = call.receiveIceTea() ?: return@checkQueries
            val from = call.request.queryParameters["from"]!!.toDouble()
            val to = call.request.queryParameters["to"]!!.toDouble()
            val rows = iceTeaService.updateByPriceRange(from, to, newIceTea)
            val response = Gson().toJson(
                DeleteIceTeaResponse(
                    message = "Update record by price range successfully",
                    status = HttpStatusCode.OK.value,
                    data = rows
                )
            )

            call.respondText(
                contentType = ContentType.Application.Json,
                status = HttpStatusCode.OK,
                provider = { response }
            )
        }
    }

    // Insert record
    post("/iceTea") {
//        val iceTea = Gson().fromJson(call.receiveText(), IceTea::class.java)
        val iceTea = call.receiveIceTea() ?: return@post
        val id = iceTeaService.insert(iceTea)
        val response = Gson().toJson(
            InsertIceTeaResponse(
                message = "Insert success",
                status = HttpStatusCode.Created.value,
                data = id
            )
        )

        call.respondText(
            contentType = ContentType.Application.Json,
            status = HttpStatusCode.Created,
            provider = { response }
        )
    }
}
