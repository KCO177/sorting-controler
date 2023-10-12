package com.example.sortingcontroller.rest
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.*

@Entity
data class MissionOrder (
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id : Long = 0,
    val mission_order_id : String,
    val date : String,
    val cost_mission_order :  Double,
    val part : String,
    val part_number_mission_order : String,
    val amount_mo : Int,
    val time_tact : Double,
    val mission_order_text: String
)

@Entity
data class InvoiceSorting (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,
    val mission_order_id : String,
    val invoice_number : String,
    val date : String,
    val cost_invoice :  Double,
    val part : String,
    val part_number_invoice : String,
    val amount_inv : Int,
    val sorting_time: Double
)

data class ViewMission(
    val mission_order_id : String,
    val date : String,
    val cost_mission_order :  Double,
    val part : String,
    val part_number_mission_order : String,
    val time_tact: Double,
    val amount_mo : Int,

)

data class ViewInvoice(
    val mission_order_id : String,
    val invoice_number : String,
    val date : String,
    val cost_invoice :  Double,
    val part : String,
    val part_number_invoice : String,
    val amount_mo : Int,
    val sorting_time: Double

)

data class CreateMissionOrder(
    val mission_order_id : String,
    val date : String,
    val cost_mission_order :  Double,
    val part : String,
    val part_number_mission_order : String,
    val amount_mo: Int,
    val time_tact: Double,
    val mission_order_text: String
)

data class CreateInvoice(
    val mission_order_id : String,
    val invoice_number : String,
    val date : String,
    val cost_invoice :  Double,
    val part : String,
    val part_number_invoice : String,
    val amount_inv : Int,
    val sorting_time: Double
)

data class UpdateMissionOrder(
    val mission_order_id: String?,
    val date: String?,
    val cost_mission_order: Double?,
    val part: String?,
    val part_number_mission_order: String?,
    val amount_mo: Int?,
    val time_tact: Double?,
    val mission_order_text: String?
)

fun MissionOrder.toView()=
    ViewMission(mission_order_id, date, cost_mission_order, part, part_number_mission_order, time_tact,  amount_mo)

fun InvoiceSorting.toView()=
    ViewInvoice(mission_order_id, invoice_number, date, cost_invoice, part, part_number_invoice, amount_inv, sorting_time)

//base findall mo interface
interface MissionOrderTableRepository: CrudRepository<MissionOrder, Long> {

    // search for current mission order
    @Query(
        "SELECT a FROM MissionOrder a WHERE a.mission_order_id LIKE CONCAT('%', :suffix, '%')"
    )
    fun searchMissionOrderById(@Param("suffix") suffix: String): Iterable<MissionOrder>

    // delete for current mission order
    @Modifying
    @Transactional
    @Query(
        "DELETE FROM MissionOrder a WHERE a.mission_order_id LIKE CONCAT('%', :suffix, '%')"
    )
    fun deleteMissionOrderById(@Param("suffix") suffix: String)
}

// base findall inovice interface
interface InvoiceSortingTableRepository: CrudRepository<InvoiceSorting, Long> {

    // search for invoices for current mission order
    @Query(
        "SELECT a FROM InvoiceSorting a WHERE a.mission_order_id LIKE CONCAT('%', :suffix, '%')"
    )
    fun searchInvoicesForMissionOrder(@Param("suffix") suffix: String): Iterable<InvoiceSorting>
}

//rest controller for mission order
@RestController
@RequestMapping("mo")
class MissionOrderController(
    val missionOrderRepository : MissionOrderTableRepository,
    ){
    @GetMapping("all")
    fun findAll(): Iterable<ViewMission> =
        missionOrderRepository. findAll().map { it.toView()}

    @GetMapping("findmo/{mo}")
    fun findByMissionOrderId(@PathVariable mo: String): Iterable<ViewMission> =
        missionOrderRepository.searchMissionOrderById(mo).map{it.toView()}

    @DeleteMapping("delmo/{mo}")
    fun deleteByMissionOrderId(@PathVariable mo: String): ResponseEntity<String> {
        missionOrderRepository.deleteMissionOrderById(mo)
        return ResponseEntity.ok("Mission Order deleted successfully")
    }

    @PostMapping("post")
    fun create(@RequestBody createMission: CreateMissionOrder) =
        missionOrderRepository.save(
            MissionOrder(
                mission_order_id =createMission.mission_order_id,
                date = createMission.date,
                cost_mission_order = createMission.cost_mission_order,
                part = createMission.part,
                part_number_mission_order=createMission.part_number_mission_order,
                amount_mo=createMission.amount_mo,
                time_tact = createMission.time_tact,
                mission_order_text=createMission.mission_order_text
            )
        ).toView()

    @PutMapping("updatemo/{mo}")
    fun updateMissionOrder(
        @PathVariable mo: String,
        @RequestBody updateMission: UpdateMissionOrder
    ): ResponseEntity<ViewMission> {
        val existingMissionOrders = missionOrderRepository.searchMissionOrderById(mo).toList()

        if (existingMissionOrders.isNotEmpty()) {
            val existingMissionOrder = existingMissionOrders[0]
            val updatedMissionOrder = existingMissionOrder.copy(
                date = updateMission.date ?: existingMissionOrder.date,
                cost_mission_order = updateMission.cost_mission_order ?: existingMissionOrder.cost_mission_order,
                part = updateMission.part ?: existingMissionOrder.part,
                part_number_mission_order = updateMission.part_number_mission_order ?: existingMissionOrder.part_number_mission_order,
                amount_mo = updateMission.amount_mo ?: existingMissionOrder.amount_mo,
                time_tact = updateMission.time_tact ?: existingMissionOrder.time_tact,
                mission_order_text = updateMission.part ?: existingMissionOrder.mission_order_text,
            )

            val savedMissionOrder = missionOrderRepository.save(updatedMissionOrder)
            return ResponseEntity.ok(savedMissionOrder.toView())
        } else {
            return ResponseEntity.notFound().build()
        }
    }




}


//rest controller to get and post invoice
@RestController
@RequestMapping("inv")
class InvoiceSortingController(
    val invoiceRepository : InvoiceSortingTableRepository
) {
    @GetMapping()
    fun findAll(): Iterable<ViewInvoice> =
        invoiceRepository.findAll().map { it.toView() }

    @GetMapping("findinvformo/{mo}")
    fun findByMissionOrderId(@PathVariable mo: String): Iterable<ViewInvoice> =
        invoiceRepository.searchInvoicesForMissionOrder(mo).map { it.toView() }

    @PostMapping("post/inv")
    fun create(@RequestBody createInvoice: CreateInvoice) =
        invoiceRepository.save(  //invoiceSortingRepository
            InvoiceSorting(
                mission_order_id = createInvoice.mission_order_id,
                invoice_number = createInvoice.invoice_number,
                date = createInvoice.date,
                cost_invoice = createInvoice.cost_invoice,
                part = createInvoice.part,
                part_number_invoice = createInvoice.part_number_invoice,
                amount_inv = createInvoice.amount_inv,
                sorting_time = createInvoice.sorting_time
            )
        ).toView()
    }






