package com.example.sortingcontroller.service.input

import net.sourceforge.tess4j.ITesseract
import net.sourceforge.tess4j.Tesseract
import org.apache.pdfbox.Loader
import org.apache.pdfbox.rendering.PDFRenderer
import java.io.File
import java.util.*
import javax.imageio.ImageIO


class OCRPdf {
    //TODO path to settings
    private val defaultDPI: Int = 300
    private val defaultPdfFilePath: String = "C:/Users/START/OneDrive/Desktop/mo_faktury/"
    private val tesseractPath: String  = "C:/Program Files/Tesseract-OCR/tessdata"

    private fun convertPdfToImage(pdfFile: File, dpi: Int): File {
        val document = Loader.loadPDF(pdfFile)
        val renderer = PDFRenderer(document)
        val image = renderer.renderImageWithDPI(0, dpi.toFloat()) // Set DPI here
        val outputImageFile = File("$defaultPdfFilePath/image01.png")
        ImageIO.write(image, "png", outputImageFile)
        println("image saved")
        document.close()
        return outputImageFile
    }

    fun performOCR(pdfFile: String, dpi: Int = defaultDPI ): List<String> {

        val results = mutableListOf<String>()
        val imageFile = convertPdfToImage(File(defaultPdfFilePath+pdfFile), dpi)
        val tesseract: ITesseract = Tesseract()
        tesseract.setDatapath(tesseractPath)
        //TODO add language to the settings
        tesseract.setLanguage("eng")
        tesseract.setTessVariable("user_defined_dpi", "300")
        val recognisedText = tesseract.doOCR(imageFile)
        val rows = recognisedText.split('\n')
        results.addAll(rows)

        val lowercaseResults = results.map { it.lowercase(Locale.getDefault()) }

        println(lowercaseResults)

        if (imageFile.exists()) {
            imageFile.delete()
            println("image deleted")
        }

        return lowercaseResults
    }


}
