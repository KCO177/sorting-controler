package com.example.sortingcontroller.service

import com.example.sortingcontroller.rest.db_data_collector

//class to simple base calculation
class Calculate(){



    val db_collector = db_data_collector()

    //calculate difference between mo and invoice (mo_from_invoice)
    fun calculateDiffInvMo(mo_from_invoice:String): Double {
        val cost_invoice : Double = db_collector.get_invoice_costs_for_mo(mo_from_invoice)
        val cost_mo : Double = db_collector.get_mission_order_regarding_mo(mo_from_invoice)
        val sum_cost: Double = cost_invoice - cost_mo
        println("difference between costs is $sum_cost")
        return sum_cost
    }

    //calculate sorting tact from part and amount
    fun calculateSortTime(part : String, amountOfPart : Int):Double{
        val timeToSort = db_collector.get_part_value_to_sort(part, "time_to_sort")
        val timeToManipulation = db_collector.get_part_value_to_sort(part, "time_to_manipulation")
        val secToSort : Double = amountOfPart * (timeToSort + timeToManipulation)
        val hoursToSort = secToSort/3600
    println ("for amount of $amountOfPart $part mentioned in mo is necessary $hoursToSort hours to sort")
        return secToSort
    }

}