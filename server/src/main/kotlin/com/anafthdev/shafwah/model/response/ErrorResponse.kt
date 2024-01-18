package com.anafthdev.shafwah.model.response

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * Error response
 */
@Serializable
data class ErrorResponse(
    override val message: String,
    override val status: Int,
    override val data: @Contextual Any?
): CommonSingleResponse<Any>
