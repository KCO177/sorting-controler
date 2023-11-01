package com.example.sortingcontroller

import com.example.sortingcontroller.rest.DB_data_collector
import com.example.sortingcontroller.service.input.OCRPdf
import com.example.sortingcontroller.service.input.OpenFile
import com.example.sortingcontroller.service.input.SetSettings
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SortingControllerApplication

fun main(args: Array<String>) {
    runApplication<SortingControllerApplication>(*args)


}


