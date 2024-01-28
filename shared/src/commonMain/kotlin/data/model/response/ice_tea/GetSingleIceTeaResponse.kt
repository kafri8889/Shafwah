package data.model.response.ice_tea

import data.model.response.CommonSingleResponse
import kotlinx.serialization.Serializable
import data.model.IceTea

@Serializable
data class GetSingleIceTeaResponse(
    override val message: String,
    override val status: Int,
    override val data: IceTea?
): CommonSingleResponse<IceTea>
