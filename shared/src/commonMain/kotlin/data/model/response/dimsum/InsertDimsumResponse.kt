package data.model.response.dimsum

import data.model.response.CommonSingleResponse
import kotlinx.serialization.Serializable

/**
 * Insert dimsum API response
 *
 * @param data Inserted rows
 */
@Serializable
data class InsertDimsumResponse(
    override val message: String,
    override val status: Int,
    override val data: Int
): CommonSingleResponse<Int>
