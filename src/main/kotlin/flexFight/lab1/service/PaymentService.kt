//package flexFight.lab1.service
//
//import com.mercadopago.MercadoPagoConfig
//import com.mercadopago.client.preference.PreferenceBackUrlsRequest
//import com.mercadopago.client.preference.PreferenceClient
//import com.mercadopago.client.preference.PreferenceItemRequest
//import com.mercadopago.client.preference.PreferenceRequest
//import com.mercadopago.resources.preference.Preference
//import com.mercadopago.exceptions.MPApiException
//import flexFight.lab1.controller.CrearPagoRequest
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.stereotype.Service
//import org.slf4j.LoggerFactory
//import java.math.BigDecimal
//
//@Service
//class PaymentService(
//    @Value("\${mercadopago.access-token}")
//    private val accessToken: String
//) {
//
//    private val logger = LoggerFactory.getLogger(PaymentService::class.java)
//
//    init {
//        if (accessToken.isBlank() || accessToken == "TEST-YOUR_ACCESS_TOKEN_HERE") {
//            logger.error("MercadoPago access token not configured properly. Please set mercadopago.access-token in application.properties")
//            throw IllegalStateException("MercadoPago access token not configured")
//        }
//
//        MercadoPagoConfig.setAccessToken(accessToken)
//        logger.info("MercadoPago configured successfully with token: ${accessToken.take(20)}...")
//    }
//
//    fun crearPreferencia(request: CrearPagoRequest): Preference {
//        try {
//            logger.info("Creating payment preference for user: ${request.userId}, course: ${request.courseId}")
//            logger.info("Payment details - Title: ${request.titulo}, Price: ${request.precio}")
//
//            // Validaciones
//            if (request.precio <= 0) {
//                throw IllegalArgumentException("Price must be greater than 0")
//            }
//
//            if (request.titulo.isBlank()) {
//                throw IllegalArgumentException("Title cannot be blank")
//            }
//
//            if (request.titulo.length > 256) {
//                throw IllegalArgumentException("Title cannot exceed 256 characters")
//            }
//
//            if (request.userId.isBlank() || request.courseId.isBlank()) {
//                throw IllegalArgumentException("UserId and CourseId cannot be blank")
//            }
//
//            val item = PreferenceItemRequest.builder()
//                .title(request.titulo.trim())
//                .quantity(1)
//                .unitPrice(BigDecimal.valueOf(request.precio))
//                .currencyId("ARS")
//                .build()
//
//            val backUrls = PreferenceBackUrlsRequest.builder()
//                .success("https://www.mercadopago.com.ar/checkout/v1/success")
//                .failure("https://www.mercadopago.com.ar/checkout/v1/failure")
//                .pending("https://www.mercadopago.com.ar/checkout/v1/pending")
//                .build()
//
//            val metadata = mapOf(
//                "userId" to request.userId,
//                "courseId" to request.courseId,
//            )
//
//            val preferenceRequest = PreferenceRequest.builder()
//                .items(listOf(item))
//                .backUrls(backUrls)
//                .metadata(metadata)
//                // .notificationUrl("http://localhost:8081/api/pagos/webhook") // Comentado para desarrollo local
//                .externalReference("${request.userId}-${request.courseId}")
//                .build()
//
//            logger.info("Sending request to MercadoPago API...")
//            val client = PreferenceClient()
//            val preference = client.create(preferenceRequest)
//
//            logger.info("Payment preference created successfully with ID: ${preference.id}")
//            return preference
//
//        } catch (e: MPApiException) {
//            logger.error("MercadoPago API error: ${e.message}")
//            logger.error("API Error Details:")
//            logger.error("- Status Code: ${e.statusCode}")
//
//            try {
//                val response = e.apiResponse
//                if (response != null) {
//                    logger.error("- Response Status: ${response.statusCode}")
//                    logger.error("- Response Content: ${response.content}")
//                    logger.error("- Response Headers: ${response.headers}")
//                }
//            } catch (ex: Exception) {
//                logger.error("Could not extract API response details: ${ex.message}")
//            }
//
//            throw RuntimeException("Error creating payment preference. Status: ${e.statusCode}, Message: ${e.message}", e)
//        } catch (e: Exception) {
//            logger.error("Unexpected error creating payment preference", e)
//            throw RuntimeException("Unexpected error creating payment preference", e)
//        }
//    }
//}
