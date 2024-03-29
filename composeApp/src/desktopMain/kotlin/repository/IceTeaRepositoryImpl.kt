package repository

import data.model.response.ice_tea.InsertIceTeaResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import data.model.IceTea
import networking.KtorClient

class IceTeaRepositoryImpl: IceTeaRepository {

    override suspend fun postIceTea(vararg iceTea: IceTea): InsertIceTeaResponse {
        return KtorClient.client.use {
            it.post("${KtorClient.BASE_URL}/iceTea") {
                setBody(iceTea)
            }.body<InsertIceTeaResponse>()
        }
    }
}