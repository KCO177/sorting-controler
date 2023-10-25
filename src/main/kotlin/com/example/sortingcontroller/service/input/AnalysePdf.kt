package com.example.sortingcontroller.service.input

import com.example.sortingcontroller.rest.DB_data_collector
import net.sourceforge.lept4j.L_Hashmap
import kotlin.collections.HashMap
import kotlin.collections.MutableList
import kotlin.collections.HashMap as HashMap1
import kotlin.collections.MutableList as MutableList1

class AnalysePdf {
    fun findSupplier(textList: MutableList<String>): Boolean {

        //TODO get list of suppliers
        //TODO set settings for current supplier
        //najdi CPS v listu

        val supplier: String = "CPS QUALITY"
        val daylight: String = "Daylight"
        val supplierIs: Boolean = textList.any { it.contains(supplier) }
        println("supplier in textList $supplierIs ")
        return supplierIs
    }

    fun getPartsHashMap(partDescriptionValueA: String, partDescriptionValueB: String): HashMap<String, String> {
        val dbDataCollector = DB_data_collector()

        // call data from DB
        val setOfPartNumbers = dbDataCollector.get_all_part_numbers(partDescriptionValueA)
        val setOfPartNames = dbDataCollector.get_all_part_numbers(partDescriptionValueB)
        // check the same length of sets
        if (setOfPartNumbers.size != setOfPartNames.size) {
            throw IllegalArgumentException("The sets have different sizes.")
        }

        // create hashmap
        val partNumberToNameMap = HashMap1<String, String>()
        val partNumberIterator = setOfPartNumbers.iterator()
        val partNameIterator = setOfPartNames.iterator()

        while (partNumberIterator.hasNext() && partNameIterator.hasNext()) {
            val partNumber = partNumberIterator.next()
            val partName = partNameIterator.next()
            partNumberToNameMap[partNumber] = partName
        }
        return partNumberToNameMap
    }

    fun checkTheHashmap(
        givenPartMap: HashMap<String, String>,
        textList: MutableList<String>,
        partDescriptionValueA: String,
        partDescriptionValueB: String
    ): HashMap<String, String> {

        // Check if items in textList are present in the HashMap return hashmap part_number : part_name
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

    fun findPart(textList: MutableList<String>): HashMap<String, String> {
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
                // switch key and value

                return partMapResult // //part_name : part_number
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


    fun findItemsInv(textList: MutableList<String>): HashMap1<String, String> {

        val partNumber: String = findPart(textList).values.first()

        // TODO add vals to supplier settings
        val invoiceIdPattern = "Invoice n° (\\d+)".toRegex()
        val orderIdPattern = "Mission order : (\\d+)".toRegex()
        val weekPattern = "Date : WEEK (\\d+)".toRegex()
        val totalCostPattern = "Total inc VAT (\\d+)".toRegex()

        val invoiceIdMatch = invoiceIdPattern.find(textList.toString())
        val orderIdMatch = orderIdPattern.find(textList.toString())
        val weekMatch = weekPattern.find(textList.toString())
        val costMatch = totalCostPattern.find(textList.toString())

        val invoiceId = invoiceIdMatch?.groupValues?.get(1)
        val orderId = orderIdMatch?.groupValues?.get(1)
        val week = weekMatch?.groupValues?.get(1)
        val cost = costMatch?.groupValues?.get(1)

        val exportHashMap = HashMap1<String, String>()

        if (invoiceId != null) {
            exportHashMap["InvoiceId"] = invoiceId
        }
        if (orderId != null) {
            exportHashMap["MissionOrderId"] = orderId
        }

        if (week != null) {
            exportHashMap["Week"] = week
        }
        if (cost != null) {
            exportHashMap["TotalCost"] = cost
        }

        if (partNumber != null) {
            exportHashMap["part_number"] = partNumber
        }
        println(exportHashMap)



        return exportHashMap
    }
}


