package data.model

import kotlinx.serialization.Serializable

@Serializable
data class Dimsum(
    /**
     * Unit price
     */
    val unitPrice: Double,
    /**
     * The number of dimsum purchased
     *
     * e.g:
     *
     * - `unit price = Rp.2.500, amount = 4`
     *
     * - `total price = Rp.2.500 * 4 = Rp.10.000`
     */
    val amount: Int,

    override val id: Long,
    override val name: String,
    override val createdAt: Long,
    /**
     * Get total price from this record
     *
     * formula: total price = [price] * [amount]
     */
    override val price: Double = unitPrice * amount
): Product
