package data.model.response.dimsum

import data.model.response.CommonSingleResponse
import kotlinx.serialization.Serializable

/**
 * Update dimsum API response
 *
 * @param data Number of updated rows
 */
@Serializable
data class UpdateDimsumResponse(
    override val message: String,
    override val status: Int,
    override val data: Int
): CommonSingleResponse<Int>
