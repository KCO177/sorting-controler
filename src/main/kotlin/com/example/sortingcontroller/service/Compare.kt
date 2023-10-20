package com.example.sortingcontroller.service

import com.example.sortingcontroller.rest.DB_data_collector

class Compare{

    class Parts {
        val db_collector = DB_data_collector()

        //compare if there are the same part issued in the Inv and MO
        //TODO() všechny záznamy do db .toLower() ukládání přes fuzzy a compare z part dat
        //TODO() celý blok hledani part a podmínky, převody řešit na inputu do db - > čistá db

        fun compare_parts(mo_from_invoice: String): Boolean {
            var result: Boolean = comparePartNumbersIssuedInMoInv(mo_from_invoice)
            if (result == false) {
                result = comparePartNamesIssuedinMoInv(mo_from_invoice)
            }
            println("result of the part match in $mo_from_invoice is $result")
            return result
        }

        fun comparePartNumbersIssuedInMoInv(mo_from_invoice: String): Boolean {
            //check the part numbers - should be different because of different numbering system in plants
            val part_number_inv: String = db_collector.get_part_from_inv(mo_from_invoice, "part_number_invoice")
            val part_number_mo: String = db_collector.get_part_from_mo(mo_from_invoice, "part_number_mission_order")
            //println("part_number_mo: $part_number_mo , part_number_inv $part_number_inv")
            val result: Boolean = compareParts(part_number_inv, part_number_mo)
            //println("result for match part numbers is $result")
            if (result == true) {
                return result
            } else {
                // check if the number match with record in part database

                val part_number_match: String = db_collector.get_part_value(part_number_inv, "part_number")
                val matchedResult: Boolean = compareParts(part_number_match, part_number_mo)
                return matchedResult
            }
        }

        fun comparePartNamesIssuedinMoInv(mo_from_invoice: String): Boolean {
            //check the part names - should be different because of different numbering system in plants
            val part_name_inv: String = db_collector.get_part_from_inv(mo_from_invoice, "part")
            val part_name_mo: String = db_collector.get_part_from_mo(mo_from_invoice, "part")
            val result: Boolean = compareParts(part_name_inv, part_name_mo)
            if (result == true) {
                return result
            } else {
                // check if shortcut or any other record is used return part number and compare
                val inv_part_record_match: String = db_collector.get_part_value(part_name_inv, "part_number")
                val mo_part_record_match: String = db_collector.get_part_value(part_name_mo, "part_number")
                //compare numbers from match
                val matchedResult: Boolean = compareParts(mo_part_record_match, inv_part_record_match)
                return matchedResult
            }
        }

        fun compareParts(partNumberInv: String, partNumberMo: String): Boolean {
            return (partNumberInv == partNumberMo)
        }
    }

    class SortingTime{
        val db_collector = DB_data_collector()
        val calculate = Calculate()


        //fun mainCompareTime(){
        // compare time tacts in MO and Inv
        // 1. compare val in mo if the value is equal to const standard #
        // --> if false recalculate recommended
        // --> use ordered val in next calculations #

        // 2. compare inv and mo
        // --> if false calculated difference for all invoices separately #
        // --> get wrong invoice #
        // --> calculate difference in hours complete invoices X mo_values # in Calculate
        // --> calculate difference in sec/part #

        fun compareSortingTimeInMo(mo:String): Boolean {
            val partMo: String = db_collector.get_part_from_mo(mo, "part")
            val totalHoursMO: Double = db_collector.get_mission_order_value_regarding_mo(mo,"time_tact")
            val amountMo : Int = db_collector.get_mission_order_value_regarding_mo(mo, "amount_mo").toInt()
            val isMoTactOK = compareTimeWithConstTimeTact(partMo, totalHoursMO, amountMo)

            println("comparation of time takt in MO and constant is OK stat:$isMoTactOK")
            return isMoTactOK
        }

        // trigger for next comparation
        fun compareSortingTimeInvMo(mo:String):Boolean {
            val sortingTimeInv: Double = db_collector.get_invoice_value_for_mo(mo, "sorting_time")
            val sortingTimeMo: Double = db_collector.get_mission_order_value_regarding_mo(mo, "time_tact")
            return (sortingTimeInv == sortingTimeMo)
        }

        // compare times in invoices
        fun compareSortingTimeInvMO(mo:String):Map<String, Double> {
            val invoices : MutableList<String> = db_collector.getMissionOrderStringsRegardingMo(
                mo = mo,
                requiredValue = "invoice_number"
            )
            //println("INVOICES: $invoices")

            //values from mo for const result
            var secondDiffPerPart: Double = 0.0
            val part : String = db_collector.get_part_from_mo(mo, "part")
            val timeToSort : Double = db_collector.get_mission_order_value_regarding_mo(mo,"time_tact")
            val amountToSort : Int  = db_collector.get_mission_order_value_regarding_mo(mo, "amount_mo").toInt()
            val const: Boolean = compareTimeWithConstTimeTact(part, timeToSort, amountToSort)

            // for each invoice in list compare the time to sort and convert to the sec/part return sec diff
            val resultInvSec: MutableMap<String, Double> = HashMap()
            for (invoice in invoices){

                val hours = db_collector.getInvoiceValueForInvoice(invoice, "sorting_time")
                val amount: Double = db_collector.getInvoiceValueForInvoice(invoice, "amount_inv")

                // compare sec/part times
                secondDiffPerPart = compareSecondPerPart(part, hours, amount, const, mo)
                resultInvSec[invoice] = secondDiffPerPart
                //println("for invoice $invoice, is the diff in sec/part $secondDiffPerPart s" )



            }
            //println(resultInvSec)
            return resultInvSec // hashmap
        }

        // compare time in mo or inv with const time tact (!time to manipulation included!)
        fun compareTimeWithConstTimeTact(part:String, timeToSort: Double, amountToSort: Int): Boolean {
            val constHoursToSort: Double = calculate.calculateConstSortTime(part)
            val constTimeTact: Double = calculate.calculateConstTimeTact(constHoursToSort)
            val calculatedTimeTact : Double = calculate.calculateSortTimeTact(timeToSort, amountToSort)
            //val timeTactDiff : Double = calculatedTimeTact - constTimeTact
            //println("difference in timetact is $timeTactDiff against the constant $constTimeTact")

            return constTimeTact == calculatedTimeTact
        }

        // compare seconds per part with constant value

        fun compareSecondPerPart(part:String, hours: Double, amount: Double, const: Boolean, mo: String): Double {
            var constSecondPerPart : Double = 0.0
            // if the value of takt time in mission order is the same as standard in part record in db
            if (const) {
                constSecondPerPart = db_collector.get_part_value_to_sort(part, "time_to_sort")
            // otherwise use the value from mission order
            } else {
                constSecondPerPart = db_collector.get_mission_order_value_regarding_mo(mo, "time_tact" )
            }
            val calculatedSecondPerPart: Double = calculate.calculateSecondPerPart(hours, amount)
            return constSecondPerPart - calculatedSecondPerPart
        }
    }
}