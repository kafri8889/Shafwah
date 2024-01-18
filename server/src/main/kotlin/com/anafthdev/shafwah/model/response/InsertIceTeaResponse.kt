package com.anafthdev.shafwah.model.response

import kotlinx.serialization.Serializable

/**
 * Insert ice tea API response
 *
 * @param data Record ID
 */
@Serializable
data class InsertIceTeaResponse(
    override val message: String,
    override val status: Int,
    override val data: Long
): CommonSingleResponse<Long>
