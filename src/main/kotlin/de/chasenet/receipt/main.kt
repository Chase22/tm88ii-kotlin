package de.chasenet.receipt

import com.fazecast.jSerialComm.SerialPort

fun main() {
   val port = SerialPort.getCommPorts()
       .firstOrNull {
           it.descriptivePortName.contains("Arduino Leonardo") && it.systemPortName.startsWith("tty")
       } ?: throw IllegalStateException("Cannot find arduino")

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

//    printer.sendToPrinter(0x1B, 0x74, 19)
//    printer.sendToPrinter(0xd5.toByte(), Printer.LINE_FEED_BYTE)

    printer.printTestPage()

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