package com.example.sortingcontroller.service

import com.example.sortingcontroller.rest.DB_data_collector
import com.example.sortingcontroller.service.output.Calculate
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName

import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class CalculateTest {
    val hours: Double = 2.0
    val amount: Double = 200.0
    val calculator = Calculate()
    val mo = "MO2"
    val part = "GB"


    @Test // test calculate cost difference between mo and invoice
    @DisplayName("Test calculate cost difference between mo and invoice")
    fun testCalculateDiffInvMo(){
        val dbCollector = mock(DB_data_collector::class.java)

        `when`(dbCollector.get_invoice_value_for_mo(mo, "cost_invoice")).thenReturn(100.0)
        `when`(dbCollector.get_mission_order_value_regarding_mo(mo, "cost_mission_order")).thenReturn(50.0)

        // Set the mock DB_data_collector in the calculator
        calculator.dbCollector = dbCollector

        val result: Double = calculator.calculateDiffInvMo(mo)
        assertEquals(50.0, result, "Failed to calculate cost difference!")

    }

    @Test //test calculate amount of part difference
    @DisplayName("Test calculate amount of part difference between mo and invoice")
    fun testCalculateDifferencePartAmount(){

        val dbCollector = mock(DB_data_collector::class.java)

        `when`(dbCollector.get_invoice_value_for_mo(mo, "amount_inv")).thenReturn(100.0)
        `when`(dbCollector.get_mission_order_value_regarding_mo(mo, "amount_mo")).thenReturn(50.0)

        // Set the mock DB_data_collector in the calculator
        calculator.dbCollector = dbCollector

        val result: Double = calculator.calculateDifferencePartAmount(mo)
        assertEquals(50.0, result, "Failed to calculate amount of parts difference!")
    }

    @Test // test calculate constant sorting time for 100 parts get hours value (!TIME TO MANIPULATION INCLUDED!)
    @DisplayName("Test calculate constant sorting time for 100 parts get hours value (!TIME TO MANIPULATION INCLUDED!")
    fun testCalculateConstSortTime(){
        val dbCollector = mock(DB_data_collector::class.java)

        `when`(dbCollector.get_part_value_to_sort(part, "time_to_sort")).thenReturn(15.0)
        `when`(dbCollector.get_part_value_to_sort(part, "time_to_manipulation")).thenReturn(3.0)

        // Set the mock DB_data_collector in the calculator
        calculator.dbCollector = dbCollector

        val result: Double = calculator.calculateConstSortTime(part)
        assertEquals(0.5, result, "Failed to calculate constant time for 100 parts!")


    }

    @Test // test calculate constant sorting time tact
    @DisplayName("Test calculate constant sorting time tact")
    fun testConstTimeTact(){
        val result: Double = calculator.calculateConstTimeTact(hours)
        assertEquals(50.0, result, "Failed to calculate const time tact!")
    }

    @Test // test sorting time from given values
    @DisplayName("Test calculate sorting time from given values")
    fun testSortTimeTact(){
        val result: Double = calculator.calculateSortTimeTact(hours, amount.toInt())
        assertEquals(100.0, result, "Failed to calculate sorting time tact!")
    }

    @Test // test calculate seconds per part from given values
    @DisplayName("Test calculate seconds per part from given values")
    fun testSecondPerPart() {
        val result: Double = calculator.calculateSecondPerPart(hours, amount)
        assertEquals(36.0, result, "Failed to calculate seconds per part!")
    }

    @Test // calculate hours difference in mo and inv
    @DisplayName("Test calculate sorting hours difference difference between mo and invoice")
    fun testCalculateDiffSortingTimeInvMo(){
        val dbCollector = mock(DB_data_collector::class.java)

        `when`(dbCollector.get_invoice_value_for_mo(mo, "sorting_time")).thenReturn(100.0)
        `when`(dbCollector.get_mission_order_value_regarding_mo(mo, "time_tact")).thenReturn(50.0)

        // Set the mock DB_data_collector in the calculator
        calculator.dbCollector = dbCollector

        val result: Double = calculator.calculateDiffSortingTimeInvMo(mo)
        assertEquals(50.0, result, "Failed to calculate sorting times difference in inv and mo!")

    }
}
