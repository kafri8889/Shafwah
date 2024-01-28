package data.model.response.ice_tea

import data.model.response.CommonSingleResponse
import kotlinx.serialization.Serializable

/**
 * Insert ice tea API response
 *
 * @param data Inserted rows
 */
@Serializable
data class InsertIceTeaResponse(
    override val message: String,
    override val status: Int,
    override val data: Int
): CommonSingleResponse<Int>
