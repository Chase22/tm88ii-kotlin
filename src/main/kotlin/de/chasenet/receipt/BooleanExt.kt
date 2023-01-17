package de.chasenet.receipt

fun Boolean.toInt(trueValue: Int = 1) = if (this) trueValue else 0

fun Boolean.toByte(trueValue: Byte = 1, falseValue: Byte = 0): Byte = if (this) trueValue else falseValue