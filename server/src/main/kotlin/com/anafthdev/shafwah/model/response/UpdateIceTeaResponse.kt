package com.anafthdev.shafwah.model.response

import kotlinx.serialization.Serializable

/**
 * Update ice tea API response
 *
 * @param data Number of updated rows
 */
@Serializable
data class UpdateIceTeaResponse(
    override val message: String,
    override val status: Int,
    override val data: Int
): CommonSingleResponse<Int>
