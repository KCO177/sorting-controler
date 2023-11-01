package com.example.sortingcontroller.service.output

import com.example.sortingcontroller.rest.DB_data_collector

//class to simple base calculation
class Calculate{

    var dbCollector = DB_data_collector()
    private val cONSTaMOUNTtOsORT : Double = 100.0


    //calculate cost difference between mo and invoice (mo_from_invoice)
    fun calculateDiffInvMo(moFromInvoice: String): Double {
        //get values from mo and invoice s regarding mo ID
        val costInvoice: Double = dbCollector.get_invoice_value_for_mo(moFromInvoice, "cost_invoice")
        val costMo: Double = dbCollector.get_mission_order_value_regarding_mo(moFromInvoice, "cost_mission_order")
        // calculate difference
        //println("difference between costs is $sum_cost")
        return costInvoice - costMo
    }

    //calculate amount of part difference
    fun calculateDifferencePartAmount(moFromInvoice: String): Double {
        //get values from mo and invoice s regarding mo ID
        val amountInvoice: Double = dbCollector.get_invoice_value_for_mo(moFromInvoice, "amount_inv")
        val amountMo: Double = dbCollector.get_mission_order_value_regarding_mo(moFromInvoice, "amount_mo")
        //println("amount difference is $dif_amount in invoice")
        return amountInvoice - amountMo
    }

    //calculate constant sorting time for 100 parts get hours value (!TIME TO MANIPULATION INCLUDED!)
    fun calculateConstSortTime(part: String): Double {

        val timeToSort = dbCollector.get_part_value_to_sort(part, "time_to_sort")
        val timeToManipulation = dbCollector.get_part_value_to_sort(part, "time_to_manipulation")
        val secToSort: Double = cONSTaMOUNTtOsORT * (timeToSort + timeToManipulation)
        //println ("for amount of $CONST_AMOUNT_TO_SORT $part mentioned in mo is necessary $hoursToSort hours to sort")
        return secToSort / 3600
    }

    //calculate constant sorting time tact
    fun calculateConstTimeTact(constHour:Double):Double{
        return cONSTaMOUNTtOsORT/constHour
    }

    //calculate sorting time tact from given values
    fun calculateSortTimeTact(hoursToSort: Double, amountOfParts: Int): Double {
        return amountOfParts / hoursToSort
    }

    //calculate seconds per part from given values
    fun calculateSecondPerPart(hours:Double, amount: Double): Double {
        return hours * 3600/amount
    }

    //calculate hours difference in mo and inv
    fun calculateDiffSortingTimeInvMo(mo:String): Double {
        val sortingTimeInv: Double = dbCollector.get_invoice_value_for_mo(mo, "sorting_time")
        val sortingTimeMo: Double = dbCollector.get_mission_order_value_regarding_mo(mo, "time_tact")
        return (sortingTimeInv - sortingTimeMo)
    }



}