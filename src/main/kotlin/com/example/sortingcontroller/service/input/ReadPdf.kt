package com.example.sortingcontroller.service.input

import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.io.IOException


class ReadPdf {
    fun readPdfFile(file: String) {
        //val defaultFile: String = "Facture n°2022121018 - ASCORIUMMOST.pdf"

        val openFile = OpenFile()
        val fileList = openFile.getFileList()

        println("file $file exists: ")
        println(openFile.getFileIfExists(fileList, file))
        if (openFile.getFileIfExists(fileList, file)){
            val ocr = OCRPdf()
            ocr.performOCR(file)}
    }

    fun readPdfMethod(filePath: String) {
        try {
            val file = File(filePath)
            if (file.exists()) {

                val document = Loader.loadPDF(file)
                val pdfStripper = PDFTextStripper()
                val text = pdfStripper.getText(document)
                document.close()
                println(text)
                println("from fun readPdfFile endpoint")
            } else {
                println("File does not exist.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}


