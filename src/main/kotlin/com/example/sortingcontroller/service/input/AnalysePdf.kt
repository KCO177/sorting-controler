package com.example.sortingcontroller.service.input

import com.example.sortingcontroller.rest.CreateInvoice
import com.example.sortingcontroller.rest.CreateMissionOrder
import com.example.sortingcontroller.rest.DB_data_collector
import kotlin.collections.HashMap


class AnalysePdf {
    private val dbDataCollector = DB_data_collector()

    // get part hashMap for checking, from db part values
    private fun getPartsHashMap(partDescriptionValueA: String, partDescriptionValueB: String): HashMap<String, String> {


                // call data from DB
                val setOfPartNumbers = dbDataCollector.get_all_part_numbers(partDescriptionValueA)
                val setOfPartNames = dbDataCollector.get_all_part_numbers(partDescriptionValueB)
                // check the same length of sets
                if (setOfPartNumbers.size != setOfPartNames.size) {
                    throw IllegalArgumentException("The sets have different sizes.")
                }

                // create hashmap
                val partNumberToNameMap = HashMap<String, String>()
                val partNumberIterator = setOfPartNumbers.iterator()
                val partNameIterator = setOfPartNames.iterator()

                while (partNumberIterator.hasNext() && partNameIterator.hasNext()) {
                    val partNumber = partNumberIterator.next()
                    val partName = partNameIterator.next()
                    partNumberToNameMap[partNumber] = partName
                }

                return partNumberToNameMap
            }

    // Check if items in textList are present in the HashMap return hashmap part_number : part_name
    private fun checkTheHashmap(givenPartMap: HashMap<String, String>, textList: List<String>, partDescriptionValueA: String, partDescriptionValueB: String): HashMap<String, String>
    {
        val resultPartMap: HashMap<String, String> = HashMap()

        for (item in textList) {
            if (givenPartMap.containsKey(item)) {
                println("Key found: $item")
                resultPartMap[partDescriptionValueA] = item

                val value = givenPartMap[item]
                if (value != null) {
                    println("Value: $value")
                    resultPartMap[partDescriptionValueB] = value
                }
            }

        }
        return resultPartMap
    }

    // Switch key and values in hashmap
    private fun <K, V> switchKeysAndValues(originalMap: Map<K, V>): Map<V, K> {
        val switchedMap = mutableMapOf<V, K>()
        for ((key, value) in originalMap) {
            switchedMap[value] = key
        }
        return switchedMap
    }

    // find part according the "part_number", "part_name", "part_shortcut", "part_number_customer"
    private fun findPart(textList: List<String>): Map<String, String> {
        // TODO get part values list from DB to hold integrity
        val partValuesList = listOf("part_number", "part_name", "part_shortcut", "part_number_customer")
        val dbPartHashMap = getPartsHashMap(partValuesList[0], partValuesList[1])


        var partMapResult = checkTheHashmap(
            givenPartMap = dbPartHashMap,
            textList = textList,
            partDescriptionValueA = partValuesList[0],
            partDescriptionValueB = partValuesList[1]
        )
        if (partMapResult.isNotEmpty()) {
            return partMapResult // part_number : part_name
        } else {
            partMapResult = checkTheHashmap(
                givenPartMap = dbPartHashMap,
                textList = textList,
                partDescriptionValueA = partValuesList[1],
                partDescriptionValueB = partValuesList[0]
            )
            if (partMapResult.isNotEmpty()) {
                //part_name : part_number
                // switch key and value
                return switchKeysAndValues(partMapResult)
            } else {
                partMapResult = checkTheHashmap(
                    givenPartMap = dbPartHashMap,
                    textList = textList,
                    partDescriptionValueA = partValuesList[2],
                    partDescriptionValueB = partValuesList[0]
                )
                if (partMapResult.isNotEmpty()) {
                    return partMapResult // //part_shortcut : part_number
                } else {
                    partMapResult = checkTheHashmap(
                        givenPartMap = dbPartHashMap,
                        textList = textList,
                        partDescriptionValueA = partValuesList[3],
                        partDescriptionValueB = partValuesList[0]
                    )
                    if (partMapResult.isNotEmpty()) {
                        return partMapResult
                    } // //part_number_customer : part_number
                }
            }
        }
        return partMapResult
    }

    // improve digit format after € if is in format X,XXX.XX to XXXX.XX
    fun processEuroStrings(textList: List<String>): List<String> {
        val modifiedList = mutableListOf<String>()

        for (inputText in textList) {
            val modifiedText = inputText.replace(Regex("€(\\d+),(\\d+\\.\\d+)"), "€$1$2")
            modifiedList.add(modifiedText)
        }
        println("modifiedList $modifiedList")
        return modifiedList
    }

