package com.example.sortingcontroller.service

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Assertions.*

@SpringBootTest
class CalculateTest {

    // Create an instance of the Calculate class for testing
    private val calculate = Calculate()

    @Test
    fun testCalculateSortTime() {
        // TODO ZMENIT CELOU NOVOU MOCK PART
        // Mock data for your test
        val part = "GB"
        val amountOfPart = 10

        // Mock data for the database collector
        // TODO ZMENIT CELOU NOVOU MOCK PART
        val timeToSort = 20
        val timeToManipulation = 1.5

        // Call the function to be tested with the mock data
        val result = calculate.calculateSortTime(part, amountOfPart)

        // Expected result
        val expected = (amountOfPart * (timeToSort + timeToManipulation)) / 3600.0

        // Assert that the result matches the expected value
        assertEquals(expected, result, 0.001) // Use a delta for double comparison
    }
}