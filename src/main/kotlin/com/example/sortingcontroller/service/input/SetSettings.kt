package com.example.sortingcontroller.service.input

import com.example.sortingcontroller.rest.DB_data_collector
import me.xdrop.fuzzywuzzy.FuzzySearch

class SetSettings {
    private val dbDataCollector = DB_data_collector()


    // check if pdf is an invoice
    fun factureInFileName(fileName: String): Boolean {
        return fileName.contains("Facture")
        //TODO get values for check from db
    }

    // find if there is any supplier in given text list - trigger to analyse proceeding
    fun supplierExists(textList: List<String>, inv: Boolean):Boolean {
        //get list of existing suppliers
        /*val listOfSuppliers = dbDataCollector.get_all_suppliers()
        //TODO get values for check from prim, inv and mo fields

        for (item: String in listOfSuppliers) {
            if (textList.contains(item)) {
                return true
            }
        }
        return false*/
        return true
    }


    // find name of supplier in given text list  - needed to set settings
    fun findSupplier(textList: List<String>, isInv:Boolean): String {
        /*
        //get list of existing suppliers
        val listOfSuppliers: Set<String>
        listOfSuppliers = if (isInv) {
            dbDataCollector.get_all_suppliers("supplier_inv")
        }else{
            dbDataCollector.get_all_suppliers("supplier_code_mo")
        }

        println("listOfSuppliers $listOfSuppliers")
        for (item: String in listOfSuppliers) {
            if (textList.contains(item)) {
                return item}
        }
        return "no supplier found"
        */
        return "cps"
    }

    fun getSettings(supplier: String): HashMap<String, String> {
        return dbDataCollector.getSettings(supplier)
    }

}