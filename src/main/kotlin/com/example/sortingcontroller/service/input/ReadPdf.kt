package com.example.sortingcontroller.service.input

import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.io.IOException
import java.util.regex.Pattern


class ReadPdf {
    fun readPdfFile(file: String): Any {
        //val defaultFile: String = "Facture n°2022121018 - ASCORIUMMOST.pdf"

        val openFile = OpenFile()
        val fileList = openFile.getFileList()
        var textList: MutableList<String> = mutableListOf()
        val itemsFromPDf:String
        println("file $file exists: ")
        println(openFile.getFileIfExists(fileList, file))

        if (openFile.getFileIfExists(fileList, file)) {
            val ocr = OCRPdf()
            textList = ocr.performOCR(pdfFile = file)
            /*if (textList.isNotEmpty()){
                val analyse = AnalysePdf()
                itemsFromPDf = analyse.findItemsInv(textList).toString()
                return itemsFromPDf
            }*/
            println(textList)
        }

            return textList
        }
}



    /*fun readPdfMethod(filePath: String) {
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
    }*/

    // tabulas implemenation DONE - lepsi vysledek poskztuje OCR
    //TODO make supplier settings table
    //TODO try to read pdf with readPDfMethod ifEmpty:
    //TODO with OCR, read supplier name, set settings according to the specific names for the supplier
    //TODO read OCR extract items to list
    //TODO post list items to db




