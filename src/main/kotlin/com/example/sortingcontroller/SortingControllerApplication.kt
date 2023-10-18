package com.example.sortingcontroller

import com.example.sortingcontroller.service.Calculate
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SortingControllerApplication

fun main(args: Array<String>) {
    runApplication<SortingControllerApplication>(*args)

val calculate = Calculate()
val mo = "MO2"
calculate.calculateDiffInvMo(mo)
val part = "GB"
val amount_of_parts : Int = 100
calculate.calculateSortTime(part, amount_of_parts)
}
