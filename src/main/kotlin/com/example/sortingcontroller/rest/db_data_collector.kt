package com.example.sortingcontroller.rest
import org.springframework.web.client.RestTemplate

class db_data_collector() {
    // TODO vvtáhnout urlIndex do settings
    val urlIndex = "http://localhost:8080/"

    fun get_invoice_costs_for_mo(mo: String): Double {
        var totalCostInvoice = 0.0
        // call rest function find all invoices to current MO (MO:INV; 1:N)
        val restTemplate = RestTemplate()
        val url = "${urlIndex}inv/findinvformo/{mo}"
        val response = restTemplate.getForObject(url, List::class.java, mo)

        //map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (invRecord in response) {
                if (invRecord is Map<*, *>) {
                    val costInvoice = invRecord["cost_invoice"]
                    if (costInvoice is Double) {
                        totalCostInvoice += costInvoice

                    }
                }
            }
            println("total costs in invoices for $mo : $totalCostInvoice")
        }
        return totalCostInvoice
    }


    fun get_mission_order_regarding_mo(mo: String, missionValue:String): Double {
        var totalValMo = 0.0
        // call rest function find MO regarding moId
        val restTemplate = RestTemplate()
        val url = "$urlIndex/mo/findmo/{mo}"
        val response = restTemplate.getForObject(url, List::class.java, mo)

        //map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (moRecord in response) {
                if (moRecord is Map<*, *>) {
                    val valMo = moRecord[{missionValue}]
                    if (valMo is Int){
                        valMo.toDouble()}
                    if (valMo is Double) {
                        totalValMo += valMo
                    }
                }
            }
        }
        return totalValMo
    }

    fun get_part_value_to_sort(part: String, partValue: String): Double {
        // call rest function findPart partValue in constructor for time to sort or time to manipulation
        val restTemplate = RestTemplate()
        val url = "$urlIndex/part/{part}"
        val response = restTemplate.getForObject(url, List::class.java, part)
        var partTime :Double = 0.0

        //map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (partRecord in response) {
                if (partRecord is Map<*, *>) {
                    partTime = partRecord[partValue] as Double
                }
            }
        }
        println("For part $part , $partValue is $partTime" )
        return partTime
    }



}






