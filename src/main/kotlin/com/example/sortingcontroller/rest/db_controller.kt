package com.example.sortingcontroller.rest
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.repository.CrudRepository
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
//   fun findMissionOrderId(mo: String): List<MissionOrder>


interface InvoiceSortingTableRepository: CrudRepository<InvoiceSorting, Long> {
}

@RestController
@RequestMapping("mo")
class MissionOrderController(
    val missionOrderRepository : MissionOrderTableRepository
    ){
    @GetMapping("all")
    fun findAll(): Iterable<ViewMission> =
        missionOrderRepository.findAll().map { it.toView()}
/*
    @GetMapping("/{mo}")
    fun findByMissionOrderId(@PathVariable mo: String): ViewMission? {
        val missionOrder = missionOrderRepository.findMissionOrderId(mo).firstOrNull()
        return missionOrder?.toView()
    }
*/
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
@RestController
@RequestMapping("inv")
class InvoiceSortingController(
    val invoiceSortingRepository : InvoiceSortingTableRepository
){
    @GetMapping()
    fun findAll(): Iterable<ViewInvoice> =
        invoiceSortingRepository.findAll().map { it.toView()}

    @PostMapping("post")
    fun create(@RequestBody createInvoice: CreateInvoice) =
        invoiceSortingRepository.save(
            InvoiceSorting(
                mission_order_id = createInvoice.mission_order_id,
                invoice_number = createInvoice.invoice_number,
                date = createInvoice.date,
                cost_invoice = createInvoice.cost_invoice,
                part = createInvoice.part,
                part_number_invoice=createInvoice.part_number_invoice,
                amount_inv=createInvoice.amount_inv,
                sorting_time = createInvoice.sorting_time
            )
        ).toView()
}


