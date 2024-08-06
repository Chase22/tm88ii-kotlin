package de.chasenet.receipt

import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.escpos.barcode.QRCode
import com.github.anastaciocintra.escpos.image.BitonalThreshold
import com.github.anastaciocintra.escpos.image.CoffeeImageImpl
import com.github.anastaciocintra.escpos.image.EscPosImage
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper
import java.io.File
import java.net.Socket
import javax.imageio.ImageIO

fun main() {
    val socket = Socket("192.168.200.3", 9000)
    val escPos = EscPos(socket.getOutputStream())
    val image = ImageIO.read(File("/Users/Lukas.Prediger/Downloads/photo_2023-09-15_22-53-18.jpg"))

    val escposImage = EscPosImage(CoffeeImageImpl(image), BitonalThreshold(127))

    escPos.write(RasterBitImageWrapper(), escposImage)
    escPos.feed(10)
    escPos.cut(EscPos.CutMode.FULL)
}