    // ###main collector of function###
    // find items in invoice
    fun findItemsInv(textList: List<String>, supplierSettings: Map<String, String>): CreateInvoice {
        println(supplierSettings)
        //get key values for current supplier settings
        val missionOrderIdKey = supplierSettings["mission_order_id_inv"].toString()
        val invoiceIdKey: String = supplierSettings["invoice_number_inv"].toString()
        val dateKey: String = supplierSettings["date_inv"].toString()
        val totalCostKey = supplierSettings["cost_invoice_inv"].toString()

        val patterns = mapOf(

            "$missionOrderIdKey (\\w+)".toRegex() to "mission_order_id",
            "$invoiceIdKey (\\d+)".toRegex() to "invoice_number",
            "$dateKey (\\d+)".toRegex() to "date",
            "$totalCostKey (\\d+)".toRegex() to "cost_invoice"
        )
        // init hashmap to export
        val exportHashMap = mutableMapOf<String, String>()
        // fill up the items find with regex
        for ((pattern, key) in patterns) {
            pattern.find(textList.toString())?.groupValues?.get(1)?.let { value ->
                exportHashMap[key] = value
            }
        }
        // find part number
        var partNumber = "not found"
        val partNumberFind = findPart(textList)
        if (partNumberFind.isNotEmpty()) {
            partNumber = partNumberFind.values.first().toString()
        }
        // find part name
        var partName = "not found"
        if (partNumber != "not found") {
            partName = dbDataCollector.get_part_value(partNumber, "part_name")
        }
        // add part item values into the hashmap
        exportHashMap["part"] = partName
        exportHashMap["part_number_invoice"] = partNumber
        println(exportHashMap)
        //return exportHashMap
        return CreateInvoice(
            mission_order_id = exportHashMap["mission_order_id"] ?: "",
            invoice_number = exportHashMap["invoice_number"] ?: "",
            date = exportHashMap["date"] ?: "",
            cost_invoice = exportHashMap["cost_invoice"]?.toDouble() ?: 0.0,
            part = exportHashMap["part"] ?: "",
            part_number_invoice = exportHashMap["part_number_invoice"] ?: "",
            amount_inv = 1080,  // TODO default amount
            sorting_time = 22.00  // TODO default sorting time
        )
    }

    // find items in orders
    fun findItemsMo(textList: List<String>, supplierSettings: HashMap<String, String>): CreateMissionOrder {
        //musí být MO

        //get key values for current supplier settings
        val missionOrderIdKey = supplierSettings["mission_order_id_mo"].toString() //"rae mission order no "
        val dateKey: String = supplierSettings["date_mo"].toString()
        val totalCostKey = supplierSettings["cost_mision_order_mo"].toString() // "in the amount of €"
        val partKey: String = supplierSettings["part_mo"].toString() //"designation: "
        val partNumberKey: String = supplierSettings["part_number_mission_order"].toString()
        val amountMoKey : String = supplierSettings["amount_mo"].toString()
        val timeTactKey : String = supplierSettings["sorting_time_mo"].toString()



        val patterns = mapOf(

            "$missionOrderIdKey(\\w+)".toRegex() to "mission_order_id",
            "$dateKey(\\w+)".toRegex() to "date",
            "$totalCostKey(\\d+\\.\\d{2})".toRegex() to "cost_mission_order",
            "$partKey(\\w+[^,]+)".toRegex() to "part",
            "$partNumberKey(\\w+)".toRegex() to "part_number_mission_order",
            "$amountMoKey(\\d+)".toRegex() to "amount_mo",
            "$timeTactKey(\\d+)".toRegex() to "time_tact"
        )
        // init hashmap to export
        val exportHashMap = mutableMapOf<String, String>()
        // fill up the items find with regex
        for ((pattern, key) in patterns) {
            pattern.find(textList.toString())?.groupValues?.get(1)?.let { value ->
                exportHashMap[key] = value
            }
        }
        println("exportHashMap $exportHashMap")
        //return exportHashMap
        return CreateMissionOrder(
            mission_order_id = exportHashMap["mission_order_id"] ?: "",
            date = exportHashMap["date"] ?: "",
            cost_mission_order = exportHashMap["cost_mission_order"]?.toDouble() ?: 0.0,
            part = exportHashMap["part"] ?: "",
            part_number_mission_order = exportHashMap["part_number_mission_order"] ?: "",
            amount_mo = exportHashMap["amount_mo"]?.toInt() ?: 0,
            time_tact = exportHashMap["time_tact"]?.toDouble() ?: 0.0)

    }
}






