package data.model

import data.IceTeaVariant
import kotlinx.serialization.Serializable

@Serializable
data class IceTea(
    val variant: IceTeaVariant,

    override val id: Long,
    override val createdAt: Long,
    override val price: Double,
    override val name: String = variant.displayName,
): Product
