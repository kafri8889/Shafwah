package repository

import data.model.Dimsum
import data.model.response.dimsum.InsertDimsumResponse

interface DimsumRepository {

    suspend fun postDimsum(vararg dimsum: Dimsum): InsertDimsumResponse

}