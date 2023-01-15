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

    fun cutPaper() {
        printer.cutPaper(5)
    }

    fun kickDrawer() {
        printer.kickDrawer()
    }
}