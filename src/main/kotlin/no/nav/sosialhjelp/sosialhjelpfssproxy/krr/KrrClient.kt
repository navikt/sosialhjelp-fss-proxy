package no.nav.sosialhjelp.sosialhjelpfssproxy.krr

import no.nav.sosialhjelp.sosialhjelpfssproxy.common.Client
import no.nav.sosialhjelp.sosialhjelpfssproxy.tokendings.TokendingsService
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.HeaderUtils.BEARER
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.HeaderUtils.HEADER_CALL_ID
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.HeaderUtils.HEADER_NAV_PERSONIDENT
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.HeaderUtils.getCallId
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class KrrClient(
    webClientBuilder: WebClient.Builder,
    @Value("\${fss-proxy.krr_url}") private val krrUrl: String,
    @Value("\${fss-proxy.krr_audience}") private val krrAudience: String,
    private val tokendingsService: TokendingsService
) : Client {

    private val krrWebClient = webClientBuilder.baseUrl(krrUrl).build()

    suspend fun getDigitalKontaktinformasjon(ident: String, request: ServerRequest): DigitalKontaktinformasjon? {
        return krrWebClient.get()
            .uri("/rest/v1/person")
            .accept(APPLICATION_JSON)
            .header(AUTHORIZATION, BEARER + tokenXtoken(request))
            .header(HEADER_CALL_ID, getCallId(request))
            .header(HEADER_NAV_PERSONIDENT, ident)
            .retrieve()
            .awaitBodyOrNull()
    }

    override suspend fun ping() {
        krrWebClient.options()
            .uri("/rest/ping")
            .accept(APPLICATION_JSON)
            .header(HEADER_CALL_ID, getCallId())
            .retrieve()
            .awaitBodyOrNull<Any>()
    }

    private suspend fun tokenXtoken(request: ServerRequest): String {
        return tokendingsService.exchangeToken(request, krrAudience)
    }
}
