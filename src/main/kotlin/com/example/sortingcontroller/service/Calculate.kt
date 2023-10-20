package com.example.sortingcontroller.service

import com.example.sortingcontroller.rest.DB_data_collector

//class to simple base calculation
class Calculate(){

    val db_collector = DB_data_collector()
    val CONST_AMOUNT_TO_SORT : Double = 100.0


    //calculate difference between mo and invoice (mo_from_invoice)
    fun calculateDiffInvMo(mo_from_invoice:String): Double {
        //get values from mo and invoice s regarding mo Id
        val cost_invoice : Double = db_collector.get_invoice_value_for_mo(mo_from_invoice, "cost_invoice")
        val cost_mo : Double = db_collector.get_mission_order_value_regarding_mo(mo_from_invoice, "cost_mission_order")
        // calculate difference
        val sum_cost: Double = cost_invoice - cost_mo
        //println("difference between costs is $sum_cost")
        return sum_cost
    }

    //calculate amount of part difference
    fun calculateDifferencePartAmount(mo_from_invoice: String):Double{
        //get values from mo and invoice s regarding mo Id
        val amount_invoice : Double = db_collector.get_invoice_value_for_mo(mo_from_invoice, "amount_inv")
        val amount_mo : Double = db_collector.get_mission_order_value_regarding_mo(mo_from_invoice, "amount_mo")
        val dif_amount: Double = amount_invoice - amount_mo
        //println("amount difference is $dif_amount in invoice")
        return dif_amount
    }

    //calculate constant sorting time for 100 parts get hours value (!TIME TO MANIPULATION INCLUDED!)
    fun calculateConstSortTime(part : String):Double{

        val timeToSort = db_collector.get_part_value_to_sort(part, "time_to_sort")
        val timeToManipulation = db_collector.get_part_value_to_sort(part, "time_to_manipulation")
        val secToSort : Double = CONST_AMOUNT_TO_SORT * (timeToSort + timeToManipulation)
        val hoursToSort = secToSort/3600
        //println ("for amount of $CONST_AMOUNT_TO_SORT $part mentioned in mo is necessary $hoursToSort hours to sort")
        return hoursToSort
    }

    //calculate constant sorting time tact
    fun calculateConstTimeTact(constHour:Double):Double{
        return CONST_AMOUNT_TO_SORT/constHour
    }

    //calculate sorting time from given values
    fun calculateSortTimeTact(hoursToSort: Double, amountOfParts: Int): Double {
        return amountOfParts / hoursToSort
    }

    //calculate seconds per part from given values
    fun calculateSecondPerPart(hours:Double, amount: Double): Double {
        return hours * 3600/amount
    }

    //calculate hours difference in mo and inv
    fun calculateDiffSortingTimeInvMo(mo:String): Double {
        val sortingTimeInv: Double = db_collector.get_invoice_value_for_mo(mo, "sorting_time")
        val sortingTimeMo: Double = db_collector.get_mission_order_value_regarding_mo(mo, "time_tact")
        return (sortingTimeInv - sortingTimeMo)
    }

}