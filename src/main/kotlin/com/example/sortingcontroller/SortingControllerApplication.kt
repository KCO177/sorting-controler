package com.example.sortingcontroller

import com.example.sortingcontroller.service.Calculate
import com.example.sortingcontroller.service.Compare
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SortingControllerApplication

fun main(args: Array<String>) {
    runApplication<SortingControllerApplication>(*args)

val calculate = Calculate()
val mo = "MO2"
calculate.calculateDiffInvMo(mo)

calculate.calculateDifferencePartAmount(mo)

val compareTime = Compare.SortingTime()

compareTime.compareSortingTimeInvMO(mo)

}
