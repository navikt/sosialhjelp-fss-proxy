package no.nav.sosialhjelp.sosialhjelpfssproxy.aareg

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
class AaregClient(
    webClientBuilder: WebClient.Builder,
    @Value("\${fss-proxy.aareg_url}") private val aaregUrl: String,
    @Value("\${fss-proxy.aareg_audience}") private val aaregAudience: String,
    private val tokendingsService: TokendingsService
) : Client {

    private val aaregWebClient = webClientBuilder.baseUrl(aaregUrl).build()

    suspend fun getArbeidsforhold(ident: String, request: ServerRequest): List<ArbeidsforholdDto>? {
        return aaregWebClient.get()
            .uri {
                it
                    .path("/v1/arbeidstaker/arbeidsforhold")
                    .queryParam("sporingsinformasjon", request.queryParam("sporingsinformasjon"))
                    .queryParam("regelverk", request.queryParam("regelverk"))
                    .queryParam("ansettelsesperiodeFom", request.queryParam("ansettelsesperiodeFom"))
                    .queryParam("ansettelsesperiodeTom", request.queryParam("ansettelsesperiodeTom"))
                    .build()
            }
            .accept(APPLICATION_JSON)
            .header(AUTHORIZATION, BEARER + tokenXtoken(request))
            .header(HEADER_CALL_ID, getCallId(request))
            .header(HEADER_NAV_PERSONIDENT, ident)
            .retrieve()
            .awaitBodyOrNull()
    }

    override suspend fun ping() {
        aaregWebClient.options()
            .uri("/ping")
            .header(HEADER_CALL_ID, getCallId())
            .retrieve()
            .awaitBodyOrNull<Any>()
    }

    private suspend fun tokenXtoken(request: ServerRequest): String {
        return tokendingsService.exchangeToken(request, aaregAudience)
    }
}
