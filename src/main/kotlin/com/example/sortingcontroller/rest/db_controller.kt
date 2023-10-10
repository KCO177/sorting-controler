package com.example.sortingcontroller.rest
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
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

fun MissionOrder.toView()=
    ViewMission(mission_order_id, date, cost_mission_order, part, part_number_mission_order, time_tact,  amount_mo)

fun InvoiceSorting.toView()=
    ViewInvoice(mission_order_id, invoice_number, date, cost_invoice, part, part_number_invoice, amount_inv, sorting_time)


interface MissionOrderTableRepository: CrudRepository<MissionOrder, Long> {
}

// search for current mission order
interface MissionOrderCurrentRepository : CrudRepository<MissionOrder, Long> {
    @Query(
        "SELECT a FROM MissionOrder a WHERE a.mission_order_id LIKE CONCAT('%', :suffix, '%')"
    )
    fun search(@Param("suffix") suffix: String): Iterable<MissionOrder>
}

// search for invoices for current mission order
interface InvoiceforMissionOrderRepository : CrudRepository<InvoiceSorting, Long> {
    @Query(
        "SELECT a FROM InvoiceSorting a WHERE a.mission_order_id LIKE CONCAT('%', :suffix, '%')"
    )
    fun search(@Param("suffix") suffix: String): Iterable<InvoiceSorting>
}


interface InvoiceSortingTableRepository: CrudRepository<InvoiceSorting, Long> {
}

//base rest controller for get and post mission order
@RestController
@RequestMapping("mo")
class MissionOrderController(
    val missionOrderRepository : MissionOrderTableRepository,
    ){
    @GetMapping("all")
    fun findAll(): Iterable<ViewMission> =
        missionOrderRepository.findAll().map { it.toView()}

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
}
//rest controller to find current mission order
@RestController
@RequestMapping("findmo")
class CurrentMissionOrderController(
    val missionOrderCurrentRepository: MissionOrderCurrentRepository
) {
    @GetMapping("/{mo}")
    fun findByMissionOrderId(@PathVariable mo: String): Iterable<ViewMission> =
        missionOrderCurrentRepository.search(mo).map{it.toView()}
    }

// rest controller to get invoices for current mission order
@RestController
@RequestMapping("findinvformo")
class InvoiceMissionOrderController(
    val invoiceMissionOrder: InvoiceforMissionOrderRepository
) {
    @GetMapping("/{mo}")
    fun findByMissionOrderId(@PathVariable mo: String): Iterable<ViewInvoice> =
        invoiceMissionOrder.search(mo).map{it.toView()}
}


//base rest controller to get and post invoice
@RestController
@RequestMapping("inv")
class InvoiceSortingController(
    val invoiceSortingRepository: InvoiceSortingTableRepository
) {
    @GetMapping()
    fun findAll(): Iterable<ViewInvoice> =
        invoiceSortingRepository.findAll().map { it.toView() }

    @PostMapping("post")
    fun create(@RequestBody createInvoice: CreateInvoice) =
        invoiceSortingRepository.save(
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



