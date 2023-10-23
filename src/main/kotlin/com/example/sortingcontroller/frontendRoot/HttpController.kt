package com.example.sortingcontroller.frontendRoot
import com.example.sortingcontroller.rest.DB_data_collector
import com.example.sortingcontroller.rest.ViewInvoice
import com.example.sortingcontroller.rest.ViewMission
import com.example.sortingcontroller.service.Calculate
import com.example.sortingcontroller.service.Compare
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.reactive.function.client.WebClient



@Controller
class HttpController(
    val webClientBuilder: WebClient.Builder
) {
    // TODO vvtáhnout urlIndex do settings
    val urlIndex = "http://localhost:8080/"
    val data_collector = DB_data_collector()


    @GetMapping("sorting")
    fun indexInv(@RequestParam(required = false, defaultValue = "") mo: String, model: Model): String {
        // Build a WebClient instance using the autowired WebClient.Builder
        val webClient = webClientBuilder.baseUrl(urlIndex).build()

        // Make a GET request to the "findAll" endpoint of the "InvoiceSortingController"
        //>Invoices<
        val invoicesData: MutableList<ViewInvoice>? = webClient
            .get()
            .uri("/inv/all")
            .retrieve()
            .bodyToFlux(ViewInvoice::class.java)
            .collectList()
            .block()

        model.addAttribute("invoicesData", invoicesData)

        // Make a GET request to the "findAll" endpoint of the "MissionOrderController"
        //>Sorting orders<
        val ordersData: MutableList<ViewMission>? = webClient
            .get()
            .uri("/mo/all")
            .retrieve()
            .bodyToFlux(ViewMission::class.java)
            .collectList()
            .block()

        model.addAttribute("ordersData", ordersData)

        //>Manage Mission Orders<
        val costDifferences: MutableList<Double> =
            model.getAttribute("costDifferences") as? MutableList<Double> ?: mutableListOf()
        val amountDifferences: MutableList<Double> =
            model.getAttribute("amountDifferences") as? MutableList<Double> ?: mutableListOf()
        val timeDifferences: MutableList<Double> =
            model.getAttribute("timeDifferences") as? MutableList<Double> ?: mutableListOf()
        //val secondPerPartDifferences: Map<String, Double> = model.getAttribute("secondPerPartDifferences") as? Map<String, Double> ?: emptyMap()

        if (mo.isNotBlank()) {

            val calculate = Calculate()
            val compareTime = Compare.SortingTime()
            val comparePart = Compare.Parts()


            if (comparePart.compare_parts(mo)) {

                if (moExists(mo)) {
                    //find inputed mo value and calculate the difference of cost for all merged invoices calculateDiffInvMo(mo)
                    val costDifference = calculate.calculateDiffInvMo(mo)
                    costDifferences.add(costDifference)
                    model.addAttribute("costDifferences", costDifferences)

                    //find inputed mo value and calculate the difference of amount for all merged invoices calculateDiffInvMo(mo)
                    val amountDifference = calculate.calculateDifferencePartAmount(mo)
                    amountDifferences.add(amountDifference)
                    model.addAttribute("amountDifferences", amountDifference)

                    //find inputed mo value and calculate the difference of time for MO and merged invoices calculateDiffInvMo(mo)
                    val timeDifference = calculate.calculateDiffSortingTimeInvMo(mo)
                    timeDifferences.add(timeDifference)
                    model.addAttribute("timeDifferences", timeDifference)

                    //find sec / part for each invoice
                    val secondPerPartDiffMap = compareTime.compareSortingTimeInvMO(mo)
                    model.addAttribute("secondPerPartDifferences", secondPerPartDiffMap)
                }
            } else {
                println("add message part does not exists")
                model.addAttribute("message", "!DIFFERENT PART INVOICED AND ORDERED!")
            }
        }

        return "index"
    }

    fun moExists(mo: String): Boolean {

        val mockData = data_collector.get_all_mo_ids()
        return mo in mockData
    }

    @GetMapping("/addnew")
    fun dragNdrop(@RequestParam(required = false, defaultValue = "") fileName: String): String {
        // Process the file name as needed
        println("Received file name: $fileName")

        // Return the response or render the appropriate view
        return "dragNdrop"
    }

}





