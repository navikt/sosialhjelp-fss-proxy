package no.nav.sosialhjelp.sosialhjelpfssproxy.ereg

import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.HeaderUtils.HEADER_CALL_ID
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.HeaderUtils.getCallId
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class EregClient(
    webClientBuilder: WebClient.Builder,
    @Value("\${fss-proxy.ereg_url}") private val eregUrl: String
) {
    private val eregWebClient = webClientBuilder.baseUrl(eregUrl).build()

    suspend fun getOrganisasjon(orgnr: String, request: ServerRequest): OrganisasjonNoekkelinfoDto? {
        return eregWebClient.get()
            .uri { it.path("/v1/organisasjon/{orgnr}/noekkelinfo").build(orgnr) }
            .accept(MediaType.APPLICATION_JSON)
            .header(HEADER_CALL_ID, getCallId(request))
//            .header(HEADER_CONSUMER_ID, getConsumerId(request))
            .retrieve()
            .awaitBodyOrNull()
    }

    suspend fun ping() {
        eregWebClient.get()
            .uri("/v1/organisasjon/990983666/noekkelinfo")
            .accept(MediaType.APPLICATION_JSON)
            .header(HEADER_CALL_ID, getCallId())
//            .header(HEADER_CONSUMER_ID, getConsumerId())
            .retrieve()
            .awaitBodyOrNull<OrganisasjonNoekkelinfoDto>()
    }
}
