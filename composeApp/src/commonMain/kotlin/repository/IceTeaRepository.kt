package repository

import data.model.response.ice_tea.InsertIceTeaResponse
import data.model.IceTea

interface IceTeaRepository {

    suspend fun postIceTea(vararg iceTea: IceTea): InsertIceTeaResponse

}