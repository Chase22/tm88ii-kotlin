package de.chasenet.receipt

import java.net.InetSocketAddress
import java.net.Socket
import com.fazecast.jSerialComm.SerialPort as JSerialPort

interface Port {
    fun writeBytes(bytes: ByteArray)
}

class SerialPort(private val comPort: JSerialPort): Port {
    override fun writeBytes(bytes: ByteArray) {
        comPort.writeBytes(bytes, bytes.size.toLong())
    }
}

class NetworkPort(private val socket: Socket): Port {
    override fun writeBytes(bytes: ByteArray) {
        socket.getOutputStream().write(bytes)
    }

}