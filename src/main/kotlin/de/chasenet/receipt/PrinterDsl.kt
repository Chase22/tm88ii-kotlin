package de.chasenet.receipt

class PrinterDsl(private val printer: Printer) {

    fun print(string: String) {
        printer.printLine(string)
    }

    fun feedLines(lineNumber: Int) {
        printer.printAndFeed(lineNumber)
    }

    fun justification(justification: PrinterJustification, block: PrinterDsl.() -> Unit) {
        printer.setJustification(justification)
        block()
        printer.setJustification(PrinterJustification.LEFT)
    }

    fun center(block: PrinterDsl.() -> Unit) {
        justification(PrinterJustification.CENTER, block)
    }

    fun right(block: PrinterDsl.() -> Unit) {
        justification(PrinterJustification.RIGHT, block)
    }

    fun withPrintMode(printMode: PrintMode, block: PrinterDsl.() -> Unit) {
        printer.selectPrintMode(printMode)
        block()
        printer.resetPrintMode()
    }

    fun reversed(block: PrinterDsl.() -> Unit) {
        printer.setReversePrintingMode(true)
        block()
        printer.setReversePrintingMode(false)
    }

    fun doubleStrike(block: PrinterDsl.() -> Unit) {
        printer.setDoubleStrikeMode(true)
        block()
        printer.setDoubleStrikeMode(false)
    }

    fun emphasis(block: PrinterDsl.() -> Unit) {
        printer.setEmphasisMode(true)
        block()
        printer.setEmphasisMode(false)
    }

    fun withUnderLine(underlineMode: UnderlineMode, block: PrinterDsl.() -> Unit) {
        printer.setUnderlineMode(underlineMode)
        block()
        printer.setUnderlineMode(UnderlineMode.OFF)
    }

    fun cutPaper() {
        printer.cutPaper(5)
    }

    fun kickDrawer() {
        printer.kickDrawer()
    }
}