package com.example.sortingcontroller.service.input

import net.sourceforge.tess4j.ITesseract
import net.sourceforge.tess4j.Tesseract
import org.apache.pdfbox.Loader
import org.apache.pdfbox.rendering.PDFRenderer
import java.io.File
import javax.imageio.ImageIO


class OCRPdf {
    val defaultDPI: Int = 300
    val defaultPdfFilePath: String = "C:/Users/START/OneDrive/Desktop/mo_faktury/"
    val tesseractPath: String  = "C:/Program Files/Tesseract-OCR/tessdata"

    fun convertPdfToImage(pdfFile: File, dpi: Int): File {
        val document = Loader.loadPDF(pdfFile)
        val renderer = PDFRenderer(document)
        val image = renderer.renderImageWithDPI(0, dpi.toFloat()) // Set DPI here
        val outputImageFile = File("$defaultPdfFilePath/image01.png")
        ImageIO.write(image, "png", outputImageFile)
        println("image saved")
        document.close()
        return outputImageFile
    }

    fun performOCR(pdfFile: String, dpi: Int = defaultDPI ): MutableList<String> {

        val results = mutableListOf<String>()
        val imageFile = convertPdfToImage(File(defaultPdfFilePath+pdfFile), dpi)
        val tesseract: ITesseract = Tesseract()
        tesseract.setDatapath(tesseractPath)
        tesseract.setLanguage("eng")
        tesseract.setTessVariable("user_defined_dpi", "300")
        val recognisedText = tesseract.doOCR(imageFile)
        val rows = recognisedText.split('\n')
        results.addAll(rows)
        //println(results)

        if (imageFile.exists()) {
            imageFile.delete()
            println("Image file deleted")
        }

        return results
    }


}
