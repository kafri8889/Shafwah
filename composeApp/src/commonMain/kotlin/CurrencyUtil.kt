import common.appendEveryDigit

object CurrencyUtil {

    /**
     * Convert [amount] to rupiah e.g: `Rp.3.000`
     */
    fun toRupiah(amount: Int): String {
        return "Rp.${amount.toString().appendEveryDigit(".", 3, true)}"
    }

}