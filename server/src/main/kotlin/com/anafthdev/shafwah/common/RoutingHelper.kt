package com.anafthdev.shafwah.common

import com.google.gson.Gson
import data.model.response.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Check if url contains [queries] or not, if not, return bad request, otherwise execute [block]
 */
suspend fun PipelineContext<Unit, ApplicationCall>.checkQueries(
    vararg queries: String,
    block: suspend () -> Unit
) {
    for (i in queries.indices) {
        if (!call.request.queryParameters.contains(queries[i])) {
            call.respondText(
                contentType = ContentType.Application.Json,
                text = Gson().toJson(
                    ErrorResponse(
                        message = "Bad request: \"${queries[i]}\" query not found in url. Did you forget it?",
                        status = HttpStatusCode.BadRequest.value,
                        data = null
                    )
                )
            )

            return
        }
    }

    block()
}

/**
 * Check if url contains [params] or not, if not, return bad request, otherwise execute [block]
 */
suspend fun PipelineContext<Unit, ApplicationCall>.checkParams(
    vararg params: String,
    block: suspend () -> Unit
) {
    for (i in params.indices) {
        if (!call.parameters.contains(params[i])) {
            call.respondText(
                contentType = ContentType.Application.Json,
                text = Gson().toJson(
                    ErrorResponse(
                        message = "Bad request: \"${params[i]}\" parameter not found",
                        status = HttpStatusCode.BadRequest.value,
                        data = null
                    )
                )
            )

            return
        }
    }

    block()
}

@OptIn(ExperimentalContracts::class)
suspend inline fun <T> PipelineContext<Unit, ApplicationCall>.checkId(id: T?) {
    contract {
        returns() implies (id != null)
    }

    checkParams("id") {}
}
