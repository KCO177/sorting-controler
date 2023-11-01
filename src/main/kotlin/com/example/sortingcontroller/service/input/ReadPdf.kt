package com.example.sortingcontroller.service.input

import com.example.sortingcontroller.rest.CreateInvoice
import com.example.sortingcontroller.rest.DB_data_collector


class ReadPdf {
    fun readPdfFile(file: String): Any {

        val openFile = OpenFile()
        val fileList = openFile.getFileList()
        var textList: List<String> = mutableListOf<String>()
        val itemsFromPDf: CreateInvoice
        println("file $file exists: ")
        //println(openFile.getFileIfExists(fileList, file))

        if (openFile.getFileIfExists(fileList, file)) {
            val ocr = OCRPdf()
            textList = ocr.performOCR(pdfFile = file)
            if (textList.isNotEmpty()) {
                //println(textList)
                val analyse = AnalysePdf()
                val postToDb = DB_data_collector()
                val setSettings = SetSettings()
                val pdfIsInv = setSettings.factureInFileName(file)
                println("pdfIsInv $pdfIsInv")
                //todo € comma supplier list
                val commaThousandSuppliers : MutableList<String> = mutableListOf("cps")

                // check if mo or inv

                val supplierExists = setSettings.supplierExists(textList, pdfIsInv)
                return if (supplierExists) {
                    //println("supplier exist $supplierExists")
                    val supplier = setSettings.findSupplier(textList, pdfIsInv)
                    println("supplier $supplier")
                    val settings = setSettings.getSettings(supplier)
                    println("settings $settings")
                    if (supplier in commaThousandSuppliers){
                        textList = analyse.processEuroStrings(textList)}

                    //TODO before post check if the inv or mo exists already

                    if (pdfIsInv) {
                        postToDb.postNewInv(analyse.findItemsInv(textList, settings))
                    }else{
                        postToDb.postNewMo(analyse.findItemsMo(textList, settings))
                    }


                } else {
                    "supplier does not exists"
                }
                //println(textList)
            } else {
                return "file does not exists"
            }


        }
        return "file does not exists"
    }
}









