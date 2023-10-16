package com.example.sortingcontroller.calculation

import com.example.sortingcontroller.rest.db_data_collector

//class to simple base calculation
class Calculate {
    val db_collector = db_data_collector()

    //calculate difference between mo and invoice (mo_from_invoice)
    fun calculateDiffInvMo(mo_from_invoice:String){
        val cost_invoice : Double = db_collector.get_invoice_costs_for_mo(mo_from_invoice)
        val cost_mo : Double = db_collector.get_mission_order_regarding_mo(mo_from_invoice, "cost_mission_order")
        val sum_cost = cost_invoice - cost_mo
        println("difference between costs is $sum_cost")
    }

    //calculate sorting tact from part and amount
    fun calculateSortTact(part : String){
        val timeToSort = db_collector.get_part_value_to_sort(part, "time_to_sort")
        val timeToManipulation = db_collector.get_part_value_to_sort(part, "time_to_manipulation")
        val amountOfPart = 100

        //val amountOfPartMo = db_collector.get_mission_order_regarding_mo(mo, "amount_mo")
        val sec : Double = amountOfPart * (timeToSort + timeToManipulation)
        val hours = sec/3600
    println ("for amount of $amountOfPart $part mentioned in mo is necessary $hours hours to sort")
    }

}