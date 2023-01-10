package de.chasenet.receipt

enum class PrinterJustification(val value: Byte) {
    LEFT(48.toByte()), CENTER(49.toByte()), RIGHT(50.toByte())
}