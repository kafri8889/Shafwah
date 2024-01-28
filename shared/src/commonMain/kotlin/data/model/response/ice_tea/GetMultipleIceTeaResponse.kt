package data.model.response.ice_tea

import data.model.IceTea
import data.model.response.CommonMultipleResponse
import kotlinx.serialization.Serializable

@Serializable
data class GetMultipleIceTeaResponse(
    override val message: String,
    override val status: Int,
    override val data: List<IceTea>
): CommonMultipleResponse<IceTea>
