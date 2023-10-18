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

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)

// TODO nesting tests to necessary ordered and default ordered
class db_controllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var missionOrderRepository: MissionOrderTableRepository

    @Autowired
    private lateinit var invoiceRepository: InvoiceSortingTableRepository

    val mockMo: String = "MO123"
    val mockInv01: String = "INV123"
    val mockInv02: String = "INV124"
    val mockInv03: String = "INV125"
    val mockSortingTime : Double = 8.5
    val mockPartNumber: String = "Part123"

    @Test
    @Order(1)
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

    @Test
    @Order(2)
    fun testFindAllMissionOrders() {
        mockMvc.perform(MockMvcRequestBuilders.get("/mo/all"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        // Add more assertions as needed to validate the response
    }

    @Test
    @Order(3)
    fun testFindByMissionOrderId() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/mo/findmo/$mockMo")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val content = result.response.contentAsByteArray

        val objectMapper = ObjectMapper()
        val missionOrders: List<MissionOrder> = objectMapper.readValue(content)
        val missionOrder = missionOrders.firstOrNull()

        assertNotNull(missionOrder)
        assertEquals(mockMo, missionOrder?.mission_order_id)
        assertNotNull(missionOrder?.amount_mo)
        assertTrue(missionOrder?.amount_mo is Int)
        assertTrue(missionOrder?.cost_mission_order is Double)
        assertEquals(2.5, missionOrder?.time_tact)


    }

    @Test
    @Order(4)
    fun testCreateInvoiceSorting() {
        val createInvoiceJson01 = """{
        "mission_order_id": "$mockMo",
        "invoice_number": "$mockInv01",
        "date": "2023-10-15",
        "cost_invoice": 75.0,
        "part": "Sample Part",
        "part_number_invoice": "$mockPartNumber",
        "amount_inv": 3,
        "sorting_time": $mockSortingTime
    }"""

        val createInvoiceJson02 = """{
        "mission_order_id": "$mockMo",
        "invoice_number": "$mockInv02",
        "date": "2023-10-15",
        "cost_invoice": 75.0,
        "part": "Sample Part",
        "part_number_invoice": "67890",
        "amount_inv": 3,
        "sorting_time": 1.8
    }"""

        val createInvoiceJson03 = """{
        "mission_order_id": "$mockMo",
        "invoice_number": "$mockInv03",
        "date": "2023-10-15",
        "cost_invoice": 75.0,
        "part": "Sample Part",
        "part_number_invoice": "67890",
        "amount_inv": 3,
        "sorting_time": 1.8
    }"""

        mockMvc.perform(
            MockMvcRequestBuilders.post("/inv/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createInvoiceJson01)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)


        mockMvc.perform(
            MockMvcRequestBuilders.post("/inv/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createInvoiceJson02)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/inv/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createInvoiceJson03)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Order(5)
    fun testFindAllInvoices() {
        mockMvc.perform(MockMvcRequestBuilders.get("/inv/all"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))

    }

    @Test
    @Order(6)
    fun testFindInvoicesForMissionOrder() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/inv/findinvformo/$mockMo")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val content = result.response.contentAsString // Read the content as a String

        // Assert that there are three JSON objects in the response
        // Read all invoices for one mo
        // Count the number of JSON objects (open curly braces) in the response
        val numberOfJsonObjects = content.count { it == '{' }

        // Assert that there are three JSON objects in the response
        assertEquals(3, numberOfJsonObjects)

    }

    @Test
    @Order(7)
    fun testFindInvoicesById() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/inv/findinvid/$mockInv01")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val content = result.response.contentAsString // Read the content as a String

        val objectMapper = ObjectMapper()
        val invoices: List<InvoiceSorting> = objectMapper.readValue(content)
        val invoiceRecord: InvoiceSorting? = invoices.firstOrNull()


        assertNotNull(invoiceRecord)
        // amount_inv is Int
        // part_number_invoice is "Part123"
        // sorting_time is
        assertEquals(mockMo, invoiceRecord?.mission_order_id)
        assertNotNull(invoiceRecord?.amount_inv)
        assertTrue(invoiceRecord?.amount_inv is Int)
        assertTrue(invoiceRecord?.cost_invoice is Double)
        assertEquals(mockPartNumber, invoiceRecord?.part_number_invoice)
        assertEquals(mockSortingTime, invoiceRecord?.sorting_time)

    }

    // After all tests are finished, delete mock data from the database
    @AfterAll
    fun clearDatabase() {
        missionOrderRepository.deleteMissionOrderById(mockMo)
        invoiceRepository.deleteInvoiceById(mockInv01)
        invoiceRepository.deleteInvoiceById(mockInv02)
        invoiceRepository.deleteInvoiceById(mockInv03)
        }
}



