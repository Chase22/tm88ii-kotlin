package de.chasenet.receipt

interface ByteEnum {
    val value: Byte
}

enum class PrinterJustification(override val value: Byte): ByteEnum {
    LEFT(48.toByte()), CENTER(49.toByte()), RIGHT(50.toByte())
}

enum class UnderlineMode(override val value: Byte): ByteEnum {
    OFF(48), SINGLE_DOT(49), DOUBLE_DOT(50)
}