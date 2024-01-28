package com.anafthdev.shafwah.routing

import com.anafthdev.shafwah.common.checkId
import com.anafthdev.shafwah.common.checkParams
import com.anafthdev.shafwah.common.checkQueries
import com.anafthdev.shafwah.service.DimsumService
import com.google.gson.Gson
import data.model.Dimsum
import data.model.response.ErrorResponse
import data.model.response.dimsum.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private suspend fun ApplicationCall.receiveDimsum(): Dimsum? {
    return try {
        receive(Dimsum::class)
    } catch (e: BadRequestException) {
        e.printStackTrace()
        null
    }
}

private suspend fun ApplicationCall.receiveDimsumArray(): Array<Dimsum>? {
    return try {
        println(receiveText())
        Gson().fromJson(receiveText(), Array<Dimsum>::class.java)
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

fun Routing.dimsumRouting(dimsumService: DimsumService) {

    // Get all record
    get("/dimsum") {
        val dimsumList = dimsumService.getAll()
        val response = Gson().toJson(
            GetMultipleDimsumResponse(
                message = "Get all record successfully",
                status = HttpStatusCode.OK.value,
                data = dimsumList
            )
        )

        call.respondText(
            contentType = ContentType.Application.Json,
            status = HttpStatusCode.OK,
            provider = { response }
        )
    }

    // Get record by ID
    // route: /dimsum/123
    get("/dimsum/{id}") {
        val id = call.parameters["id"]?.toLong()

        checkId(id)

        val dimsum = dimsumService.getById(id)
        val response = if (dimsum == null) {
            Gson().toJson(
                GetSingleDimsumResponse(
                    message = "Not found",
                    status = HttpStatusCode.NotFound.value,
                    data = null
                )
            )
        } else {
            Gson().toJson(
                GetSingleDimsumResponse(
                    message = "Success",
                    status = HttpStatusCode.OK.value,
                    data = dimsum
                )
            )
        }

        call.respondText(
            contentType = ContentType.Application.Json,
            status = if (dimsum == null) HttpStatusCode.NotFound else HttpStatusCode.OK,
            provider = { response }
        )
    }

    // Get record by amount
    // route: /dimsum/amount/10
    get("/dimsum/amount/{amount}") {
        checkParams("amount") {
            val amount = call.parameters["amount"]!!.toInt()
            val dimsum = dimsumService.getByAmount(amount)
            val response = Gson().toJson(
                GetMultipleDimsumResponse(
                    message = "Success",
                    status = HttpStatusCode.OK.value,
                    data = dimsum
                )
            )

            call.respondText(
                contentType = ContentType.Application.Json,
                status = HttpStatusCode.OK,
                provider = { response }
            )
        }
    }

    // Get record by date range
    get("/dimsum/date-range") {
        checkQueries("from", "to") {
            val from = call.request.queryParameters["from"]!!.toLong()
            val to = call.request.queryParameters["to"]!!.toLong()
            val dimsumList = dimsumService.getByDateRange(from, to)
            val response = Gson().toJson(
                GetMultipleDimsumResponse(
                    message = "Get record by date range successfully",
                    status = HttpStatusCode.OK.value,
                    data = dimsumList
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
    get("/dimsum/price-range") {
        checkQueries("from", "to") {
            val from = call.request.queryParameters["from"]!!.toDouble()
            val to = call.request.queryParameters["to"]!!.toDouble()
            val dimsumList = dimsumService.getByPriceRange(from, to)
            val response = Gson().toJson(
                GetMultipleDimsumResponse(
                    message = "Get record by price range successfully",
                    status = HttpStatusCode.OK.value,
                    data = dimsumList
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
    get("/dimsum/date-price-range") {
        checkQueries("dateFrom", "dateTo", "priceFrom", "priceTo") {
            val dateFrom = call.request.queryParameters["dateFrom"]!!.toLong()
            val dateTo = call.request.queryParameters["dateTo"]!!.toLong()
            val priceFrom = call.request.queryParameters["dateTo"]!!.toDouble()
            val priceTo = call.request.queryParameters["priceTo"]!!.toDouble()
            val dimsumList = dimsumService.getByPriceRangeAndDateRange(dateFrom, dateTo, priceFrom, priceTo)
            val response = Gson().toJson(
                GetMultipleDimsumResponse(
                    message = "Get record by date and price range successfully",
                    status = HttpStatusCode.OK.value,
                    data = dimsumList
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
    delete("/dimsum") {
        val rows = dimsumService.deleteAll()
        val response = Gson().toJson(
            DeleteDimsumResponse(
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
    // route: /dimsum/123
    delete("/dimsum/{id}") {
        val id = call.parameters["id"]?.toLong()

        checkId(id)

        val rows = dimsumService.deleteById(id)
        val response = Gson().toJson(
            DeleteDimsumResponse(
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
    delete("/dimsum/date-range") {
        checkQueries("from", "to") {
            val from = call.request.queryParameters["from"]!!.toLong()
            val to = call.request.queryParameters["to"]!!.toLong()
            val rows = dimsumService.deleteByDateRange(from, to)
            val response = Gson().toJson(
                DeleteDimsumResponse(
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
    put("/dimsum") {
        val newDimsum = call.receiveDimsum() ?: return@put
        val updatedRows = dimsumService.update(newDimsum)
        val response = Gson().toJson(
            UpdateDimsumResponse(
                message = "Successfully updated record with ID ${newDimsum.id}",
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
    put("/dimsum/date-range") {
        checkQueries("from", "to") {
            val newDimsum = call.receiveDimsum() ?: return@checkQueries
            val from = call.request.queryParameters["from"]!!.toLong()
            val to = call.request.queryParameters["to"]!!.toLong()
            val rows = dimsumService.updateByDateRange(from, to, newDimsum)
            val response = Gson().toJson(
                DeleteDimsumResponse(
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
    put("/dimsum/price-range") {
        checkQueries("from", "to") {
            val newDimsum = call.receiveDimsum() ?: return@checkQueries
            val from = call.request.queryParameters["from"]!!.toDouble()
            val to = call.request.queryParameters["to"]!!.toDouble()
            val rows = dimsumService.updateByPriceRange(from, to, newDimsum)
            val response = Gson().toJson(
                DeleteDimsumResponse(
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
    post("/dimsum") {
        val dimsum = call.receiveDimsum() // if return null, input maybe array
        val dimsums = if (dimsum == null) { // get as array, if null, ada kesalahan
            call.receiveDimsumArray()
        } else null
        val dimsumToInsert = arrayListOf<Dimsum>().apply {
            if (dimsum != null) add(dimsum)
            if (dimsums != null) addAll(dimsums)
        }

        for (tea in dimsumToInsert) dimsumService.insert(tea)

        val response = Gson().toJson(
            InsertDimsumResponse(
                message = "Insert success",
                status = HttpStatusCode.Created.value,
                data = dimsumToInsert.size
            )
        )

        call.respondText(
            contentType = ContentType.Application.Json,
            status = HttpStatusCode.Created,
            provider = { response }
        )
    }
}
