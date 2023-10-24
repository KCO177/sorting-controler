package com.example.sortingcontroller.service

import com.example.sortingcontroller.rest.DB_data_collector
import com.example.sortingcontroller.service.output.Compare

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class CompareTests {
    val comparePartsCl = Compare.Parts()
    val mo: String = "MO2"
    val partShortcut: String = "GB"
    val partNumber: String = "B000"


    @Test
    @DisplayName("Test simple String part matcher")
    fun testCompareParts(){

        val resultF: Boolean = comparePartsCl.matchParts(partNumber, partShortcut)
        Assertions.assertFalse(resultF, "Failed to match part values!")
        val resultT: Boolean = comparePartsCl.matchParts(partNumber, partNumber)
        Assertions.assertTrue(resultT, "Failed to match part values!")
    }

    @Test
    @DisplayName("Test check the part names match - True scenario")
    fun testComparePartNumbersIssuedInMoInvTrue() {
        val dbCollector = Mockito.mock(DB_data_collector::class.java)

        Mockito.`when`(dbCollector.get_part_from_inv(mo, "part")).thenReturn(partShortcut)
        Mockito.`when`(dbCollector.get_part_from_mo(mo, "part")).thenReturn(partShortcut)

        comparePartsCl.db_collector = dbCollector

        val result: Boolean = comparePartsCl.comparePartNamesIssuedinMoInv(mo)
        Assertions.assertTrue(
            result,
            "Failed to compare parts names, values, shortcuts in Inv and MO! - first condition"
        )
    }
}

/*
    @Test
    @DisplayName("Test check the part names match - False scenario")
    fun testComparePartNumbersIssuedInMoInvFalse() {
        val dbCollector = Mockito.mock(DB_data_collector::class.java)

        Mockito.`when`(dbCollector.get_part_from_inv(mo, "part")).thenReturn(partShortcut)
        Mockito.`when`(dbCollector.get_part_from_mo(mo, "part")).thenReturn(partNumber)

        comparePartsCl.db_collector = dbCollector

        val result: Boolean = comparePartsCl.comparePartNamesIssuedinMoInv(mo)
        Assertions.assertTrue(
            result,
            "Failed to compare parts names, values, shortcuts in Inv and MO! - first condition"
        )
    }
}
*/

        /*
        Mockito.`when`(dbCollector.get_part_value(partShortcut, "part_number")).thenReturn(partNumber)
        Mockito.`when`(compareParts.compareParts(Mockito.anyString(), Mockito.anyString())).thenReturn(true)

        val comparePartsCl = Compare.Parts()
        comparePartsCl.db_collector = dbCollector
        comparePartsCl.compare_parts(mo) // Call the compare_parts method with 'mo' parameter

        val result: Boolean = comparePartsCl.comparePartNamesIssuedinMoInv(mo)

        Assertions.assertTrue(result, "Failed to compare parts names, values, shortcuts in Inv and MO! - second condition")
    }
}*/