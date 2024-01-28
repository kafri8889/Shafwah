package data.model.response.dimsum

import data.model.Dimsum
import data.model.response.CommonSingleResponse
import kotlinx.serialization.Serializable

@Serializable
data class GetSingleDimsumResponse(
    override val message: String,
    override val status: Int,
    override val data: Dimsum?
): CommonSingleResponse<Dimsum>
