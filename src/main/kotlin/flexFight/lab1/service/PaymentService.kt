package flexFight.lab1.service

import com.mercadopago.MercadoPagoConfig
import com.mercadopago.client.preference.PreferenceBackUrlsRequest
import com.mercadopago.client.preference.PreferenceClient
import com.mercadopago.client.preference.PreferenceItemRequest
import com.mercadopago.client.preference.PreferenceRequest
import com.mercadopago.resources.preference.Preference
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PaymentService {

    init {
        MercadoPagoConfig.setAccessToken("") // Tu token de prueba
    }

    fun crearPreferencia(titulo: String, precio: Double): Preference {
        val item = PreferenceItemRequest.builder()
            .title(titulo)
            .quantity(1)
            .unitPrice(BigDecimal.valueOf(precio))
            .currencyId("ARS")
            .build()

        val backUrls = PreferenceBackUrlsRequest.builder()
            .success("http://localhost:5173/checkout-success")
            .failure("http://localhost:5173/checkout-failure")
            .pending("http://localhost:5173/checkout-pending")
            .build()

        val preferenceRequest = PreferenceRequest.builder()
            .items(listOf(item))
            .backUrls(backUrls)
            .autoReturn("approved")
            .build()

        val client = PreferenceClient()
        val preference = client.create(preferenceRequest)

        return preference
    }
}
