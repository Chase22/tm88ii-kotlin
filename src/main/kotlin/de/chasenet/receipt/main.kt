package de.chasenet.receipt

import com.fazecast.jSerialComm.SerialPort
import java.io.File
import java.net.Socket
import javax.imageio.ImageIO

fun main() {
    val socket = Socket("192.168.200.3", 9000)
    val port = NetworkPort(socket)
    val printer = Printer(port)

//    printer.sendToPrinter(0x1B, 0x74, 19)
//    printer.sendToPrinter(0xd5.toByte(), Printer.LINE_FEED_BYTE)


    fun sendToPrinter(vararg byte: Int) {
        printer.sendToPrinter(*byte.map { it.toByte() }.toByteArray())
    }

    process("/Users/Lukas.Prediger/Pictures/testImage.bmp").forEach {
        sendToPrinter(*(it.map { if (it == 1) 0xDB else 0x20 } + Printer.LINE_FEED_BYTE.toInt()).toIntArray())
    }

    printer.cutPaper()

    Thread.sleep(1000)

    socket.close()
    /*
        val buffer = ByteArray(port.bytesAvailable())
        port.readBytes(buffer, buffer.size.toLong())
        buffer.forEach {
            PrinterStatus(it.toInt()).let(::println)
            println("${it.toString(16).padStart(2, '0').uppercase()}: ${it.toString(2).padStart(8, '0')}")
        }

        port.flushIOBuffers()
        port.closePort()*/
}

private fun setupComPort(): SerialPort {
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
    return port
}

val image = listOf(
// 'testImage', 30x30px
    0xff, 0xff, 0xff, 0xfc, 0xff, 0xff, 0xff, 0xfc, 0xff, 0xff, 0xff, 0xfc, 0xff, 0xff, 0xff, 0xfc,
    0xff, 0xff, 0xff, 0xfc, 0xff, 0xff, 0xff, 0xfc, 0xff, 0xff, 0xff, 0xfc, 0xff, 0xff, 0xff, 0xfc,
    0xff, 0xf0, 0x7f, 0xfc, 0xff, 0xe0, 0x3f, 0xfc, 0xff, 0xc0, 0x1f, 0xfc, 0xff, 0x80, 0x0f, 0xfc,
    0xff, 0x80, 0x0f, 0xfc, 0xff, 0x80, 0x0f, 0xfc, 0xff, 0x80, 0x0f, 0xfc, 0xff, 0x80, 0x0f, 0xfc,
    0xff, 0xc0, 0x1f, 0xfc, 0xff, 0xe0, 0x3f, 0xfc, 0xff, 0xf0, 0x7f, 0xfc, 0xff, 0xff, 0xff, 0xfc,
    0xff, 0xff, 0xff, 0xfc, 0xff, 0xff, 0xff, 0xfc, 0xff, 0xff, 0xff, 0xfc, 0xff, 0xff, 0xff, 0xfc,
    0xff, 0xff, 0xff, 0xfc, 0xff, 0xff, 0xff, 0xfc, 0xff, 0xff, 0xff, 0xfc, 0xff, 0xff, 0xff, 0xfc,
    0xff, 0xff, 0xff, 0xfc, 0xff, 0xff, 0xff, 0xfc
)