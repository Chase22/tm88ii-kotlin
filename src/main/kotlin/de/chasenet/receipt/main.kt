package de.chasenet.receipt

import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent

fun main() {
    SerialPort.getCommPorts().forEach { println("${it.systemPortName}: ${it.descriptivePortName}") }
    val port = SerialPort.getCommPort("tty.usbmodem2101")
    port.setComPortParameters(9600, 8, SerialPort.TWO_STOP_BITS, SerialPort.ODD_PARITY)

    port.setComPortTimeouts(
        /* newTimeoutMode = */ SerialPort.TIMEOUT_WRITE_BLOCKING or SerialPort.TIMEOUT_READ_BLOCKING,
        /* newReadTimeout = */ 500,
        /* newWriteTimeout = */ 500
    )

    if (!port.openPort()) {
        throw IllegalStateException("Couldn't open port")
    }
    val printer = Printer(port)

    printer.initializePrinter()

    printer.selectPrintMode(
        PrintMode(
            characterFont = false,
            emphasizedMode = true,
            doubleHeightMode = false,
            doubleWidthMode = true,
            underlineMode = false
        )
    )
    printer.printLine("A")
    printer.resetPrintMode()
    printer.setReversePrintingMode(true)
    printer.printLine("AAAAA")
    printer.cutPaper()
    printer.kickDrawer()

    Thread.sleep(1000)

    val buffer = ByteArray(port.bytesAvailable())
    port.readBytes(buffer, buffer.size.toLong())
    buffer.forEach {
        PrinterStatus(it.toInt()).let(::println)
        println("${it.toString(16).padStart(2, '0').uppercase()}: ${it.toString(2).padStart(8, '0')}")
    }

    port.flushIOBuffers()
    port.closePort()
}

fun SerialPort.listenForStatus(body: (PrinterStatus) -> Unit) {
    addDataListener(object : SerialPortDataListener {
        override fun getListeningEvents(): Int = SerialPort.LISTENING_EVENT_DATA_AVAILABLE

        override fun serialEvent(event: SerialPortEvent) {
            event.receivedData.map { PrinterStatus(it.toInt()) }.forEach(body)
        }
    })
}