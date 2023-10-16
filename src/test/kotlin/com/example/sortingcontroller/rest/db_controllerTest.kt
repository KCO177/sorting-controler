package com.example.sortingcontroller.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.http.MediaType
import org.junit.jupiter.api.Assertions.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class db_controllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var missionOrderRepository: MissionOrderTableRepository

    val mockMo : String = "MO123"

    //After finish all test delete mock mockMo value from the database
    @AfterAll
    fun clearDatabase() {
        // Code to delete mission orders after all test methods have run
        missionOrderRepository.deleteMissionOrderById(mockMo)
    }

    //Test the findAll() endpoint
    //Find all mission orders in database and get all in JSON format
    @Test
    fun testFindAllMissionOrders() {
        mockMvc.perform(MockMvcRequestBuilders.get("/mo/all"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        // Add more assertions as needed to validate the response
    }

    // Test the "create" endpoint
    // Create mission order with mock mission order ID
    @Test
    fun testCreateMissionOrder() {
        val createMissionJson = """{
            "mission_order_id": "$mockMo",
            "date": "2023-10-10",
            "cost_mission_order": 100.0,
            "part": "Sample Part",
            "part_number_mission_order": "12345",
            "amount_mo": 5,
            "time_tact": 2.5,
            "mission_order_text": "Sample Mission Order Text"
        }"""

        mockMvc.perform(
            MockMvcRequestBuilders.post("/mo/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createMissionJson)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    // Test the "FindById" endpoint
    // Find the mock mission order by mock ID
    @Test
    fun testFindByMissionOrderId() {
        val missionOrderController = MissionOrderController(missionOrderRepository)
        val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(missionOrderController).build() // Replace yourController with your actual controller

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/mo/findmo/$mockMo")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val content = result.response.contentAsByteArray

        // Use Jackson to parse the content
        val objectMapper = ObjectMapper()
        val missionOrders: List<MissionOrder> = objectMapper.readValue(content)
        val missionOrder = missionOrders.firstOrNull()

        // Test that the db contain the mock id value
        val missionOrderId = missionOrder?.mission_order_id
        assert(missionOrder != null)
        assert(missionOrderId == mockMo)

        // Test that the amount of part is whole number Int (there is no 0.x of parts)
        val missionOrderAmount = missionOrder?.amount_mo
        assert(missionOrder != null)
        assert(value = missionOrderAmount is Int)

        // Test that the timetact is in Double type
        assert(missionOrder != null)
        assert(missionOrder?.time_tact is Double)
        assert(missionOrder?.time_tact == 2.5)// Replace with the expected value

    }

}

@SpringBootTest
@AutoConfigureMockMvc
class InvoiceSortingControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    /* // PŘEDĚLAT PRO INVOICE*/

    @Autowired
    private lateinit var missionOrderRepository: MissionOrderTableRepository // Replace MissionOrderRepository with your actual repository

    @AfterEach
    fun clearDatabase() {
        missionOrderRepository.deleteMissionOrderById("MO123")
    }

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





