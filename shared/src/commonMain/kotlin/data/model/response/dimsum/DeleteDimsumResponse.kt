package data.model.response.dimsum

import data.model.response.CommonSingleResponse
import kotlinx.serialization.Serializable

/**
 * Delete dimsum response
 *
 * @param message the message
 * @param status http status code
 * @param data number of row deleted
 */
@Serializable
data class DeleteDimsumResponse(
    override val message: String,
    override val status: Int,
    override val data: Int?
): CommonSingleResponse<Int>
