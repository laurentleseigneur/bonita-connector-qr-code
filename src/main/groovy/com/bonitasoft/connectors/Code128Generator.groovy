package com.bonitasoft.connectors

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.oned.Code128Writer

import javax.imageio.ImageIO
import java.nio.file.Files

class Code128Generator {

    File generateCode128Image(String barcodeText, Integer width, Integer height ) throws Exception {
        Code128Writer barcodeWriter = new Code128Writer()
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_128, width, height)
        def bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix)
        File output = Files.createTempFile("img", ".png").toFile()
        ImageIO.write(bufferedImage, "png", output)
        output
    }


}
