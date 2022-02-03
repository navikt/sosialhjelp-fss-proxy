package no.nav.sosialhjelp.sosialhjelpfssproxy.kodeverk

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull

@Component
class KodeverkClient(
    webClientBuilder: WebClient.Builder,
    @Value("\${fss-proxy.kodeverk_url}") private val kodeverkUrl: String
) {

    private val norgWebClient = webClientBuilder.baseUrl(kodeverkUrl).build()

    suspend fun getKodeverk(kodeverksnavn: String): KodeverkDto? {
        return norgWebClient.get()
            .uri {
                it
                    .path("/{kodeverksnavn}/koder/betydninger")
                    .queryParam("ekskluderUgyldige", true)
                    .queryParam("spraak", "nb")
                    .build(kodeverksnavn)
            }
//            .header(HEADER_CALL_ID, MDCUtils.get(MDCUtils.CALL_ID))
//            .header(HEADER_CONSUMER_ID, SubjectHandler.getConsumerId())
            .retrieve()
            .awaitBodyOrNull()
    }

    suspend fun ping() {
        norgWebClient.get()
//            .header(HEADER_CALL_ID, MDCUtils.get(MDCUtils.CALL_ID))
//            .header(HEADER_CONSUMER_ID, SubjectHandler.getConsumerId())
            .retrieve()
            .awaitBodyOrNull<String>()
    }
}
