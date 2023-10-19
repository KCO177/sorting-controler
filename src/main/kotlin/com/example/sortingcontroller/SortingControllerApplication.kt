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
// @test


calculate.calculateDifferencePartAmount(mo)

val part = "GB LHD"
val time : Double = 20.6
val amount : Int = 1000

val compareTime = Compare.SortingTime()
println(compareTime.compareTimeWithConstTimeTact(part, time, amount))

compareTime.compareSortingTimeInMo(mo)

}
