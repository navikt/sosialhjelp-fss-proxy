package no.nav.sosialhjelp.sosialhjelpfssproxy.kodeverk

import no.nav.sosialhjelp.sosialhjelpfssproxy.common.Client
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.HeaderUtils.HEADER_CALL_ID
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.HeaderUtils.HEADER_CONSUMER_ID
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.HeaderUtils.getCallId
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.HeaderUtils.getConsumerId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class KodeverkClient(
    webClientBuilder: WebClient.Builder,
    @Value("\${fss-proxy.kodeverk_url}") private val kodeverkUrl: String
) : Client {

    private val norgWebClient = webClientBuilder.baseUrl(kodeverkUrl).build()

    suspend fun getKodeverk(kodeverksnavn: String, request: ServerRequest): KodeverkDto? {
        return norgWebClient.get()
            .uri {
                it
                    .path("/{kodeverksnavn}/koder/betydninger")
                    .queryParam("ekskluderUgyldige", true)
                    .queryParam("spraak", "nb")
                    .build(kodeverksnavn)
            }
            .header(HEADER_CALL_ID, getCallId(request))
            .header(HEADER_CONSUMER_ID, getConsumerId(request))
            .retrieve()
            .awaitBodyOrNull()
    }

    override suspend fun ping() {
        norgWebClient.get()
            .header(HEADER_CALL_ID, getCallId())
            .header(HEADER_CONSUMER_ID, getConsumerId())
            .retrieve()
            .awaitBodyOrNull<String>()
    }
}
