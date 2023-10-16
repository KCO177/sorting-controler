package com.example.sortingcontroller

import com.example.sortingcontroller.calculation.Calculate
import com.example.sortingcontroller.rest.db_data_collector
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SortingControllerApplication

fun main(args: Array<String>) {
    runApplication<SortingControllerApplication>(*args)
val calculate = Calculate()
val mo = "MO3"
calculate.calculateDiffInvMo(mo)
val part = "GB"
val amount_of_parts : Int = 100
calculate.calculateSortTact(part, amount_of_parts)
}
