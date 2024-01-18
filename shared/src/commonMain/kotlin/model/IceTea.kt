package model

import data.IceTeaVariant
import kotlinx.serialization.Serializable

@Serializable
data class IceTea(
    val id: Long,
    val variant: IceTeaVariant,
    val createdAt: Long,
    /**
     * price on date [createdAt]
     *
     * jadi misal dibulan januari harganya 5k tapi di bulan febuari naik jadi 6k,
     * record data bulan sebelumnya (januari) tetep 5k, tapi dibulan febuari dst 6k
     */
    val price: Double
)
