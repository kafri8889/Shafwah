package repository

import data.model.Dimsum
import data.model.response.dimsum.InsertDimsumResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import networking.KtorClient

class DimsumRepositoryImpl: DimsumRepository {

    override suspend fun postDimsum(vararg dimsum: Dimsum): InsertDimsumResponse {
        return KtorClient.client.use {
            it.post("${KtorClient.BASE_URL}/dimsum") {
                setBody(dimsum)
            }.body<InsertDimsumResponse>()
        }
    }
}