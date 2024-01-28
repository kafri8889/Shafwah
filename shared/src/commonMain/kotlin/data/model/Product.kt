package data.model

/**
 * Base class untuk produk yg dijual, contohnya es teh, dimsum, jus, dll.
 */
interface Product {

    val id: Long
    val name: String
    /**
     * price on date [createdAt]
     *
     * jadi misal dibulan januari harganya 5k tapi di bulan febuari naik jadi 6k,
     * record data bulan sebelumnya (januari) tetep 5k, tapi dibulan febuari dst 6k
     */
    val price: Double
    val createdAt: Long

}