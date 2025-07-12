package flexFight.lab1.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/api/pagos")
class PagoController(
    @Value("\${mercadopago.access-token}") val accessToken: String
) {
    val restTemplate = RestTemplate()

    @PostMapping("/preferencia")
    fun crearPreferencia(@RequestParam courseId: String, @RequestParam precio: Double,  @RequestParam userId: String): ResponseEntity<MercadoPagoPreferenceResponse> {
        val url = UriComponentsBuilder
            .fromHttpUrl("https://api.mercadopago.com/checkout/preferences")
            .queryParam("access_token", accessToken)
            .toUriString()

        val body = mapOf(
            "items" to listOf(
                mapOf(
                    "title" to courseId,
                    "quantity" to 1,
                    "unit_price" to precio,
                    "currency_id" to "ARS"
                )
            ),
            "back_urls" to mapOf(
                "success" to "https://lab1-redirect.vercel.app?courseId=$courseId&userId=$userId",
                "success" to "https://lab1-redirect.vercel.app?courseId=$courseId&userId=$userId",
                "success" to "https://lab1-redirect.vercel.app?courseId=$courseId&userId=$userId"
            ),
            "auto_return" to "approved"
        )

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = HttpEntity(body, headers)

        val response = restTemplate.postForEntity(url, request, Map::class.java)
        val initPoint = response.body?.get("init_point") as? String
            ?: return ResponseEntity.internalServerError().build()

        return ResponseEntity.ok(MercadoPagoPreferenceResponse(init_point = initPoint))
    }

    data class MercadoPagoPreferenceResponse(
        val init_point: String
    )
}