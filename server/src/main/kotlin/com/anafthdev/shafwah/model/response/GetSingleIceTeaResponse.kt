package com.anafthdev.shafwah.model.response

import kotlinx.serialization.Serializable
import model.IceTea

@Serializable
data class GetSingleIceTeaResponse(
    override val message: String,
    override val status: Int,
    override val data: IceTea?
): CommonSingleResponse<IceTea>
