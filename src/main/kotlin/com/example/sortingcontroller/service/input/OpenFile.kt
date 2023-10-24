package com.example.sortingcontroller.service.input

import java.io.File

class OpenFile {
    //TODO vytahnout paths do settings
    val defaultDocumentPath : String =  "C:/Users/START/OneDrive/Desktop/mo_faktury"
    val defaultFile : String = "1SMRC0323RAIM1131.pdf"

    //get list of existing files in folder path
    fun getFileList(folderPath: String = defaultDocumentPath): List<String> {
        val folder = File(folderPath)
        val fileNameList = mutableListOf<String>()

        if (folder.exists() && folder.isDirectory) {
            val filesInFolder = folder.listFiles()

            if (filesInFolder != null) {
                for (file in filesInFolder) {
                    val fileName = file.name
                    fileNameList.add(fileName)
                }
            } else {
                fileNameList.add("No files found in the folder.")
                println("No files found in the folder.")
            }
        } else {
            println("The specified path is not a valid directory.")
            fileNameList.add("The specified path is not a valid directory.")
        }

        return fileNameList
    }


    //check if the file exists in the folder return bool
        fun getFileIfExists(fileNameList: List<String>, fileName: String): Boolean {
            return fileNameList.contains(fileName)
        }
}

