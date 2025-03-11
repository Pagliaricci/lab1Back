package flexFight.lab1.controller

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/payments")
class PaymentController {

    private val accessToken = System.getenv("ACCESS_TOKEN")

    @PostMapping("/create")
    fun createPayment(@RequestBody paymentRequest: PaymentRequest): ResponseEntity<Map<String, String>> {
        val url = "https://api.mercadopago.com/checkout/preferences"
        val headers = org.springframework.http.HttpHeaders().apply {
            add("Authorization", "Bearer $accessToken")
            add("Content-Type", "application/json")
        }

        val requestBody = """
            {
                "items": [
                    {
                        "title": "${paymentRequest.title}",
                        "quantity": 1,
                        "currency_id": "ARS",
                        "unit_price": ${paymentRequest.price}
                    }
                ],
                "payer": { "email": "user@example.com" },
                "back_urls": {
                    "success": "http://localhost:5173/success",
                    "failure": "http://localhost:5173/failure",
                    "pending": "http://localhost:5173/pending"
                },
                "auto_return": "approved"
            }
        """

        val requestEntity = HttpEntity(requestBody, headers)
        val restTemplate = RestTemplate()
        val response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map::class.java)

        val preferenceId = response.body?.get("id") as String
        return ResponseEntity.ok(mapOf("preferenceId" to preferenceId))
    }
}

data class PaymentRequest(
    val userId: String,
    val title: String,
    val price: Double
)
