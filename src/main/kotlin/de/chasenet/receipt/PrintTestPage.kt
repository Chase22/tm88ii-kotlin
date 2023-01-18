package de.chasenet.receipt

fun Printer.printTestPage() = print {
    doubleStrike {
        print("Double Strike")
    }
    emphasis {
        print("Emphasis")
    }
    reversed {
        print("Reversed")
    }
    withUnderLine(UnderlineMode.SINGLE_DOT) {
        print("Single Dot Underline")
    }
    withUnderLine(UnderlineMode.DOUBLE_DOT) {
        print("Double Dot Underline")
    }
    center {
        print("Center justification")
    }
    right {
        print("Right justification")
    }
    withPrintMode(PrintMode(true)) {
        print("Character Font B")
    }
    withPrintMode(PrintMode(doubleHeightMode = true)) {
        print("Double Height Mode")
    }
    withPrintMode(PrintMode(doubleWidthMode = true)) {
        print("Double Width Mode")
    }
    cutPaper()
}