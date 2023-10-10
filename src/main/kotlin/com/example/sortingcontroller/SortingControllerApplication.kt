package com.example.sortingcontroller

import com.example.sortingcontroller.rest.db_data_collector
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SortingControllerApplication

fun main(args: Array<String>) {
    runApplication<SortingControllerApplication>(*args)
val db_collector = db_data_collector()
val mo = "MO2"
val cost_invoice : Double = db_collector.get_invoice_costs_for_mo(mo)
val cost_mo : Double = db_collector.get_mission_order_costs_for_mo(mo)
val sum_cost = cost_invoice - cost_mo
println("difference between costs is $sum_cost")

}
