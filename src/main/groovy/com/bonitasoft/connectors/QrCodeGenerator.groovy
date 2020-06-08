package com.bonitasoft.connectors

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

import javax.imageio.ImageIO
import java.nio.file.Files

class QrCodeGenerator {

    File generateQRCodeImage(String barcodeText, Integer size) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter()
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, size, size)
        def bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix)
        File output = Files.createTempFile("img", ".png").toFile()
        ImageIO.write(bufferedImage, "png", output)
        output
    }


}
