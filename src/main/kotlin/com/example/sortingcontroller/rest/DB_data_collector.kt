package com.example.sortingcontroller.rest
import org.springframework.web.client.RestTemplate

class DB_data_collector {


    // class with functions to get values in needed types from rest endpoints
    // TODO vvtáhnout urlIndex do settings
    val urlIndex = "http://localhost:8080/"


    // get the one Int, Double value from one Invoice regarding invoice Id
    fun getInvoiceValueForInvoice(inv: String, requiredValue: String, defaultValue: Double = 0.0): Double {
        // Call the rest function to find invoice regarding invoiceId
        val restTemplate = RestTemplate()
        val url = "$urlIndex/inv/findinvid/{inv}"
        val response = restTemplate.getForObject(url, List::class.java, inv)

        // Map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (moRecord in response) {
                if (moRecord is Map<*, *>) {
                    val valMo = moRecord[requiredValue]
                    if (valMo is Int) {
                        return valMo.toDouble()
                    }
                }
            }
        }

        return defaultValue  // Return the default value if the required value was not found
    }

    //get Int, Double values from invoices records regarding mo Id
    fun get_invoice_value_for_mo(mo: String, requiredValue: String): Double {
        var totalValInvoice = 0.0
        // call rest function find all invoices to current MO (MO:INV; 1:N)
        val restTemplate = RestTemplate()
        val url = "${urlIndex}inv/findinvformo/{mo}"
        val response = restTemplate.getForObject(url, List::class.java, mo)

        //map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (invRecord in response) {
                if (invRecord is Map<*, *>) {
                    val valInvoice = invRecord[requiredValue]
                    if (valInvoice is Int) {
                        totalValInvoice += valInvoice.toDouble()
                    }
                    if (valInvoice is Double) {
                        totalValInvoice += valInvoice
                    }

                }
            }
        }
        //println("total $requiredValue in invoices for $mo : $totalValInvoice")
        return totalValInvoice
    }

    //get Int, Double values from mission order record regarding mo Id
    fun get_mission_order_value_regarding_mo(mo: String, requiredValue: String): Double {
        var totalValMo = 0.0
        // call rest function find MO regarding moId
        val restTemplate = RestTemplate()
        val url = "$urlIndex/mo/findmo/{mo}"
        val response = restTemplate.getForObject(url, List::class.java, mo)

        //map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (moRecord in response) {
                if (moRecord is Map<*, *>) {
                    val valMo = moRecord[requiredValue]
                    if (valMo is Int) {
                        totalValMo += valMo.toDouble()
                    }
                    if (valMo is Double) {
                        totalValMo += valMo
                    }
                }
            }
        }
        //println("total costs in mo $mo : $totalValMo")

        return totalValMo
    }

    //get String values from mission order record regarding mo Id
    fun getMissionOrderStringsRegardingMo(mo: String, requiredValue: String): MutableList<String> {
        val valuesList = mutableListOf<String>()
        // Call the rest function to find MO regarding moId
        val restTemplate = RestTemplate()
        val url = "$urlIndex/inv/findinvformo/{mo}"
        val response = restTemplate.getForObject(url, List::class.java, mo)

        // Map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (moRecord in response) {
                if (moRecord is Map<*, *>) {
                    val valMo = moRecord[requiredValue]
                    if (valMo is String) {
                        valuesList.add(valMo)
                    }
                }
            }
        }

        return valuesList
    }

    //get String values from mission order record regarding mo Id
    fun get_part_from_inv(mo: String, partValueKind: String): String {
        val restTemplate = RestTemplate()
        val url = "${urlIndex}inv/findinvformo/{mo}"
        val response = restTemplate.getForObject(url, List::class.java, mo)
        var partInvoice: String = ""
        //map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (invRecord in response) {
                if (invRecord is Map<*, *>) {
                    partInvoice = invRecord[partValueKind].toString()

                }
            }
        }
        return partInvoice
    }

    //get String values from mission order record regarding mo Id
    fun get_part_from_mo(mo: String, partValueKind: String): String {
        var partMo: String = ""
        // call rest function find MO regarding moId
        val restTemplate = RestTemplate()
        val url = "$urlIndex/mo/findmo/{mo}"
        val response = restTemplate.getForObject(url, List::class.java, mo)
        //map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (moRecord in response) {
                if (moRecord is Map<*, *>) {
                    partMo = moRecord[partValueKind].toString()
                }
            }
        }
       //println("part val in MO $mo : $partMo")

        return partMo
    }

    //get Int, Double values from part records
    fun get_part_value_to_sort(part: String, partValue: String): Double {
        // call rest function findPart partValue in constructor for time to sort or time to manipulation
        val restTemplate = RestTemplate()
        val url = "$urlIndex/part/{part}"
        val response = restTemplate.getForObject(url, List::class.java, part)
        var partTime: Double = 0.0

        //map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (partRecord in response) {
                if (partRecord is Map<*, *>) {
                    partTime = partRecord[partValue] as Double
                }
            }
        }
        //println("For part $part , $partValue is $partTime")
        return partTime
    }

    //get String values from part records
    fun get_part_value(part: String, partItemKind:String): String {
        var partItem: String = ""
        // call rest function find part regarding part (name, shortcut, number)
        val restTemplate = RestTemplate()
        val url = "$urlIndex/part/{part}"
        val response = restTemplate.getForObject(url, List::class.java, part)
        //map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (partRecord in response) {
                if (partRecord is Map<*, *>) {
                    partItem = partRecord[partItemKind].toString()
                }
            }
        }
        //println("For part $partItemKind is  $part : $partItem")

        return partItem
    }

    //get all mission orders ids to list
    fun get_all_mo_ids():Set<String>{
        val moSet = mutableSetOf<String>()
        // Call the rest function to find MO regarding moId
        val restTemplate = RestTemplate()
        val url = "$urlIndex/mo/all"
        val response = restTemplate.getForObject(url, List::class.java)

        // Map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (moRecord in response) {
                if (moRecord is Map<*, *>) {
                    val valMo = moRecord["mission_order_id"]
                    if (valMo is String) {
                        moSet.add(valMo)
                    }
                }
            }
        }

        return moSet
    }

    //TODO add to tests:
    //get all part values according kind of part value
    fun get_all_part_numbers(partValueKind: String):Set<String>{
        val partNumberSet = mutableSetOf<String>()
        // Call the rest function to find MO regarding moId
        val restTemplate = RestTemplate()
        val url = "$urlIndex/part/all"
        val response = restTemplate.getForObject(url, List::class.java)

        // Map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (partRecord in response) {
                if (partRecord is Map<*, *>) {
                    val valPart = partRecord[partValueKind]
                    if (valPart is String) {
                        partNumberSet.add(valPart)
                    }
                }
            }
        }

        return partNumberSet
    }

    // posting new invoice used by dragNdrop front end functionality
    fun postNewInv(createInvoice: CreateInvoice) {
        val url = "http://localhost:8080/inv/post"

        val restTemplate = RestTemplate()
        val response = restTemplate.postForEntity(url, createInvoice, String::class.java)

        if (response.statusCode.is2xxSuccessful) {
            println("POST request was successful.")
        } else {
            println("POST request failed with status code: ${response.statusCode}")
        }
    }

    // posting new order used by dragNdrop front end functionality
    fun postNewMo(createMissionOrder: CreateMissionOrder) {
        val url = "http://localhost:8080/mo/post"

        val restTemplate = RestTemplate()
        val response = restTemplate.postForEntity(url, createMissionOrder, String::class.java)

        if (response.statusCode.is2xxSuccessful) {
            println("POST request was successful.")
        } else {
            println("POST request failed with status code: ${response.statusCode}")
        }
    }

    // get set of all suppliers in settings table
    fun get_all_suppliers(supplierValue : String):Set<String>{
        val supplierSet = mutableSetOf<String>()
        // Call the rest function to find MO regarding moId
        val restTemplate = RestTemplate()
        val url = "$urlIndex/settings/all"
        val response = restTemplate.getForObject(url, List::class.java)

        // Map the JSON response to values
        if (response != null && response.isNotEmpty()) {
            for (supRecord in response) {
                if (supRecord is Map<*, *>) {
                    val valSup = supRecord[supplierValue]
                    if (true) {
                        supplierSet.add(valSup.toString())
                    }
                }
            }
        }

        return supplierSet
    }

    // get current settings for supplier
    fun getSettings(supplier: String): HashMap<String, String> {
        val restTemplate = RestTemplate()
        val url = "$urlIndex/settings/find/{supplier}"
        val response = restTemplate.getForObject(url, List::class.java, supplier)

        val settingsMap = HashMap<String, String>()

        if (response != null) {
            for (supRecord in response) {
                if (supRecord is Map<*, *>) {
                    @Suppress("UNCHECKED_CAST")
                    val recordMap = supRecord as Map<String, String>
                    // Extract the key-value pairs from the recordMap and add them to the settingsMap
                    settingsMap.putAll(recordMap)
                }
            }
        }

        return settingsMap
    }

}


















