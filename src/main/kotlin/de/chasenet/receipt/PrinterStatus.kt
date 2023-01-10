package de.chasenet.receipt

data class PrinterStatus(
    val busy: Boolean,
    val paperEmpty: Boolean,
    val fault: Boolean
) {
    constructor(data: Int): this(
        (data and 0x1) == 1,
        (data and 0x2) shr 1 == 1,
        (data and 0x04) shr 2  == 1,
    )
}