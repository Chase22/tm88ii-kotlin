package de.chasenet.receipt

data class PrintMode(
    val characterFont: Boolean,
    val emphasizedMode: Boolean,
    val doubleHeightMode: Boolean,
    val doubleWidthMode: Boolean,
    val underlineMode: Boolean
) {
    fun toByte(): Byte {
        val value = characterFont.toInt() or
                emphasizedMode.toInt(0x08) or
                doubleHeightMode.toInt(0x10) or
                doubleWidthMode.toInt(0x20) or
                underlineMode.toInt(0x80)

        return value.toByte()
    }
}

fun Boolean.toInt(trueValue: Int = 1) = if (this) trueValue else 0