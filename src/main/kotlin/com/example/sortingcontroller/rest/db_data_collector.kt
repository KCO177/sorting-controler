package com.example.sortingcontroller.rest
import org.springframework.web.client.RestTemplate

class db_data_collector {


    fun get_invoice_costs_for_mo(mo:String):Double {
        var totalCostInvoice = 0.0
        // call rest function find all invoices to current MO (MO:INV; 1:N)
        val restTemplate = RestTemplate()
        val url = "http://localhost:8080/inv/findinvformo/{mo}"
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


    fun get_mission_order_costs_for_mo(mo:String):Double {
        var totalCostMo = 0.0
        // call rest function find all invoices to current MO (MO:INV; 1:N)
        val restTemplate = RestTemplate()
        val url = "http://localhost:8080/mo/findmo/{mo}"
        val response = restTemplate.getForObject(url, List::class.java, mo)

        //map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (moRecord in response) {
                if (moRecord is Map<*, *>) {
                    val costMo = moRecord["cost_mission_order"]
                    if (costMo is Double) {

                        totalCostMo += costMo
                    }
                }
            }
            println("total costs in MO for $mo: $totalCostMo")

        }
        return totalCostMo
    }
}







