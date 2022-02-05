package no.nav.sosialhjelp.sosialhjelpfssproxy.norg

import no.nav.sosialhjelp.sosialhjelpfssproxy.common.Client
import no.nav.sosialhjelp.sosialhjelpfssproxy.exceptions.NorgException
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.HeaderUtils.HEADER_CALL_ID
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.HeaderUtils.getCallId
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class NorgClient(
    webClientBuilder: WebClient.Builder,
    @Value("\${fss-proxy.norg_url}") private val norgUrl: String
) : Client {
    private val norgWebClient = webClientBuilder.baseUrl(norgUrl).build()

    suspend fun hentNavEnhet(enhetsnr: String, request: ServerRequest): NavEnhet {
        log.debug("Forsøker å hente NAV-enhet $enhetsnr fra NORG2")

        try {
            return norgWebClient.get()
                .uri("/enhet/{enhetsnr}", enhetsnr)
                .accept(MediaType.APPLICATION_JSON)
                .header(HEADER_CALL_ID, getCallId(request))
                .retrieve()
                .awaitBody<NavEnhet>()
                .also { log.info("Hentet NAV-enhet $enhetsnr fra NORG2") }
        } catch (e: WebClientResponseException) {
            log.warn("Noe feilet ved kall mot NORG2 ${e.statusCode}", e)
            throw NorgException(e.message, e)
        }
    }

    suspend fun hentNavEnhetForGeografiskTilknytning(geografiskTilknytning: String, request: ServerRequest): NavEnhet {
        log.debug("Forsøker å hente NAV-enhet for geografiskTilknytning=$geografiskTilknytning fra NORG2")

        try {
            return norgWebClient.get()
                .uri("/enhet/navkontor/{geografiskTilknytning}", geografiskTilknytning)
                .accept(MediaType.APPLICATION_JSON)
                .header(HEADER_CALL_ID, getCallId(request))
                .retrieve()
                .awaitBody<NavEnhet>()
                .also { log.info("Hentet NAV-enhet for geografiskTilknytning=$geografiskTilknytning fra NORG2") }
        } catch (e: WebClientResponseException) {
            log.warn("Noe feilet ved kall mot NORG2 ${e.statusCode}", e)
            throw NorgException(e.message, e)
        }
    }

    // samme kall som selftest i soknad-api
    override suspend fun ping() {
        norgWebClient.get()
            .uri("/kodeverk/EnhetstyperNorg")
            .accept(MediaType.APPLICATION_JSON)
            .header(HEADER_CALL_ID, getCallId())
            .retrieve()
            .awaitBody<String>()
    }

    companion object {
        private val log by logger()
    }
}
