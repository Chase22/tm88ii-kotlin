package de.chasenet.receipt

import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

fun process(imagePath: String): List<List<Int>> {
    val image = ImageIO.read(File(imagePath))
    val data = (0 until image.height).map {y ->
        (0 until image.width).map { x->
            Color(image.getRGB(x,y)).let {
                (0.2126*it.red + 0.7152*it.green + 0.0722*it.blue)
            }
        }.map { if (it < 0.5) 1 else 0 }
    }
    return data

}