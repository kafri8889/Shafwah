package common

/**
 * Append [s] every [k] digits
 * @param s the string to append
 * @param k the number of digits to append
 * @param reverse if reversed, the appended string will be start from end
 * @param appendLast
 * @return modified string
 */
fun String.appendEveryDigit(
    s: String,
    k: Int,
    reverse: Boolean = false,
    appendLast: Boolean = false
): String {
    val range = if (reverse) lastIndex downTo 0 else indices
    return StringBuilder().apply {
        for (i in range) {
            val mi = if (reverse) i else i + 1
            val shouldAppend = mi % k == 0

            append(this@appendEveryDigit[if (reverse) this@appendEveryDigit.lastIndex - i else i])

            if (shouldAppend) append(s)
        }
    }.let { if (it.endsWith(s) && !appendLast) it.deleteRange(it.length - s.length, it.length).toString() else it.toString() }
}
