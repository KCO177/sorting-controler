package com.example.sortingcontroller.rest

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.http.MediaType

@SpringBootTest
@AutoConfigureMockMvc
class db_controllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var missionOrderRepository: MissionOrderTableRepository // Replace MissionOrderRepository with your actual repository

    @AfterEach
    fun clearDatabase() {
        missionOrderRepository.deleteMissionOrderById("MO123")
    }

    //Test the findAll() endpoint
    @Test
    fun testFindAllMissionOrders() {
        mockMvc.perform(MockMvcRequestBuilders.get("/mo/all"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        // Add more assertions as needed to validate the response
    }


    // Test the "create" endpoint
    @Test
    fun testCreateMissionOrder() {
        val createMissionJson = """{
            "mission_order_id": "MO123",
            "date": "2023-10-10",
            "cost_mission_order": 100.0,
            "part": "Sample Part",
            "part_number_mission_order": "12345",
            "amount_mo": 5,
            "time_tact": 2.5,
            "mission_order_text": "Sample Mission Order Text"
        }"""

        mockMvc.perform(MockMvcRequestBuilders.post("/mo/post")
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMissionJson))
            .andExpect(MockMvcResultMatchers.status().isOk)



        // Add more assertions as needed to validate the response

        // Optionally, you can query the database or repository to verify the saved data
        // For example: assert that the created mission order exists in the repository
    }
}

@SpringBootTest
@AutoConfigureMockMvc
class InvoiceSortingControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    /* // PŘEDĚLAT PRO INVOICE
    @Autowired
    private lateinit var missionOrderRepository: MissionOrderTableRepository // Replace MissionOrderRepository with your actual repository

    @AfterEach
    fun clearDatabase() {
        missionOrderRepository.deleteMissionOrderById("MO123")
    }
   */

    // Test the "findAll" endpoint for InvoiceSortingController
    @Test
    fun testFindAllInvoiceSorting() {
        mockMvc.perform(MockMvcRequestBuilders.get("/inv"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        // Add more assertions as needed to validate the response
    }

    // Test the "create" endpoint for InvoiceSortingController
    @Test
    fun testCreateInvoiceSorting() {
        val createInvoiceJson = """{
            "mission_order_id": "MO123",
            "invoice_number": "INV456",
            "date": "2023-10-15",
            "cost_invoice": 75.0,
            "part": "Sample Part",
            "part_number_invoice": "67890",
            "amount_inv": 3,
            "sorting_time": 1.8
        }"""

        mockMvc.perform(MockMvcRequestBuilders.post("/inv/post")
            .contentType(MediaType.APPLICATION_JSON)
            .content(createInvoiceJson))
            .andExpect(MockMvcResultMatchers.status().isOk)
        // Add more assertions as needed to validate the response

        // Optionally, you can query the database or repository to verify the saved data
        // For example: assert that the created invoice sorting exists in the repository
    }
}
