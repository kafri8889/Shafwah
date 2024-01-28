package repository

import data.response.InsertIceTeaResponse
import model.IceTea

interface IceTeaRepository {

    suspend fun postIceTea(vararg iceTea: IceTea): InsertIceTeaResponse

}