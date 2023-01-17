package de.chasenet.receipt

import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent

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

    printer.print {
        center {
            withPrintMode(PrintMode(
                doubleHeightMode = true,
                doubleWidthMode = true,
                emphasizedMode = true,
                underlineMode = true
            )) {
                print("TEST")
            }
        }
        cutPaper()
    }

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