package data.model.response.ice_tea

import data.model.response.CommonSingleResponse
import kotlinx.serialization.Serializable

/**
 * Delete response
 *
 * @param message the message
 * @param status http status code
 * @param data number of row deleted
 */
@Serializable
data class DeleteIceTeaResponse(
    override val message: String,
    override val status: Int,
    override val data: Int?
): CommonSingleResponse<Int>
