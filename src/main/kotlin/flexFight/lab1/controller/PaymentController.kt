package flexFight.lab1.controller

import com.mercadopago.client.payment.PaymentClient
import flexFight.lab1.entity.AddSubscriber
import flexFight.lab1.service.CourseService
import flexFight.lab1.service.PaymentService
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity


@RestController
@RequestMapping("/api/pagos")
class PaymentController(
    private val paymentService: PaymentService,
    private val courseService: CourseService
) {

    @PostMapping("/crear")
    fun crearPago(@RequestBody request: CrearPagoRequest): ResponseEntity<Map<String, String>> {
        println("LLEGUE")
        val preference = paymentService.crearPreferencia(request)
        return ResponseEntity.ok(mapOf("preferenceId" to preference.id))
    }

    @PostMapping("/webhook")
    fun recibirWebhook(@RequestBody payload: Map<String, Any>): ResponseEntity<Void> {
        val type = payload["type"] as? String
        val data = payload["data"] as? Map<*, *>
        val paymentId = data?.get("id")?.toString()

        if (type == "payment" && paymentId != null) {
            // Buscar el pago con la API de Mercado Pago para verificar estado
            val client = PaymentClient()
            val payment = client.get(paymentId.toLong())

            if (payment.status == "approved") {
                val metadata = payment.metadata

                val userId = metadata["userId"]?.toString()
                val courseId = metadata["courseId"]?.toString()

                if (userId != null && courseId != null) {
                    courseService.subscribeToCourse(AddSubscriber(userId, courseId))
                }
            }
        }

        return ResponseEntity.ok().build()
    }

}

data class CrearPagoRequest(
    val titulo: String,
    val precio: Double,
    val userId: String,
    val courseId: String
)
