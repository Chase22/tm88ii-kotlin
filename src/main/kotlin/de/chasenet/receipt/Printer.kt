package de.chasenet.receipt

import com.fazecast.jSerialComm.SerialPort
import java.nio.charset.Charset

class Printer(private val comPort: SerialPort) {
    fun lineFeed() {
        sendToPrinter(LINE_FEED_BYTE)
    }

    fun print(string: String) {
        val bytes = string.toByteArray(CODEPAGE437_CHARSET).map {
            //Replace space (hex 20) with DEL (hex 7F) byte. Space does not get printed
            if (it == 0x20.toByte()) SPACE_BYTE else it
        }.toByteArray()
        writeByteArrayToPrinter(bytes)
    }

    fun printLine(string: String) {
        print(string)
        lineFeed()
    }

    fun selectPrintMode(printMode: PrintMode) {
        sendToPrinter(ESC_BYTE, PRINT_MODE_BYTE, printMode.toByte())
    }

    fun resetPrintMode() {
        sendToPrinter(ESC_BYTE, PRINT_MODE_BYTE, 0x48)
    }

    fun initializePrinter() {
        sendToPrinter(ESC_BYTE, INITIALIZE_PRINTER_BYTE)
    }

    fun printAndFeed(feedNumber: Int) {
        if (!feedNumber.isValidByte()) {
            throw IllegalArgumentException("Feed number must be between 0 and 255. Was $feedNumber")
        }
        sendToPrinter(ESC_BYTE, FEED_BYTE, feedNumber.toByte())

    }

    fun kickDrawer() {
        sendToPrinter(ESC_BYTE, GENERATE_PULSE_BYTE, 48, 50, 50)
    }

    fun setJustification(justification: PrinterJustification) {
        sendToPrinter(ESC_BYTE, SET_JUSTIFICATION_BYTE, justification.value)
    }

    fun setReversePrintingMode(enabled: Boolean) {
        val enableByte: Byte = if (enabled) 0x49 else 0x48
        sendToPrinter(GS_BYTE, REVERSE_PRINTING_BYTE, enableByte)
    }

    fun cutPaper(offset: Int = 0) {
        if (!offset.isValidByte()) {
            throw IllegalArgumentException("offset must be between 0 and 255. Was $offset")
        }

        // Add 10 to cut paper under printed text
        val byte = (offset + 10).let {
            when (it) {
                0 -> 0x48
                1 -> 0x49
                else -> it.toByte()
            }
        }

        sendToPrinter(GS_BYTE, CUT_PAPER_BYTE, 66, byte)
    }

    fun sendToPrinter(vararg bytes: Byte) {
        writeByteArrayToPrinter(bytes.toTypedArray().toByteArray())
    }

    fun writeByteArrayToPrinter(bytes: ByteArray) {
        comPort.writeBytes(bytes, bytes.size.toLong())
    }

    companion object {

        private const val ESC_BYTE = 0x1B.toByte()
        private const val GS_BYTE = 0x1D.toByte()

        private const val REVERSE_PRINTING_BYTE = 0x42.toByte()
        private const val CUT_PAPER_BYTE = 0x56.toByte()
        private const val SET_JUSTIFICATION_BYTE = 0x61.toByte()
        private const val FEED_BYTE = 0x64.toByte()
        private const val GENERATE_PULSE_BYTE = 0x70.toByte()

        const val LINE_FEED_BYTE = 0x0A.toByte()
        private const val PRINT_MODE_BYTE = 0x21.toByte()

        private const val INITIALIZE_PRINTER_BYTE = 0x40.toByte()

        private const val SPACE_BYTE = 0x7F.toByte()

        val CODEPAGE437_CHARSET = Charset.forName("Cp437")

        private val BYTE_RANGE = 0..255

        private fun Int.isValidByte() = BYTE_RANGE.contains(this)
    }

}