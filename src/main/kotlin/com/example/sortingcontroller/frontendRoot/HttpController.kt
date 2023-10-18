package com.example.sortingcontroller.frontendRoot
import com.example.sortingcontroller.rest.ViewInvoice
import com.example.sortingcontroller.rest.ViewMission
import com.example.sortingcontroller.service.Calculate
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.reactive.function.client.WebClient


@Controller
class HttpController(
    val webClientBuilder: WebClient.Builder
) {
    @GetMapping("invoices")
    fun indexInvoice(model: Model): String {
        // Build a WebClient instance using the autowired WebClient.Builder
        val webClient = webClientBuilder.baseUrl("http://localhost:8080").build()

        // Make a GET request to the "findAll" endpoint of the "InvoiceSortingController"
        val invoicesData: MutableList<ViewInvoice>? = webClient
            .get()
            .uri("/inv/all")
            .retrieve()
            .bodyToFlux(ViewInvoice::class.java)
            .collectList()
            .block()

        model.addAttribute("invoicesData", invoicesData)
        return "invoiceIndex"
    }


    @GetMapping("orders")
    fun indexMO(model: Model): String {
        // Build a WebClient instance using the autowired WebClient.Builder
        val webClient = webClientBuilder.baseUrl("http://localhost:8080").build()

        // Make a GET request to the "findAll" endpoint
        val ordersData: MutableList<ViewMission>? = webClient
            .get()
            .uri("/mo/all")
            .retrieve()
            .bodyToFlux(ViewMission::class.java)
            .collectList()
            .block()

        model.addAttribute("ordersData", ordersData)
        return "ordersIndex"
    }



    @PostMapping("/manage")
    fun manageMO(@RequestParam mo: String, model:Model): String {
        val costDifferences: MutableList<Double> = model.getAttribute("costDifferences") as? MutableList<Double> ?: mutableListOf()
        if (!mo.isNullOrBlank()) {
            val calculate = Calculate()
            val costDifference = calculate.calculateDiffInvMo(mo)
            costDifferences.add(costDifference)
            model.addAttribute("costDifferences", costDifferences)
        }
        return "redirect:/sorting" // You should have a corresponding Thymeleaf view for this.
    }


    @GetMapping("sorting")
    fun indexInv(@RequestParam(required = false, defaultValue = "") mo: String, model: Model): String {
        // Build a WebClient instance using the autowired WebClient.Builder
        val webClient = webClientBuilder.baseUrl("http://localhost:8080").build()

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

        //find inputed mo value and calculate the differnece of cost for all merged invoices calculateDiffInvMo(mo)
        //>Manage Mission Orders<
        val costDifferences: MutableList<Double> = model.getAttribute("costDifferences") as? MutableList<Double> ?: mutableListOf()
        if (!mo.isNullOrBlank()) {
            val calculate = Calculate()
            val costDifference = calculate.calculateDiffInvMo(mo)
            costDifferences.add(costDifference)
            model.addAttribute("costDifferences", costDifferences)
        }

        return "index"
        }


    }






