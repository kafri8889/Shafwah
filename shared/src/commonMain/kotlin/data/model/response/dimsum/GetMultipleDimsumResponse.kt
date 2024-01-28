package data.model.response.dimsum

import data.model.Dimsum
import data.model.response.CommonMultipleResponse
import kotlinx.serialization.Serializable

@Serializable
data class GetMultipleDimsumResponse(
    override val message: String,
    override val status: Int,
    override val data: List<Dimsum>
): CommonMultipleResponse<Dimsum>
