package flexFight.lab1.controller

import flexFight.lab1.service.PaymentService
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/pagos")
class PaymentController(
    private val paymentService: PaymentService
) {

    @PostMapping("/crear")
    fun crearPago(@RequestBody request: CrearPagoRequest): ResponseEntity<Map<String, String>> {
        println("LLEGUE")
        val preference = paymentService.crearPreferencia(request.titulo, request.precio)
        return ResponseEntity.ok(mapOf("preferenceId" to preference.id))
    }
}

data class CrearPagoRequest(
    val titulo: String,
    val precio: Double
)
